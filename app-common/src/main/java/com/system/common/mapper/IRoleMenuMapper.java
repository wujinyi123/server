package com.system.common.mapper;

import com.system.common.model.RoleMenuModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IRoleMenuMapper {
    List<RoleMenuModel> listByRoleCode(@Param("roleCode") String roleCode);

    int deleteByRoleCode(@Param("roleCode") String roleCode);

    int insertRoleMenu(List<RoleMenuModel> list);
}
