package com.system.file.service;

import com.github.pagehelper.PageInfo;
import com.system.file.domain.model.FileModel;
import com.system.file.domain.model.FolderModel;
import com.system.file.domain.qo.file.FileQO;
import com.system.file.domain.qo.folder.FolderQO;
import org.springframework.web.multipart.MultipartFile;

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
