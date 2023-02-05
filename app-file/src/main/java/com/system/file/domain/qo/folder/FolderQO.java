package com.system.file.domain.qo.folder;

import com.system.base.domain.BasePageQueryParam;
import lombok.Data;

@Data
public class FolderQO extends BasePageQueryParam {
    private String folderName;
}
