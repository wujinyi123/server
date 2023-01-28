package com.system.base.component;

public interface IRedisService {
    void setByHOURS(String key, String value, long hours);

    void setByMINUTES(String key, String value, long minutes);

    void setBySECONDS(String key, String value, long seconds);

    void removeKeyValue(String key);

    String getValueByKey(String key);
}
