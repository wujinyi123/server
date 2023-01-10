package com.system.file.rest;

import com.github.pagehelper.PageInfo;
import com.system.file.model.FileModel;
import com.system.file.model.FolderModel;
import com.system.file.pojo.ftp.FileQO;
import com.system.file.pojo.ftp.FolderQO;
import com.system.file.service.IFtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class FtpController {
    @Autowired
    private IFtpService ftpService;

    @GetMapping("/ftp/folder")
    public PageInfo<FolderModel> pageFolder(FolderQO folderQO) {
        return ftpService.pageFolder(folderQO);
    }

    @PostMapping("/ftp/folder")
    public Boolean insertFolder(@RequestBody FolderModel folderModel) {
        return ftpService.insertFolder(folderModel);
    }

    @PutMapping("/ftp/folder")
    public Boolean updateFolder(@RequestBody FolderModel folderModel) {
        return ftpService.updateFolder(folderModel);
    }

    @DeleteMapping("/ftp/folder/{folderName}")
    public Boolean deleteFolder(@PathVariable String folderName) {
        return ftpService.deleteFolder(folderName);
    }

    @GetMapping("/ftp/file")
    public PageInfo<FileModel> pageFile(FileQO fileQO) {
        return ftpService.pageFile(fileQO);
    }

    @PostMapping("/ftp/file")
    public FileModel upload(@RequestParam MultipartFile file,
                            @RequestParam String folderName,
                            @RequestParam String username) {
        return ftpService.upload(file, folderName, username);
    }

    @GetMapping("/ftp/file/{id}")
    public void download(HttpServletResponse response,
                         @PathVariable Long id) {
        ftpService.download(response, id);
    }

    @DeleteMapping("/ftp/file/{id}")
    public Boolean deleteFile(@PathVariable Long id) {
        return ftpService.deleteFile(id);
    }
}
