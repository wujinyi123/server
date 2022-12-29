package com.system.base.domain;

import com.github.pagehelper.IPage;
import lombok.Data;

@Data
public class BasePageQueryParam implements IPage {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String orderBy;
}
