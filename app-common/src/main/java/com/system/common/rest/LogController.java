package com.system.common.rest;

import com.github.pagehelper.PageInfo;
import com.system.common.domain.qo.log.LogQO;
import com.system.common.domain.model.LogModel;
import com.system.common.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {
    @Autowired
    private ILogService logService;

    @GetMapping("/common/log")
    public PageInfo<LogModel> pageLog(LogQO logQO) {
        return logService.pageLog(logQO);
    }

    @PostMapping("/common/log")
    public Boolean insertLog(@RequestBody LogModel model) {
        return logService.insertLog(model);
    }

}
