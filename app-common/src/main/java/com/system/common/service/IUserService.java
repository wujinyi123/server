package com.system.common.service;

import com.system.base.domain.CurrentUser;
import com.system.common.pojo.user.UpdatePasswordDTO;
import com.system.common.pojo.user.UserQO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IUserService {
    CurrentUser login(HttpServletRequest request, UserQO userQO);

    void getCurrentUser(HttpServletRequest request, HttpServletResponse response);

    Boolean logout(HttpServletRequest request);

    Boolean updatePassword(UpdatePasswordDTO dto);
}
