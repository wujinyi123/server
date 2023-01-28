package com.system.common.mapper;

import com.system.common.model.MenuModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IMenuMapper {
    List<MenuModel> listMenu();

    MenuModel getByCode(@Param("code") String code);

    int insertMenu(MenuModel model);

    int updateMenu(MenuModel model);

    int deleteMenu(List<String> codeList);
}
