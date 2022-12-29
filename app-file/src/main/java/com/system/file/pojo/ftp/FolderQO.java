package com.system.file.pojo.ftp;

import com.system.base.domain.BasePageQueryParam;
import lombok.Data;

@Data
public class FolderQO extends BasePageQueryParam {
    private String folderName;
}
