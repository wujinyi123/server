package com.system.file.mapper;

import com.system.file.model.FileModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IFileMapper {
    List<FileModel> listFile(@Param("folderName") String folderName,
                             @Param("fileName") String fileName,
                             @Param("realName") String realName);

    int insertFile(FileModel fileModel);

    FileModel selectById(@Param("id") Long id);

    int deleteFile(@Param("id") Long id);
}
