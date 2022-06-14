package top.dreamstartcloud.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.dreamstartcloud.bo.ErrorCode;
import top.dreamstartcloud.bo.Result;
import top.dreamstartcloud.entity.User;
import top.dreamstartcloud.ibo.LoginIBO;
import top.dreamstartcloud.ibo.RegisterIBO;
import top.dreamstartcloud.ibo.TokenIBO;
import top.dreamstartcloud.ibo.UpdateuserIBO;
import top.dreamstartcloud.mapper.UserMapper;
import top.dreamstartcloud.service.UserService;
import top.dreamstartcloud.utils.JwtUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static top.dreamstartcloud.bo.ErrorCode.ACCOUNT_PWD_NOT_EXIST;
import static top.dreamstartcloud.bo.ErrorCode.PARAMS_ERROR;

/**
 * @author liu
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Result login(LoginIBO loginParam) {
        String username = loginParam.getUsername();
        String password= loginParam.getPassword();
        //格式错误
        if(StringUtils.isBlank(username) || StringUtils.isBlank(username) ){
            return Result.fail(PARAMS_ERROR.getCode(), PARAMS_ERROR.getMsg());
        }
        User user = userService.findUserSlat(username);
        //账号错误
        if(user==null){
            return Result.fail(ACCOUNT_PWD_NOT_EXIST.getCode(), ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String slat = user.getSalt();
        password = DigestUtils.md5Hex(password + slat);
        User sysUser= userService.findUser(username,password);
        // 账号密码错误
        if(sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JwtUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public Result register(RegisterIBO registerParam) {
        String username = registerParam.getUsername();
        String password = registerParam.getPassword();
        String nickname = registerParam.getNickname();
        // 格式错误
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //重复注册
        User user = userService.findUserByAccount(username);
        if(user!=null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        //生成随机加密盐
        String slat = getRandomSlat();
        // 插入准备
        user = new User();
        user.setAccount(username);
        user.setPassword(DigestUtils.md5Hex(password+slat));
        user.setNickname(nickname);
        user.setAdmin(false);
        user.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        user.setSalt(slat);
        user.setEmail(null);
        user.setMobilePhoneNumber(null);
        //插入数据库
        userMapper.insert(user);
        //生成Token
        String token = JwtUtils.createToken(user.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(user),1, TimeUnit.DAYS);
        return Result.success(token);
    }
    @Override
    public Result logout(TokenIBO param) {
        String token = param.getToken();
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    @Override
    public Result updateUser(UpdateuserIBO updateUserParam,String id) {
        String email = updateUserParam.getEmail();
        String mobilePhoneNumber = updateUserParam.getMobilePhoneNumber();
        String nickname = updateUserParam.getNickname();
        String password = updateUserParam.getPassword();
        return null;
    }

    /**
     * 生成随机加密盐
     * @return String
     */
    public String getRandomSlat(){
        String randStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        int len =4;
        for (int i=0;i< len ;i++){
            stringBuilder.append(randStr.charAt(random.nextInt(randStr.length())));
        }
        return stringBuilder.toString();
    }
    /**
     * 查找用户加密盐
     * @return User
     */
    @Override
    public User findUserSlat(String account){
        LambdaQueryWrapper<User> slat= new LambdaQueryWrapper();
        slat.eq(User::getAccount,account);
        slat.select(User::getSalt);
        slat.last("limit 1");
        return userMapper.selectOne(slat);
    }
    @Override
    public User findUserByAccount(String account){
        LambdaQueryWrapper<User> slat= new LambdaQueryWrapper();
        slat.eq(User::getAccount,account);
        slat.select(User::getSalt);
        slat.last("limit 1");
        return userMapper.selectOne(slat);
    }
    /**
     * 验证用户账号密码
     * @return User
     */
    @Override
    public User findUser(String account, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount,account);
        queryWrapper.eq(User::getPassword,password);
        queryWrapper.select(User::getAccount,User::getNickname);
        queryWrapper.last("limit 1");
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User checkToken(String token) {
        if(StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> stringObjectMap = JwtUtils.checkToken(token);
        if(stringObjectMap == null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_"+token);
        if(StringUtils.isBlank(userJson)){
            return null;
        }
        return JSON.parseObject(userJson, User.class);
    }
}
