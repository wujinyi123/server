package com.system.common.service;

import com.system.common.domain.model.RoleModel;

import java.util.List;

public interface IRoleService {
    List<RoleModel> listRole();

    Boolean insertRole(RoleModel model);

    Boolean updateRole(RoleModel model);

    Boolean deleteRole(String code);
}
