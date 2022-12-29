package com.system.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.system.base.component.IUserSession;
import com.system.base.domain.CurrentUser;
import com.system.base.util.DaoUtil;
import com.system.base.util.SnowflakeIdUtil;
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
    @Autowired
    private IUserSession userSession;
    @Autowired
    private IUserMapper IUserMapper;
    @Autowired
    private ILogMapper ILogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CurrentUser login(HttpServletRequest request, UserQO userQO) {
        if (StringUtils.isAnyEmpty(userQO.getUsername(), userQO.getPassword())) {
            throw new RuntimeException("账号密码不能为空");
        }
        UserModel model = IUserMapper.login(userQO.getUsername(), userQO.getPassword());
        if (Objects.isNull(model)) {
            throw new RuntimeException("账号或密码错误");
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
        logModel.setLogInfo("用户登录");
        if (DaoUtil.isInsertFail(ILogMapper.insertLog(logModel))) {
            throw new RuntimeException("记录日志异常");
        }
        return currentUser;
    }

    @Override
    public CurrentUser getCurrentUser(HttpServletRequest request) {
        return userSession.getAttibute(request);
    }
}
