package com.system.common.rest;

import com.github.pagehelper.PageInfo;
import com.system.common.pojo.log.LogQO;
import com.system.common.model.LogModel;
import com.system.common.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LogController {
    @Autowired
    private ILogService logService;

    @GetMapping("/common/log")
    public PageInfo<LogModel> pageLog(HttpServletRequest request, LogQO logQO) {
        return logService.pageLog(logQO);
    }
}
