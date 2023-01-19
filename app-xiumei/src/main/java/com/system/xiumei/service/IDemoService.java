package com.system.xiumei.service;

import com.github.pagehelper.PageInfo;
import com.system.base.domain.BasePageQueryParam;

public interface IDemoService {
    PageInfo pageDemo(BasePageQueryParam pageQueryParam);
}
