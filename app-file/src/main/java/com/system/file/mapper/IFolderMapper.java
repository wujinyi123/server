package com.system.file.mapper;

import com.system.file.domain.model.FolderModel;
import com.system.file.domain.qo.folder.FolderQO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IFolderMapper {
    List<FolderModel> listFolder(FolderQO folderQO);

    FolderModel selectByFolderName(@Param("folderName") String folderName);

    int insertFolder(FolderModel folderModel);

    int updateFolder(FolderModel folderModel);

    int deleteFolder(@Param("folderName") String folderName);
}
