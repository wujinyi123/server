package com.system.common.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserModel {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String sex;
    private String tel;
    private String email;
    private String roleCode;
    private String img;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    private String updateInfo;
}
