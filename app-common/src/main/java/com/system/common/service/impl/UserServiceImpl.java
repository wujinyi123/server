package com.system.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.system.base.component.IUserSession;
import com.system.base.domain.CurrentUser;
import com.system.base.exception.BusinessException;
import com.system.base.util.DaoUtil;
import com.system.base.util.SnowflakeIdUtil;
import com.system.common.domain.qo.user.UserQO;
import com.system.common.mapper.IRoleMapper;
import com.system.common.domain.model.RoleModel;
import com.system.common.domain.dto.user.UpdatePasswordDTO;
import com.system.common.domain.dto.user.UserDTO;
import com.system.common.domain.qo.user.LoginQO;
import com.system.common.mapper.ILogMapper;
import com.system.common.mapper.IUserMapper;
import com.system.common.domain.model.LogModel;
import com.system.common.domain.model.UserModel;
import com.system.common.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    private static final String UPDATE_FORMAT = "%s：%s";

    @Autowired
    private IUserSession userSession;
    @Autowired
    private IUserMapper userMapper;
    @Autowired
    private ILogMapper logMapper;
    @Autowired
    private IRoleMapper roleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CurrentUser login(LoginQO loginQO) {
        if (StringUtils.isAnyEmpty(loginQO.getUsername(), loginQO.getPassword())) {
            throw new BusinessException("账号密码不能为空");
        }
        UserModel model = userMapper.getUserByPassword(loginQO);
        if (Objects.isNull(model)) {
            throw new BusinessException("账号或密码错误");
        }
        model.setPassword(null);
        if (StringUtils.isNotEmpty(model.getRoleCode())) {
            UserDTO dto = new UserDTO();
            BeanUtils.copyProperties(model, dto);
            List<RoleModel> roles = roleMapper.getByCodes(dto.getRoleCode());
            String roleName = roles.stream().map(item -> item.getName()).distinct().collect(Collectors.joining(","));
            dto.setRoleName(roleName);
            model = dto;
        }
        String json = JSONObject.toJSONString(model);
        Map<String, Object> user = JSONObject.parseObject(json, Map.class);
        CurrentUser currentUser = userSession.setAttibute(user);
        LogModel logModel = new LogModel();
        logModel.setId(SnowflakeIdUtil.getSnowflakeId());
        logModel.setUsername(model.getUsername());
        logModel.setToken(currentUser.getToken());
        logModel.setLogType("login");
        logModel.setLogInfo(JSONObject.toJSONString(currentUser.getUser()));
        if (DaoUtil.isInsertFail(logMapper.insertLog(logModel))) {
            throw new BusinessException("记录日志异常");
        }
        return currentUser;
    }

    @Override
    public CurrentUser getCurrentUser() {
        return userSession.getAttibute();
    }

    @Override
    public Boolean logout() {
        return userSession.removeAttibute();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePassword(UpdatePasswordDTO dto) {
        String username = dto.getUsername();
        String oldPassword = dto.getOldPassword();
        String newPassword = dto.getNewPassword();
        String checkPassword = dto.getCheckPassword();
        if (StringUtils.isAnyEmpty(username, oldPassword, newPassword, checkPassword)) {
            throw new BusinessException("所有参数均不能为空");
        }
        if (!newPassword.equals(checkPassword)) {
            throw new BusinessException("新密码与确认密码不一致");
        }
        if (newPassword.equals(oldPassword)) {
            throw new BusinessException("新旧密码不能一样");
        }
        if (Objects.isNull(userMapper.getUserByPassword(new LoginQO(username, oldPassword)))) {
            throw new BusinessException("旧密码不正确");
        }
        String updateInfo = String.format(UPDATE_FORMAT, username, "修改密码");
        if (DaoUtil.isUpdateFail(userMapper.updatePassword(username, newPassword, updateInfo))) {
            throw new BusinessException("修改密码异常");
        }
        return true;
    }

    @Override
    public PageInfo<UserModel> pageUser(UserQO qo) {
        PageHelper.startPage(qo);
        PageInfo<UserModel> pageInfo = new PageInfo<>(userMapper.listUser(qo));
        if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
            Set<String> roleCodes = new HashSet<>();
            for (UserModel model : pageInfo.getList()) {
                if (StringUtils.isNotEmpty(model.getRoleCode())) {
                    String[] roleCodeArr = model.getRoleCode().split(",");
                    for (String roleCode : roleCodeArr) {
                        roleCodes.add(roleCode);
                    }
                }
            }
            List<RoleModel> listRole = roleCodes.size() > 0 ?
                    roleMapper.getByCodes(roleCodes.stream().collect(Collectors.joining(",")))
                    : new ArrayList<>();
            Map<String, String> mapRole = listRole.stream().collect(Collectors.toMap(
                    RoleModel::getCode,
                    RoleModel::getName,
                    (key1, key2) -> key1
            ));
            List<UserModel> list = new ArrayList<>();
            UserDTO dto;
            String[] roleCodeArr;
            List<String> roleNameList;
            String roleName;
            for (UserModel model : pageInfo.getList()) {
                dto = new UserDTO();
                BeanUtils.copyProperties(model, dto);
                roleNameList = new ArrayList<>();
                if (StringUtils.isNotEmpty(dto.getRoleCode())) {
                    roleCodeArr = model.getRoleCode().split(",");
                    for (String roleCode : roleCodeArr) {
                        roleName = mapRole.get(roleCode);
                        if (StringUtils.isNotEmpty(roleName)) {
                            roleNameList.add(roleName);
                        }
                    }
                }
                dto.setRoleName(roleNameList.stream().collect(Collectors.joining(",")));
                dto.setPassword(null);
                dto.setIsAlive(Objects.nonNull(userSession.getAttibute(dto.getUsername())));
                list.add(dto);
            }
            pageInfo.setList(list);
        }
        return pageInfo;
    }

    @Override
    public Boolean addUser(UserModel model) {
        if (StringUtils.isAnyEmpty(model.getUsername(), model.getName(), model.getSex())) {
            throw new BusinessException("必填项不能为空");
        }
        if (Objects.nonNull(userMapper.getUser(model.getUsername()))) {
            throw new BusinessException("账号已存在");
        }
        CurrentUser currentUser = userSession.getAttibute();
        String updateInfo = String.format(UPDATE_FORMAT, currentUser.getUser().getOrDefault("username", ""), "添加信息");
        model.setUpdateInfo(updateInfo);
        model.setId(SnowflakeIdUtil.getSnowflakeId());
        if (DaoUtil.isInsertFail(userMapper.addUser(model))) {
            throw new BusinessException("添加失败");
        }
        return true;
    }

    @Override
    public Boolean updateUser(UserModel model) {
        if (StringUtils.isAnyEmpty(model.getUsername(), model.getName(), model.getSex())) {
            throw new BusinessException("必填项不能为空");
        }
        if (Objects.isNull(userMapper.getUser(model.getUsername()))) {
            throw new BusinessException("账号不存在");
        }
        CurrentUser currentUser = userSession.getAttibute();
        String updateInfo = String.format(UPDATE_FORMAT, currentUser.getUser().getOrDefault("username", ""), "修改信息");
        model.setUpdateInfo(updateInfo);
        if (DaoUtil.isInsertFail(userMapper.updateUser(model))) {
            throw new BusinessException("修改失败");
        }
        return true;
    }

    @Override
    public Boolean deleteUser(String username) {
        if (DaoUtil.isInsertFail(userMapper.deleteUser(username))) {
            throw new BusinessException("删除失败");
        }
        return true;
    }
}
