package com.system.common.rest;

import com.system.common.domain.model.MenuModel;
import com.system.common.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MenuController {
    @Autowired
    private IMenuService menuService;

    @GetMapping("/menu/current")
    public Map<String, Object> currentMenu() {
        return menuService.currentMenu();
    }

    @GetMapping("/menu/info")
    public Map<String, Object> allMenu() {
        return menuService.allMenu();
    }

    @PostMapping("/menu/info")
    public Boolean insertMenu(@RequestBody MenuModel model) {
        return menuService.insertMenu(model);
    }

    @PutMapping("/menu/info")
    public Boolean updateMenu(@RequestBody MenuModel model) {
        return menuService.updateMenu(model);
    }

    @DeleteMapping("/menu/info")
    public Boolean deleteMenu(@RequestParam String code) {
        return menuService.deleteMenu(code);
    }
}
