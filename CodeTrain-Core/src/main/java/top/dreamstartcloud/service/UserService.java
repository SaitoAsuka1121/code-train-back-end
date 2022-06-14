package top.dreamstartcloud.service;

import top.dreamstartcloud.entity.User;
import top.dreamstartcloud.ibo.LoginIBO;
import top.dreamstartcloud.bo.Result;
import top.dreamstartcloud.ibo.RegisterIBO;
import top.dreamstartcloud.ibo.TokenIBO;
import top.dreamstartcloud.ibo.UpdateuserIBO;

/**
 * @author liu
 */

public interface UserService {
    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginIBO loginParam);

    /**
     * 注册功能
     * @param registerParam
     * @return
     */
    Result register(RegisterIBO registerParam);

    /**
     * 查看用户加密盐
     * @param account
     * @return
     */
    User findUserSlat(String account);

    /**
     * 验证账号密码
     * @param account
     * @param password
     * @return
     */
    User findUser(String account, String password);

    /**
     * 查找用户
     * @param account
     * @return
     */
    User findUserByAccount(String account);

    /**
     * 退出用户功能
     * @param token
     * @return
     */
    Result logout(TokenIBO tokenParam);

    /**
     * 更新用户信息
     * @param updateuserParam
     * @return
     */
    Result updateUser(UpdateuserIBO updateuserParam,String id);

    User checkToken(String token);
}
