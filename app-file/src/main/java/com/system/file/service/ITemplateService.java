package com.system.file.service;

import com.system.file.domain.dto.template.ExportDTO;
import com.system.file.domain.model.TemplateTaskModel;
import org.springframework.http.HttpHeaders;

import java.io.File;

public interface ITemplateService {
    TemplateTaskModel export(HttpHeaders httpHeaders, ExportDTO dto);

    File export(String fileName);
}
