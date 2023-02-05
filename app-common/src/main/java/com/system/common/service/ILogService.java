package com.system.common.service;

import com.github.pagehelper.PageInfo;
import com.system.common.domain.qo.log.LogQO;
import com.system.common.domain.model.LogModel;

import java.util.Map;

public interface ILogService {
    PageInfo<LogModel> pageLog(LogQO logQO);

    Boolean insertLog(LogModel model);

    Map<String, Object> logStat(String username);
}
