package com.system.common.mapper;

import com.system.common.model.UserModel;
import org.apache.ibatis.annotations.Param;

public interface IUserMapper {
    UserModel getUserByPassword(@Param("username") String username,
                                @Param("password") String password);

    int updatePassword(@Param("username") String username,
                       @Param("password") String password,
                       @Param("updateInfo") String updateInfo);
}
