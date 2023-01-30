package com.system.common.service;

import com.system.common.model.MenuModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IMenuService {
    Map<String, Object> currentMenu(HttpServletRequest request);

    Map<String, Object> allMenu();

    Boolean insertMenu(MenuModel model);

    Boolean updateMenu(MenuModel model);

    Boolean deleteMenu(String code);
}
