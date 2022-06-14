package top.dreamstartcloud.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(value = "页码请求格式")
@Data
public class PageParamsDTO {
    @ApiModelProperty(value = "页数")
    private Integer page;
}