package com.system.base.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class CurrentUser {
    private String token;
    private Map<String, Object> user;
}
