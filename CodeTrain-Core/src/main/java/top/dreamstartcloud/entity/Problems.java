package top.dreamstartcloud.entity;

import lombok.Data;

@Data
public class Problems {

    private String id;
    private String title;
    private Integer memory;
    private Integer time;
    private String body;
    private Integer accepts;
    private Integer submits;
    private String pid;
}
