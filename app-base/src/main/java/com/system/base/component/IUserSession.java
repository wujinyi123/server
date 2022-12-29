package com.system.base.component;

import com.system.base.domain.CurrentUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IUserSession {
    CurrentUser setAttibute(HttpServletRequest request, Map<String, Object> user);

    CurrentUser getAttibute(HttpServletRequest request);
}
