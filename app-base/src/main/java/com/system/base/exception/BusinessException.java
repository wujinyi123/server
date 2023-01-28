package com.system.base.exception;

import com.system.base.enums.ResultCodeEnum;

public class BusinessException extends RuntimeException {
    private int code;

    private String msg;

    // 手动设置异常
    public BusinessException(ResultCodeEnum codeEnum, String message) {
        // message用于用户设置抛出错误详情
        super(message);
        // 状态码
        this.code = codeEnum.getCode();
        // 状态码配套的msg
        this.msg = codeEnum.getMsg();
    }

    // 默认异常使用APP_ERROR状态码
    public BusinessException(String message) {
        super(message);
        this.code = ResultCodeEnum.FAILED.getCode();
        this.msg = ResultCodeEnum.FAILED.getMsg();
    }
}
