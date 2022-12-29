package com.system.common.rest;

import com.system.base.domain.CurrentUser;
import com.system.common.pojo.user.UserQO;
import com.system.common.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/user/login")
    public CurrentUser login(HttpServletRequest request,
                             @RequestBody UserQO userQO) {
        return userService.login(request, userQO);
    }

    @GetMapping("/user/current")
    public CurrentUser getCurrentUser(HttpServletRequest request) {
        return userService.getCurrentUser(request);
    }
}
