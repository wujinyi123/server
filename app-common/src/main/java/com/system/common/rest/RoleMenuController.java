package com.system.common.rest;

import com.system.common.domain.model.RoleMenuModel;
import com.system.common.service.IRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleMenuController {
    @Autowired
    private IRoleMenuService roleMenuService;

    @GetMapping("/role/menu")
    public List<RoleMenuModel> listByRoleCode(@RequestParam String roleCode) {
        return roleMenuService.listByRoleCode(roleCode);
    }

    @PostMapping("/role/menu")
    public Boolean saveRoleMenu(@RequestParam String roleCode,
                                @RequestBody List<String> menuCodes) {
        return roleMenuService.saveRoleMenu(roleCode, menuCodes);
    }
}
