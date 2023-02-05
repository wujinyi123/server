package com.system.common.mapper;

import com.system.common.domain.model.UserModel;
import com.system.common.domain.qo.user.LoginQO;
import com.system.common.domain.qo.user.UserQO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IUserMapper {
    UserModel getUserByPassword(LoginQO loginQO);

    int updatePassword(@Param("username") String username,
                       @Param("password") String password,
                       @Param("updateInfo") String updateInfo);

    List<UserModel> listUser(UserQO userQO);

    UserModel getUser(@Param("username") String username);

    int addUser(UserModel model);

    int updateUser(UserModel model);

    int deleteUser(@Param("username") String username);
}
