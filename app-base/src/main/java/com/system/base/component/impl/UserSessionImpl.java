package com.system.base.component.impl;

import com.system.base.component.IUserSession;
import com.system.base.domain.CurrentUser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class UserSessionImpl implements IUserSession {

    @Override
    public CurrentUser setAttibute(HttpServletRequest request, Map<String, Object> user) {
        CurrentUser currentUser = new CurrentUser();
        HttpSession session = request.getSession();
        currentUser.setToken(session.getId());
        currentUser.setUser(user);
        session.setAttribute("user", currentUser);
        return currentUser;
    }

    @Override
    public CurrentUser getAttibute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        CurrentUser currentUser = (CurrentUser) session.getAttribute("user");
        return currentUser;
    }
}
