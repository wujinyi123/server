package com.system.base.component.impl;

import com.system.base.component.IRedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisServiceImpl implements IRedisService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void setByHOURS(String key, String value, long hours) {
        redisTemplate.opsForValue().set(key, value, hours, TimeUnit.HOURS);
    }

    @Override
    public void setByMINUTES(String key, String value, long minutes) {
        redisTemplate.opsForValue().set(key, value, minutes, TimeUnit.MINUTES);
    }

    @Override
    public void setBySECONDS(String key, String value, long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void removeKeyValue(String key) {
        setBySECONDS(key,null,1);
    }

    @Override
    public String getValueByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
