package com.system.file.rest;

import com.system.file.domain.dto.template.ExportDTO;
import com.system.file.domain.dto.template.TemplateDTO;
import com.system.file.domain.model.TemplateTaskModel;
import com.system.file.service.ITemplateService;
import com.system.file.util.TemplateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RestController
public class TemplateController {
    @Autowired
    private ITemplateService templateService;

    @GetMapping("/template")
    public TemplateDTO queryById(@RequestParam String id) {
        return TemplateUtil.getExportTemplate(id);
    }

    @PostMapping("/template/export")
    public TemplateTaskModel export(HttpServletRequest request,
                                    @RequestBody ExportDTO dto) {
        return templateService.export(getHttpHeaders(request), dto);
    }

    private HttpHeaders getHttpHeaders(HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        //header
        Enumeration<String> headerNames = request.getHeaderNames();
        String key;
        while (headerNames.hasMoreElements()) {
            key = headerNames.nextElement();
            if (StringUtils.isNotEmpty(key)) {
                httpHeaders.add(key, request.getHeader(key));
            }
        }
        //cookie
        List<String> cookieList = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                cookieList.add(cookie.getName() + "=" + cookie.getValue());
            }
        }
        httpHeaders.put(HttpHeaders.COOKIE, cookieList);
        return httpHeaders;
    }
}
