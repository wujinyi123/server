package com.system.common.service;

import com.system.base.domain.CurrentUser;
import com.system.common.pojo.user.UpdatePasswordDTO;
import com.system.common.pojo.user.UserQO;

import javax.servlet.http.HttpServletRequest;

public interface IUserService {
    CurrentUser login(HttpServletRequest request, UserQO userQO);

    CurrentUser getCurrentUser(HttpServletRequest request);

    Boolean logout(HttpServletRequest request);

    Boolean updatePassword(UpdatePasswordDTO dto);
}
