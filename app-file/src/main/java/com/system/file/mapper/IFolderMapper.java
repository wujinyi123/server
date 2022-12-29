package com.system.file.mapper;

import com.system.file.model.FolderModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IFolderMapper {
    List<FolderModel> listFolder(@Param("folderName") String folderName);

    FolderModel selectByFolderName(@Param("folderName") String folderName);

    int insertFolder(FolderModel folderModel);

    int updateFolder(FolderModel folderModel);

    int deleteFolder(@Param("folderName") String folderName);
}
