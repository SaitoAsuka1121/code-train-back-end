package top.dreamstartcloud.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
@ApiModel(value = "修改用户格式")
public class UpdateuserDTO {
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "手机号")
    private String mobilePhoneNumber;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "密码")
    private String password;
}
