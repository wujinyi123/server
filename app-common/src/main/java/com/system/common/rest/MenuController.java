package com.system.common.rest;

import com.system.common.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
