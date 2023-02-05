package com.system.file.mapper;

import com.system.file.domain.model.FileModel;
import com.system.file.domain.qo.file.FileQO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IFileMapper {
    List<FileModel> listFile(FileQO fileQO);

    int insertFile(FileModel fileModel);

    FileModel selectById(@Param("id") Long id);

    int deleteFile(@Param("id") Long id);
}
