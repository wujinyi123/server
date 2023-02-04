package com.system.xiumei.rest;

import com.system.base.domain.BasePageQueryParam;
import com.system.base.inter.BackgroundTask;
import com.system.xiumei.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class DemoController {
    @Autowired
    private IDemoService demoService;

    @BackgroundTask
    @GetMapping("/rest/test")
    public Object listTest(@RequestParam int second) throws Exception {
        for (int i = 0; i < second; i++) {
            Thread.sleep(1000);
        }
        return Arrays.asList("test", "demo");
    }

    @BackgroundTask
    @GetMapping("/rest/demo")
    public Object pageDemo(BasePageQueryParam pageQueryParam) throws InterruptedException {
        for (int i = 0; i < pageQueryParam.getPageSize(); i++) {
            Thread.sleep(1000);
        }
        return demoService.pageDemo(pageQueryParam);
    }
}
