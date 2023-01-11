package com.system.common.rest;

import com.github.pagehelper.PageInfo;
import com.system.common.pojo.log.LogQO;
import com.system.common.model.LogModel;
import com.system.common.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class LogController {
    @Autowired
    private ILogService logService;

    @GetMapping("/common/log")
    public PageInfo<LogModel> pageLog(HttpServletRequest request, LogQO logQO) {
        return logService.pageLog(logQO);
    }

    @PostMapping("/common/log")
    public Boolean insertLog(@RequestBody LogModel model) {
        return logService.insertLog(model);
    }

    @GetMapping("/common/log/stat")
    public Map<String, Object> logStat(@RequestParam String username) {
        return logService.logStat(username);
    }
}
