package com.system.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCodeEnum {
    SUCCESS(1000, "请求成功"),
    FAILED(1001, "请求失败");

    private int code;
    private String msg;
}
