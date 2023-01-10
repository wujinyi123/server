package com.system.common.pojo.menu;

import com.system.common.model.MenuModel;
import lombok.Data;

import java.util.List;

@Data
public class MenuTreeDTO extends MenuModel {
    List<String> codes;
    List<String> names;
    List<MenuTreeDTO> children;
}
