package com.system.common.service.impl;

import com.system.base.exception.BusinessException;
import com.system.base.util.DaoUtil;
import com.system.base.util.SnowflakeIdUtil;
import com.system.common.mapper.IRoleMapper;
import com.system.common.model.RoleModel;
import com.system.common.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private IRoleMapper roleMapper;

    @Override
    public List<RoleModel> listRole() {
        return roleMapper.listRole();
    }

    @Override
    public Boolean insertRole(RoleModel model) {
        if (Objects.nonNull(roleMapper.getByCode(model.getCode()))) {
            throw new BusinessException("角色代码已存在");
        }
        model.setId(SnowflakeIdUtil.getSnowflakeId());
        if (DaoUtil.isInsertFail(roleMapper.insertRole(model))) {
            throw new BusinessException("添加失败");
        }
        return true;
    }

    @Override
    public Boolean updateRole(RoleModel model) {
        if (DaoUtil.isUpdateFail(roleMapper.updateRole(model))) {
            throw new BusinessException("修改失败");
        }
        return true;
    }

    @Override
    public Boolean deleteRole(String code) {
        if (DaoUtil.isDeleteFail(roleMapper.deleteRole(code))) {
            throw new BusinessException("删除失败");
        }
        return true;
    }
}
