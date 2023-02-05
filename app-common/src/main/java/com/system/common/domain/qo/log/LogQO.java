package com.system.common.domain.qo.log;

import com.system.base.domain.BasePageQueryParam;
import lombok.Data;

@Data
public class LogQO extends BasePageQueryParam {
    private String username;
    private String logType;
    private Long queryBegin;
    private Long queryEnd;
}
