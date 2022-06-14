package top.dreamstartcloud.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Match {
    private String matchId;
    private String problemsId;
    private Date startTime;
    private Date endTime;
    private String userId;
    private String matchName;
}
