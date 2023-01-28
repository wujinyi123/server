package com.system.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.system.base.component.IUserSession;
import com.system.base.domain.CurrentUser;
import com.system.base.exception.BusinessException;
import com.system.base.util.DaoUtil;
import com.system.base.util.SnowflakeIdUtil;
import com.system.common.pojo.user.UpdatePasswordDTO;
import com.system.common.pojo.user.UserQO;
import com.system.common.mapper.ILogMapper;
import com.system.common.mapper.IUserMapper;
import com.system.common.model.LogModel;
import com.system.common.model.UserModel;
import com.system.common.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CurrentUser login(HttpServletRequest request, UserQO userQO) {
        if (StringUtils.isAnyEmpty(userQO.getUsername(), userQO.getPassword())) {
            throw new BusinessException("账号密码不能为空");
        }
        UserModel model = userMapper.getUserByPassword(userQO.getUsername(), userQO.getPassword());
        if (Objects.isNull(model)) {
            throw new BusinessException("账号或密码错误");
        }
        model.setPassword(null);
        String json = JSONObject.toJSONString(model);
        Map<String, Object> user = JSONObject.parseObject(json, Map.class);
        CurrentUser currentUser = userSession.setAttibute(request, user);
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
    public CurrentUser getCurrentUser(HttpServletRequest request) {
        return userSession.getAttibute(request);
    }

    @Override
    public Boolean logout(HttpServletRequest request) {
        return userSession.removeAttibute(request);
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
        if (Objects.isNull(userMapper.getUserByPassword(username, oldPassword))) {
            throw new BusinessException("旧密码不正确");
        }
        String updateInfo = String.format(UPDATE_FORMAT, username, "修改密码");
        if (DaoUtil.isUpdateFail(userMapper.updatePassword(username, newPassword, updateInfo))) {
            throw new BusinessException("修改密码异常");
        }
        return true;
    }
}
