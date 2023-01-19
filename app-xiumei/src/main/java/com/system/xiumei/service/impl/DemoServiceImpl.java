package com.system.xiumei.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.system.base.domain.BasePageQueryParam;
import com.system.xiumei.mapper.IDemoMapper;
import com.system.xiumei.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements IDemoService {
    @Autowired
    private IDemoMapper demoMapper;

    @Override
    public PageInfo pageDemo(BasePageQueryParam pageQueryParam) {
        PageHelper.startPage(pageQueryParam);
        return new PageInfo<>(demoMapper.listDemo());
    }
}
