package com.system.file.domain.model;

import lombok.Data;

import java.util.Date;

@Data
public class TemplateTaskModel {
    private Long id;
    private String code;
    private Byte type;
    private String status;
    private String taskResult;
    private String result;
    private String error;
    private String operaUser;
    private Date createTime;
    private Date updateTime;
}
