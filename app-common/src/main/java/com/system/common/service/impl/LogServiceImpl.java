package com.system.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.system.base.exception.BusinessException;
import com.system.base.util.DaoUtil;
import com.system.base.util.SnowflakeIdUtil;
import com.system.common.domain.qo.log.LogQO;
import com.system.common.mapper.ILogMapper;
import com.system.common.domain.model.LogModel;
import com.system.common.service.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
public class LogServiceImpl implements ILogService {
    @Autowired
    private ILogMapper logMapper;

    @Override
    public PageInfo<LogModel> pageLog(LogQO logQO) {
        PageHelper.startPage(logQO);
        PageInfo<LogModel> pageInfo = new PageInfo<>(logMapper.listLog(logQO));
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertLog(LogModel model) {
        model.setId(SnowflakeIdUtil.getSnowflakeId());
        if (DaoUtil.isInsertFail(logMapper.insertLog(model))) {
            throw new BusinessException("添加日志失败");
        }
        return true;
    }

}
