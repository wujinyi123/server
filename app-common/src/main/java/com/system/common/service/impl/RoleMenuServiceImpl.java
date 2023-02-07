package com.system.common.service.impl;

import com.system.base.exception.BusinessException;
import com.system.base.util.DaoUtil;
import com.system.base.util.SnowflakeIdUtil;
import com.system.common.mapper.IRoleMapper;
import com.system.common.mapper.IRoleMenuMapper;
import com.system.common.domain.model.RoleMenuModel;
import com.system.common.service.IRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RoleMenuServiceImpl implements IRoleMenuService {
    @Autowired
    private IRoleMenuMapper roleMenuMapper;
    @Autowired
    private IRoleMapper roleMapper;

    @Override
    public List<RoleMenuModel> listByRoleCode(String roleCode) {
        return roleMenuMapper.listByRoleCode(roleCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRoleMenu(String roleCode, List<String> menuCodes) {
        if (Objects.isNull(roleMapper.getByCode(roleCode))) {
            throw new BusinessException("角色不存在");
        }
        roleMenuMapper.deleteByRoleCode(roleCode);
        if (CollectionUtils.isEmpty(menuCodes)) {
            return true;
        }
        List<RoleMenuModel> list = new ArrayList<>();
        RoleMenuModel model;
        for (String menuCode : menuCodes) {
            model = new RoleMenuModel();
            model.setId(SnowflakeIdUtil.getSnowflakeId());
            model.setRoleCode(roleCode);
            model.setMenuCode(menuCode);
            list.add(model);
        }
        if (DaoUtil.isInsertFail(roleMenuMapper.insertRoleMenu(list))) {
            throw new BusinessException("保存失败");
        }
        return true;
    }
}
