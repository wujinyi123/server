package com.system.common.service;

import com.github.pagehelper.PageInfo;
import com.system.common.pojo.log.LogQO;
import com.system.common.model.LogModel;

public interface ILogService {
    PageInfo<LogModel> pageLog(LogQO logQO);
}
