package top.dreamstartcloud.utils;


import top.dreamstartcloud.entity.User;

public class UserThreadLocal {
    private UserThreadLocal(){}
    private static final  ThreadLocal<User> LOCAL = new InheritableThreadLocal<>();
    public static void put(User sysUser){
        LOCAL.set(sysUser);
    }
    public static User get(){
        return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }
}