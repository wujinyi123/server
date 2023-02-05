package com.system.base.component.impl;

import com.alibaba.fastjson.JSONObject;
import com.system.base.component.IRedisService;
import com.system.base.component.IUserSession;
import com.system.base.domain.CurrentUser;
import com.system.base.exception.NoLoginRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class UserSessionImpl implements IUserSession {
    private static final String TOKEN = "token";

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IRedisService redisService;

    @Override
    public CurrentUser setAttibute(Map<String, Object> user) {
        String token = UUID.randomUUID().toString().toLowerCase().replace("-", "");
        CurrentUser currentUser = new CurrentUser(token, user);
        redisService.setByMINUTES(token, JSONObject.toJSONString(currentUser), 30L);
        return currentUser;
    }

    @Override
    public CurrentUser getAttibute() {
        String token = getToken();
        String json = redisService.getValueByKey(token);
        CurrentUser currentUser = null;
        try {
            currentUser = JSONObject.parseObject(json, CurrentUser.class);
        } catch (Exception e) {
            log.error("json转化失败");
        }
        if (Objects.isNull(currentUser)) {
            throw new NoLoginRuntimeException("用户未登录或登录已过期");
        }
        return currentUser;
    }

    @Override
    public Boolean removeAttibute() {
        String token = getToken();
        redisService.setBySECONDS(token, null, 1L);
        return true;
    }

    private String getToken() {
        String token = request.getHeader(TOKEN);
        if (StringUtils.isEmpty(token)) {
            throw new NoLoginRuntimeException("请求头token不能空");
        }
        return token;
    }
}
