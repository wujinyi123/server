package com.system.file.domain.dto.ftp;

import com.system.file.domain.model.FileModel;
import lombok.Data;

import java.io.File;

@Data
public class FileDTO extends FileModel {
    private File file;
}
