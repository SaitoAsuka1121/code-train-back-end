package top.dreamstartcloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author liu
 */
@Data
public class User {
    /**
     *  数据库为bigint，Long放不下
     */
   @TableId(type = IdType.ASSIGN_ID)
   private String id;

   private String account;

   private Boolean admin;

   private String createDate;

   private String email;

   private String mobilePhoneNumber;

   private String nickname;

   private String password;

   private String salt;
}
