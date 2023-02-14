package com.system.file.domain.dto.template;

import lombok.Data;

import java.util.Map;

@Data
public class ExportDTO {
    private String templateId;
    private Map<String, Object> params;
}
