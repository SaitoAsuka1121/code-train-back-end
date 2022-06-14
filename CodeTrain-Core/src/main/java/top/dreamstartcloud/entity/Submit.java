package top.dreamstartcloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Submit {
    private String userId;
    private String problemId;
    private String type;
    @TableId(type = IdType.NONE)
    private String id;
    private Date CreatedTime;
    private String language;
}
