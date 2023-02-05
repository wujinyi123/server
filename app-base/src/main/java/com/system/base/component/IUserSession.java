package com.system.base.component;

import com.system.base.domain.CurrentUser;

import java.util.Map;

public interface IUserSession {
    CurrentUser setAttibute(Map<String, Object> user);

    CurrentUser getAttibute();

    CurrentUser getAttibute(String username);

    Boolean removeAttibute();

    Boolean removeAttibute(String username);
}
