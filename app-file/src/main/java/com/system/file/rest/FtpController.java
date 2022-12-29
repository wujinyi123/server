package com.system.file.rest;

import com.github.pagehelper.PageInfo;
import com.system.file.model.FolderModel;
import com.system.file.pojo.ftp.FolderQO;
import com.system.file.service.IFtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FtpController {
    @Autowired
    private IFtpService ftpService;

    @GetMapping("/ftp/folder")
    public PageInfo<FolderModel> pageFolder(FolderQO folderQO) {
        return ftpService.pageFolder(folderQO);
    }

    @PostMapping("/ftp/folder")
    public Boolean insertFolder(HttpServletRequest request,
                                @RequestBody FolderModel folderModel) {
        return ftpService.insertFolder(request, folderModel);
    }

    @PutMapping("/ftp/folder")
    public Boolean updateFolder(HttpServletRequest request,
                                @RequestBody FolderModel folderModel) {
        return ftpService.updateFolder(request, folderModel);
    }

    @DeleteMapping("/ftp/folder/{folderName}")
    public Boolean deleteFolder(@PathVariable String folderName) {
        return ftpService.deleteFolder(folderName);
    }
}
