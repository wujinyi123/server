package com.system.common.domain.enums;

public enum UpdateUserEnum {
    UPDATE_PASSWORD("updatePassword", "修改密码"),
    UPDATE_INFO("updateInfo", "修改用户信息");

    private String code;
    private String message;

    UpdateUserEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
