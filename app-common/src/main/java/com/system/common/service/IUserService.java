package com.system.common.service;

import com.github.pagehelper.PageInfo;
import com.system.base.domain.CurrentUser;
import com.system.common.domain.dto.user.UpdatePasswordDTO;
import com.system.common.domain.model.UserModel;
import com.system.common.domain.qo.user.LoginQO;
import com.system.common.domain.qo.user.UserQO;

import javax.servlet.http.HttpServletRequest;

public interface IUserService {
    CurrentUser login(HttpServletRequest request, LoginQO loginQO);

    CurrentUser getCurrentUser(HttpServletRequest request);

    Boolean logout(HttpServletRequest request);

    Boolean updatePassword(UpdatePasswordDTO dto);

    PageInfo<UserModel> pageUser(UserQO qo);

    Boolean addUser(UserModel model);

    Boolean updateUser(UserModel model);

    Boolean deleteUser(String username);
}
