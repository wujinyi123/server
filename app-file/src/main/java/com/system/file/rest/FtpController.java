package com.system.file.rest;

import com.github.pagehelper.PageInfo;
import com.system.base.exception.BusinessException;
import com.system.file.domain.dto.ftp.FileDTO;
import com.system.file.domain.model.FileModel;
import com.system.file.domain.model.FolderModel;
import com.system.file.domain.qo.file.FileQO;
import com.system.file.domain.qo.folder.FolderQO;
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

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

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
        FileDTO dto = ftpService.download(id);
        File file = dto.getFile();
        // 清空response
        response.reset();
        // 设置response的Header
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(dto.getRealName(), "UTF-8"));
        } catch (Exception e) {
            throw new BusinessException("文件下载失败");
        }
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        byte[] buffer;
        //InputStream -> byte[]
        try (InputStream fis = new BufferedInputStream(new FileInputStream(file))) {
            buffer = new byte[fis.available()];
            fis.read(buffer);
        } catch (Exception e) {
            throw new BusinessException("文件下载失败");
        }
        //byte[] -> OutputStream
        try (OutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            toClient.write(buffer);
            toClient.flush();
        } catch (Exception e) {
            throw new BusinessException("文件下载失败");
        }
    }

    @DeleteMapping("/ftp/file/{id}")
    public Boolean deleteFile(@PathVariable Long id) {
        return ftpService.deleteFile(id);
    }
}
