package com.system.file.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.system.base.exception.BusinessException;
import com.system.base.util.DaoUtil;
import com.system.base.util.SnowflakeIdUtil;
import com.system.base.util.SystemChooseUtil;
import com.system.base.util.ValidatorUtil;
import com.system.file.mapper.IFileMapper;
import com.system.file.mapper.IFolderMapper;
import com.system.file.domain.model.FileModel;
import com.system.file.domain.model.FolderModel;
import com.system.file.domain.qo.file.FileQO;
import com.system.file.domain.qo.folder.FolderQO;
import com.system.file.service.IFtpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class FtpServiceImpl implements IFtpService {
    private static long MAXSIZE = new Integer(10 * 1024 * 1024).longValue();
    private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    @Value("${file.root.win:G:/fileSystem}")
    private String rootForWin;
    @Value("${file.root.linux:/data/fileSystem}")
    private String rootForLinux;

    @Autowired
    private IFolderMapper folderMapper;
    @Autowired
    private IFileMapper fileMapper;

    @Override
    public PageInfo<FolderModel> pageFolder(FolderQO folderQO) {
        PageHelper.startPage(folderQO);
        PageInfo<FolderModel> pageInfo = new PageInfo<>(folderMapper.listFolder(folderQO));
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertFolder(FolderModel folderModel) {
        if (StringUtils.isEmpty(folderModel.getFolderName())) {
            throw new BusinessException("文件夹名称不能为空");
        }
        if (!ValidatorUtil.isLetterNum(folderModel.getFolderName())) {
            throw new BusinessException("文件夹名称仅由字母、数字组成");
        }
        if (Objects.nonNull(folderMapper.selectByFolderName(folderModel.getFolderName()))) {
            throw new BusinessException("文件夹已存在");
        }
        folderModel.setId(SnowflakeIdUtil.getSnowflakeId());
        if (DaoUtil.isInsertFail(folderMapper.insertFolder(folderModel))) {
            throw new BusinessException("添加异常");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateFolder(FolderModel folderModel) {
        if (StringUtils.isEmpty(folderModel.getFolderName())) {
            throw new BusinessException("文件夹名称不能为空");
        }
        if (DaoUtil.isInsertFail(folderMapper.updateFolder(folderModel))) {
            throw new BusinessException("修改异常");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFolder(String folderName) {
        if (DaoUtil.isInsertFail(folderMapper.deleteFolder(folderName))) {
            throw new BusinessException("删除异常");
        }
        return true;
    }

    @Override
    public PageInfo<FileModel> pageFile(FileQO fileQO) {
        PageHelper.startPage(fileQO);
        PageInfo<FileModel> pageInfo = new PageInfo<>(fileMapper.listFile(fileQO));
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileModel upload(MultipartFile file, String folderName, String username) {
        if (file.getSize() > MAXSIZE) {
            throw new BusinessException("文件过大");
        }
        if (Objects.isNull(folderMapper.selectByFolderName(folderName))) {
            throw new BusinessException("文件夹不存在");
        }
        String root = SystemChooseUtil.choose(rootForWin, rootForLinux);
        String realName = file.getOriginalFilename();
        String[] arr = realName.split("\\.");
        String fileType = arr[arr.length - 1];
        FileModel model = new FileModel();
        model.setFolderName(folderName);
        model.setFileName(DATE_FORMAT.format(new Date()) + "." + fileType);
        model.setRealName(realName);
        model.setFileSize(file.getSize());
        model.setUsername(username);
        //所在文件夹
        File folderFile = new File(root + "/" + folderName);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        //上传的文件
        File uploadFile = new File(root + "/" + folderName + "/" + model.getFileName());
        if (uploadFile.exists()) {
            uploadFile.delete();
        }
        //上传
        try {
            file.transferTo(uploadFile);
        } catch (Exception e) {
            if (uploadFile.exists()) {
                uploadFile.delete();
            }
            log.error("文件上传失败：", e);
            throw new BusinessException("文件上传失败");
        }
        model.setId(SnowflakeIdUtil.getSnowflakeId());
        if (DaoUtil.isInsertFail(fileMapper.insertFile(model))) {
            throw new BusinessException("更新数据库失败");
        }
        return model;
    }

    @Override
    public void download(HttpServletResponse response, Long id) {
        FileModel model = fileMapper.selectById(id);
        if (Objects.isNull(model)) {
            throw new BusinessException("文件id错误");
        }
        String root = SystemChooseUtil.choose(rootForWin, rootForLinux);
        File file = new File(root + "/" + model.getFolderName() + "/" + model.getFileName());
        if (!file.exists()) {
            throw new BusinessException("文件不存在");
        }
        // 清空response
        response.reset();
        // 设置response的Header
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(model.getRealName(), "UTF-8"));
        } catch (Exception e) {
            log.error("文件下载失败：", e);
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
            log.error("文件下载失败：", e);
            throw new BusinessException("文件下载失败");
        }
        //byte[] -> OutputStream
        try (OutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            toClient.write(buffer);
            toClient.flush();
        } catch (Exception e) {
            log.error("文件下载失败：", e);
            throw new BusinessException("文件下载失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFile(Long id) {
        FileModel model = fileMapper.selectById(id);
        if (Objects.isNull(model)) {
            throw new BusinessException("文件id错误");
        }
        String root = SystemChooseUtil.choose(rootForWin, rootForLinux);
        File file = new File(root + "/" + model.getFolderName() + "/" + model.getFileName());
        if (file.exists()) {
            file.delete();
        }
        if (DaoUtil.isDeleteFail(fileMapper.deleteFile(id))) {
            throw new BusinessException("更新数据库失败");
        }
        return true;
    }
}
