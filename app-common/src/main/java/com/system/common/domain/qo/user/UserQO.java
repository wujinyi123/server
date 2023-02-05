package com.system.common.domain.qo.user;

import com.system.base.domain.BasePageQueryParam;
import lombok.Data;

@Data
public class UserQO extends BasePageQueryParam {
    private String username;
}
