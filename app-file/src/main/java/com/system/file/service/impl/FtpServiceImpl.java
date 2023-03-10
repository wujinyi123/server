package com.system.file.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.system.base.exception.BusinessException;
import com.system.base.util.DaoUtil;
import com.system.base.util.SnowflakeIdUtil;
import com.system.base.util.SystemChooseUtil;
import com.system.base.util.ValidatorUtil;
import com.system.file.domain.dto.ftp.FileDTO;
import com.system.file.mapper.IFileMapper;
import com.system.file.mapper.IFolderMapper;
import com.system.file.domain.model.FileModel;
import com.system.file.domain.model.FolderModel;
import com.system.file.domain.qo.file.FileQO;
import com.system.file.domain.qo.folder.FolderQO;
import com.system.file.service.IFtpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class FtpServiceImpl implements IFtpService {
    private static long MAXSIZE = new Integer(10 * 1024 * 1024).longValue();
    private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    @Value("${file.ftp.win}")
    private String ftpForWin;
    @Value("${file.ftp.linux}")
    private String ftpForLinux;

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
            throw new BusinessException("???????????????????????????");
        }
        if (!ValidatorUtil.isLetterNum(folderModel.getFolderName())) {
            throw new BusinessException("??????????????????????????????????????????");
        }
        if (Objects.nonNull(folderMapper.selectByFolderName(folderModel.getFolderName()))) {
            throw new BusinessException("??????????????????");
        }
        folderModel.setId(SnowflakeIdUtil.getSnowflakeId());
        if (DaoUtil.isInsertFail(folderMapper.insertFolder(folderModel))) {
            throw new BusinessException("????????????");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateFolder(FolderModel folderModel) {
        if (StringUtils.isEmpty(folderModel.getFolderName())) {
            throw new BusinessException("???????????????????????????");
        }
        if (DaoUtil.isInsertFail(folderMapper.updateFolder(folderModel))) {
            throw new BusinessException("????????????");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFolder(String folderName) {
        if (DaoUtil.isInsertFail(folderMapper.deleteFolder(folderName))) {
            throw new BusinessException("????????????");
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
            throw new BusinessException("????????????");
        }
        if (Objects.isNull(folderMapper.selectByFolderName(folderName))) {
            throw new BusinessException("??????????????????");
        }
        String ftpRoot = SystemChooseUtil.choose(ftpForWin, ftpForLinux);
        String realName = file.getOriginalFilename();
        String[] arr = realName.split("\\.");
        String fileType = arr[arr.length - 1];
        FileModel model = new FileModel();
        model.setFolderName(folderName);
        model.setFileName(DATE_FORMAT.format(new Date()) + "." + fileType);
        model.setRealName(realName);
        model.setFileSize(file.getSize());
        model.setUsername(username);
        //???????????????
        File folderFile = new File(ftpRoot + "/" + folderName);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        //???????????????
        File uploadFile = new File(ftpRoot + "/" + folderName + "/" + model.getFileName());
        if (uploadFile.exists()) {
            uploadFile.delete();
        }
        //??????
        try {
            file.transferTo(uploadFile);
        } catch (Exception e) {
            if (uploadFile.exists()) {
                uploadFile.delete();
            }
            log.error("?????????????????????", e);
            throw new BusinessException("??????????????????");
        }
        model.setId(SnowflakeIdUtil.getSnowflakeId());
        if (DaoUtil.isInsertFail(fileMapper.insertFile(model))) {
            throw new BusinessException("?????????????????????");
        }
        return model;
    }

    @Override
    public FileDTO download(Long id) {
        FileModel model = fileMapper.selectById(id);
        if (Objects.isNull(model)) {
            throw new BusinessException("??????id??????");
        }
        String ftpRoot = SystemChooseUtil.choose(ftpForWin, ftpForLinux);
        File file = new File(ftpRoot + "/" + model.getFolderName() + "/" + model.getFileName());
        if (!file.exists()) {
            throw new BusinessException("???????????????");
        }
        FileDTO dto = new FileDTO();
        BeanUtils.copyProperties(model, dto);
        dto.setFile(file);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFile(Long id) {
        FileModel model = fileMapper.selectById(id);
        if (Objects.isNull(model)) {
            throw new BusinessException("??????id??????");
        }
        String ftpRoot = SystemChooseUtil.choose(ftpForWin, ftpForLinux);
        File file = new File(ftpRoot + "/" + model.getFolderName() + "/" + model.getFileName());
        if (file.exists()) {
            file.delete();
        }
        if (DaoUtil.isDeleteFail(fileMapper.deleteFile(id))) {
            throw new BusinessException("?????????????????????");
        }
        return true;
    }
}
