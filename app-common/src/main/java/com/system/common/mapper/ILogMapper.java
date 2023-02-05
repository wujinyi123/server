package com.system.common.mapper;

import com.system.common.domain.model.LogModel;
import com.system.common.domain.qo.log.LogQO;

import java.util.List;

public interface ILogMapper {
    List<LogModel> listLog(LogQO logQO);

    int insertLog(LogModel logModel);
}
