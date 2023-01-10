package com.system.common.service.impl;

import com.system.common.mapper.IMenuMapper;
import com.system.common.model.MenuModel;
import com.system.common.pojo.menu.MenuTreeDTO;
import com.system.common.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuServiceImpl implements IMenuService {
    @Autowired
    private IMenuMapper menuMapper;

    @Override
    public Map<String, Object> currentMenu() {
        List<MenuModel> list = menuMapper.listMenu();
        List<MenuTreeDTO> allNodes = new ArrayList<>();
        MenuTreeDTO dto;
        for (MenuModel model : list) {
            dto = new MenuTreeDTO();
            BeanUtils.copyProperties(model, dto);
            allNodes.add(dto);
        }
        createMenuTree(allNodes);
        Map<String, Object> map = new HashMap<>();
        map.put("tree", allNodes.stream()
                .filter(item->StringUtils.isEmpty(item.getParentCode()))
                .collect(Collectors.toList()));
        map.put("routes",allNodes.stream()
                .filter(item->item.getType().equals((byte)2))
                .collect(Collectors.toList()));
        return map;
    }

    private void createMenuTree(List<MenuTreeDTO> allNodes) {
        for (MenuTreeDTO treeDTO : allNodes) {
            treeDTO.setChildren(allNodes.stream()
                    .filter(item -> treeDTO.getCode().equals(item.getParentCode()))
                    .collect(Collectors.toList()));
        }
        createPaths(allNodes.stream()
                .filter(item->StringUtils.isEmpty(item.getParentCode()))
                .collect(Collectors.toList()),
                new ArrayList<>(),
                new ArrayList<>());
    }

    private void createPaths(List<MenuTreeDTO> nodes, List<String> codes, List<String> names) {
        for (MenuTreeDTO dto:nodes) {
            codes.add(dto.getCode());
            names.add(dto.getName());
            dto.setCodes(codes.stream().collect(Collectors.toList()));
            dto.setNames(names.stream().collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(dto.getChildren())) {
                createPaths(dto.getChildren(),
                        codes.stream().collect(Collectors.toList()),
                        names.stream().collect(Collectors.toList()));
            }
            codes.remove(codes.size()-1);
            names.remove(names.size()-1);
        }
    }
}
