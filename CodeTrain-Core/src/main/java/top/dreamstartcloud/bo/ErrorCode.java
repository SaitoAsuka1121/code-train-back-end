package top.dreamstartcloud.bo;

/**
 * @author liu
 */
public enum  ErrorCode {
    /**
     *    统一错误码
     */
    PARAMS_ERROR(1000,"参数不全或参数错误"),
    ACCOUNT_PWD_NOT_EXIST(1002,"用户名或密码不正确"),
    NO_PERMISSION(1003,"无访问权限"),
    SESSION_TIME_OUT(1004,"会话超时"),
    NO_LOGIN(1005,"未登录"),
    TOKEN_EXIST(1006,"Token不合法"),
    ACCOUNT_EXIST(1007,"账号已存在"),
    SYSTEM_ERROR(9999,"系统发生错误"),
    NO_PROBLEM(1008,"题目不存在"),;
    private int code;
    private String msg;

    ErrorCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}