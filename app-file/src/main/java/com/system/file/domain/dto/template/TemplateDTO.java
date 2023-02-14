package com.system.file.domain.dto.template;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class TemplateDTO {
    private File pathFile;
    private Integer dateRow;
    private String serverName;
    private String requestUrl;
    private List<String> fields;
}
