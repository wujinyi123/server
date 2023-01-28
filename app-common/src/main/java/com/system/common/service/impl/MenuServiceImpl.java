package com.system.common.service.impl;

import com.system.base.exception.BusinessException;
import com.system.base.util.DaoUtil;
import com.system.base.util.SnowflakeIdUtil;
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
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class MenuServiceImpl implements IMenuService {
    @Autowired
    private IMenuMapper menuMapper;

    @Override
    public Map<String, Object> currentMenu() {
        List<MenuModel> list = menuMapper.listMenu();
        return getMenuResult(list);
    }

    @Override
    public Map<String, Object> allMenu() {
        List<MenuModel> list = menuMapper.listMenu();
        return getMenuResult(list);
    }

    @Override
    public Boolean insertMenu(MenuModel model) {
        if (Objects.nonNull(menuMapper.getByCode(model.getCode()))) {
            throw new BusinessException("菜单代码已存在");
        }
        model.setId(SnowflakeIdUtil.getSnowflakeId());
        if (DaoUtil.isInsertFail(menuMapper.insertMenu(model))) {
            throw new BusinessException("添加失败");
        }
        return true;
    }

    @Override
    public Boolean updateMenu(MenuModel model) {
        if (DaoUtil.isUpdateFail(menuMapper.updateMenu(model))) {
            throw new BusinessException("修改失败");
        }
        return true;
    }

    @Override
    public Boolean deleteMenu(String code) {
        List<MenuModel> list = menuMapper.listMenu();
        List<MenuTreeDTO> allNodes = createMenuTree(list);
        List<String> codeList = allNodes.stream()
                .filter(item -> item.getCodes().indexOf(code) > -1)
                .map(item -> item.getCode()).collect(Collectors.toList());
        if (codeList.size()==0 || DaoUtil.isDeleteFail(menuMapper.deleteMenu(codeList))) {
            throw new BusinessException("删除失败");
        }
        return true;
    }

    private Map<String, Object> getMenuResult(List<MenuModel> list) {
        List<MenuTreeDTO> allNodes = createMenuTree(list);
        Map<String, Object> map = new HashMap<>();
        map.put("tree", allNodes.stream()
                .filter(item -> StringUtils.isEmpty(item.getParentCode()))
                .collect(Collectors.toList()));
        map.put("folders", allNodes.stream()
                .filter(item -> item.getType().equals((byte) 1))
                .collect(Collectors.toList()));
        map.put("routes", allNodes.stream()
                .filter(item -> item.getType().equals((byte) 2))
                .collect(Collectors.toList()));
        return map;
    }

    private List<MenuTreeDTO> createMenuTree(List<MenuModel> list) {
        List<MenuTreeDTO> allNodes = new ArrayList<>();
        MenuTreeDTO dto;
        for (MenuModel model : list) {
            dto = new MenuTreeDTO();
            BeanUtils.copyProperties(model, dto);
            allNodes.add(dto);
        }
        for (MenuTreeDTO treeDTO : allNodes) {
            treeDTO.setChildren(allNodes.stream()
                    .filter(item -> treeDTO.getCode().equals(item.getParentCode()))
                    .collect(Collectors.toList()));
        }
        createPaths(allNodes.stream()
                        .filter(item -> StringUtils.isEmpty(item.getParentCode()))
                        .collect(Collectors.toList()),
                new ArrayList<>(),
                new ArrayList<>());
        return allNodes;
    }

    private void createPaths(List<MenuTreeDTO> nodes, List<String> codes, List<String> names) {
        for (MenuTreeDTO dto : nodes) {
            codes.add(dto.getCode());
            names.add(dto.getName());
            dto.setCodes(codes.stream().collect(Collectors.toList()));
            dto.setNames(names.stream().collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(dto.getChildren())) {
                createPaths(dto.getChildren(),
                        codes.stream().collect(Collectors.toList()),
                        names.stream().collect(Collectors.toList()));
            }
            codes.remove(codes.size() - 1);
            names.remove(names.size() - 1);
        }
    }
}
