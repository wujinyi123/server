package com.system.base.domain;

import lombok.Data;

@Data
public class TaskResult {
    private Boolean isAlive = true;
    private String taskCode;
    private Object result;
}
