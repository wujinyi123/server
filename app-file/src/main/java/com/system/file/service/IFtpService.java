package com.system.file.service;

import com.github.pagehelper.PageInfo;
import com.system.file.model.FileModel;
import com.system.file.model.FolderModel;
import com.system.file.pojo.ftp.FileQO;
import com.system.file.pojo.ftp.FolderQO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IFtpService {
    PageInfo<FolderModel> pageFolder(FolderQO folderQO);

    Boolean insertFolder(FolderModel folderModel);

    Boolean updateFolder(FolderModel folderModel);

    Boolean deleteFolder(String folderName);

    PageInfo<FileModel> pageFile(FileQO fileQO);

    FileModel upload(MultipartFile file, String folderName, String username);

    void download(HttpServletResponse response, Long id);

    Boolean deleteFile(Long id);
}
