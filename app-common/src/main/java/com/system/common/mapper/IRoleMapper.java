package com.system.common.mapper;

import com.system.common.model.RoleModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IRoleMapper {
    List<RoleModel> listRole();

    RoleModel getByCode(@Param("code") String code);

    int insertRole(RoleModel model);

    int updateRole(RoleModel model);

    int deleteRole(@Param("code") String code);
}
