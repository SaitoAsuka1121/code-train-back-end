package top.dreamstartcloud.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author liu
 */
@Data
@AllArgsConstructor
public class Result {
    private boolean success;
    private Integer code;
    private String msg;
    private Object data;
    public static Result success(Object data){
        return new Result(true,200,"success",data);
    }
    public static Result fail(Integer code,String msg){
        return new Result(false,200,msg,null);
    }
}
