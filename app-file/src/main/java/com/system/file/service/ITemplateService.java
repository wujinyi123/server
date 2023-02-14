package com.system.file.service;

import com.system.file.domain.dto.template.ExportDTO;
import com.system.file.domain.model.TemplateTaskModel;
import org.springframework.http.HttpHeaders;

public interface ITemplateService {
    TemplateTaskModel export(HttpHeaders httpHeaders, ExportDTO dto);
}
