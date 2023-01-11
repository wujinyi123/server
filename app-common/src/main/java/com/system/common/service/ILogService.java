package com.system.common.service;

import com.github.pagehelper.PageInfo;
import com.system.common.pojo.log.LogQO;
import com.system.common.model.LogModel;

import java.util.Map;

public interface ILogService {
    PageInfo<LogModel> pageLog(LogQO logQO);

    Boolean insertLog(LogModel model);

    Map<String, Object> logStat(String username);
}
