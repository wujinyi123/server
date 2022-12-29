package com.system.base.domain;

import lombok.Data;

import java.util.Map;

@Data
public class CurrentUser {
    private String token;
    private Map<String, Object> user;
}
