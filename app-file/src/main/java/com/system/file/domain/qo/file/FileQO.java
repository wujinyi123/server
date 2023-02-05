package com.system.file.domain.qo.file;

import com.system.base.domain.BasePageQueryParam;
import lombok.Data;

@Data
public class FileQO extends BasePageQueryParam {
    private String folderName;
    private String fileName;
    private String realName;
}
