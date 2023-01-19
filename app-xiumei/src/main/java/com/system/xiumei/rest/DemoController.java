package com.system.xiumei.rest;

import com.github.pagehelper.PageInfo;
import com.system.base.domain.BasePageQueryParam;
import com.system.xiumei.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @Autowired
    private IDemoService demoService;

    @GetMapping("/rest/demo")
    public PageInfo pageDemo(BasePageQueryParam pageQueryParam) {
        return demoService.pageDemo(pageQueryParam);
    }
}
