package top.dreamstartcloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import top.dreamstartcloud.dto.LoginDTO;
import top.dreamstartcloud.dto.RegisterDTO;
import top.dreamstartcloud.dto.TokenClass;
import top.dreamstartcloud.dto.UpdateuserDTO;
import top.dreamstartcloud.entity.User;
import top.dreamstartcloud.ibo.LoginIBO;
import top.dreamstartcloud.bo.Result;
import top.dreamstartcloud.ibo.RegisterIBO;
import top.dreamstartcloud.ibo.TokenIBO;
import top.dreamstartcloud.ibo.UpdateuserIBO;
import top.dreamstartcloud.service.UserService;
import top.dreamstartcloud.utils.UserThreadLocal;

import javax.annotation.Resource;

/**
 * @author liu
 */
@Slf4j
@RestController
@RequestMapping("users")
@Api(tags = "用户模块", value = "用户模块")
public class UsersController {
    @Resource
    UserService userService;
    @PostMapping("login")
    @ApiOperation(value = "登录接口", notes = "登录")
    public Result login(@RequestBody LoginDTO loginDTO){
        LoginIBO loginParam = new LoginIBO();
        BeanUtils.copyProperties(loginDTO,loginParam);
        return userService.login(loginParam);
    }
    @PostMapping("register")
    @ApiOperation(value = "注册接口", notes = "注册")
    public Result register(@RequestBody RegisterDTO registerDTO){
        RegisterIBO registerParam = new RegisterIBO();
        BeanUtils.copyProperties(registerDTO,registerParam);
        return userService.register(registerParam);
    }
    @PostMapping("logout")

    @ApiOperation(value = "退出接口", notes = "退出")
    public Result logout(@RequestBody TokenClass token){
        TokenIBO tokenParam = new TokenIBO();
        BeanUtils.copyProperties(token,tokenParam);
        return userService.logout(tokenParam);
    }
    @PostMapping("updateuser")
    @ApiOperation(value = "用户信息接口", notes = "用户信息")
    public Result updateuser(@RequestBody UpdateuserDTO updateuserDTO){
        UpdateuserIBO updateuserParam = new UpdateuserIBO();
        BeanUtils.copyProperties(updateuserDTO,updateuserParam);
        User user = UserThreadLocal.get();
        String id = user.getId();
        return userService.updateUser(updateuserParam,id);
    }
}
