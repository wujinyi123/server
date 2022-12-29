package com.system.file.service;

import com.github.pagehelper.PageInfo;
import com.system.file.model.FolderModel;
import com.system.file.pojo.ftp.FolderQO;

import javax.servlet.http.HttpServletRequest;

public interface IFtpService {
    PageInfo<FolderModel> pageFolder(FolderQO folderQO);

    Boolean insertFolder(HttpServletRequest request, FolderModel folderModel);

    Boolean updateFolder(HttpServletRequest request, FolderModel folderModel);

    Boolean deleteFolder(String folderName);
}
