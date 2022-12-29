package com.system.common.mapper;

import com.system.common.model.LogModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ILogMapper {
    List<LogModel> listLog(@Param("username") String username,
                           @Param("logType") String logType,
                           @Param("queryBegin") Long queryBegin,
                           @Param("queryEnd") Long queryEnd);

    int insertLog(LogModel logModel);
}
