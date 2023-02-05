package com.system.common.service;

import com.system.common.domain.model.RoleMenuModel;

import java.util.List;

public interface IRoleMenuService {
    List<RoleMenuModel> listByRoleCode(String roleCode);

    Boolean saveRoleMenu(String roleCode, List<String> menuCodes);
}
