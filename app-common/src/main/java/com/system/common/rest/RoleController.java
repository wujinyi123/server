package com.system.common.rest;

import com.system.common.domain.model.RoleModel;
import com.system.common.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @GetMapping("/common/role")
    public List<RoleModel> listRole() {
        return roleService.listRole();
    }

    @PostMapping("/common/role")
    public Boolean insertRole(@RequestBody RoleModel model) {
        return roleService.insertRole(model);
    }

    @PutMapping("/common/role")
    public Boolean updateRole(@RequestBody RoleModel model) {
        return roleService.updateRole(model);
    }

    @DeleteMapping("/common/role")
    public Boolean deleteRole(@RequestParam String code) {
        return roleService.deleteRole(code);
    }
}
