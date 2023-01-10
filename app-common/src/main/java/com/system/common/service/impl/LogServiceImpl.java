package com.system.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.system.base.util.DaoUtil;
import com.system.base.util.SnowflakeIdUtil;
import com.system.common.pojo.log.LogQO;
import com.system.common.mapper.ILogMapper;
import com.system.common.model.LogModel;
import com.system.common.service.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogServiceImpl implements ILogService {
    @Autowired
    private ILogMapper logMapper;

    @Override
    public PageInfo<LogModel> pageLog(LogQO logQO) {
        PageHelper.startPage(logQO);
        PageInfo<LogModel> pageInfo = new PageInfo<>(logMapper.listLog(logQO.getUsername(), logQO.getLogType(), logQO.getQueryBegin(), logQO.getQueryEnd()));
        return pageInfo;
    }

    @Override
    public Boolean insertLog(LogModel model) {
        model.setId(SnowflakeIdUtil.getSnowflakeId());
        if (DaoUtil.isInsertFail(logMapper.insertLog(model))) {
            throw new RuntimeException("添加日志失败");
        }
        return true;
    }
}
