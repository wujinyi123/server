package com.system.common.domain.model;

import lombok.Data;

@Data
public class MenuModel {
    private Long id;
    private String code;
    private String name;
    private String icon;
    private String route;
    private String parentCode;
    private Byte type;
    private Integer sortNo;
}
