package com.system.file.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.system.base.component.IUserSession;
import com.system.base.domain.CurrentUser;
import com.system.base.util.DaoUtil;
import com.system.base.util.SnowflakeIdUtil;
import com.system.base.util.ValidatorUtil;
import com.system.file.mapper.IFolderMapper;
import com.system.file.model.FolderModel;
import com.system.file.pojo.ftp.FolderQO;
import com.system.file.service.IFtpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Service
public class FtpServiceImpl implements IFtpService {
    @Autowired
    private IUserSession userSession;
    @Autowired
    private IFolderMapper folderMapper;

    @Override
    public PageInfo<FolderModel> pageFolder(FolderQO folderQO) {
        PageHelper.startPage(folderQO);
        PageInfo<FolderModel> pageInfo = new PageInfo<>(folderMapper.listFolder(folderQO.getFolderName()));
        return pageInfo;
    }

    @Override
    public Boolean insertFolder(HttpServletRequest request, FolderModel folderModel) {
        if (StringUtils.isEmpty(folderModel.getFolderName())) {
            throw new RuntimeException("文件夹名称不能为空");
        }
        if (!ValidatorUtil.isLetterNum(folderModel.getFolderName())) {
            throw new RuntimeException("文件夹名称仅由字母、数字组成");
        }
        if (Objects.nonNull(folderMapper.selectByFolderName(folderModel.getFolderName()))) {
            throw new RuntimeException("文件夹已存在");
        }
        folderModel.setId(SnowflakeIdUtil.getSnowflakeId());
        CurrentUser currentUser = userSession.getAttibute(request);
        if (Objects.nonNull(currentUser) && Objects.nonNull(currentUser.getUser())) {
            folderModel.setUsername((String) currentUser.getUser().get("username"));
        }
        if (DaoUtil.isInsertFail(folderMapper.insertFolder(folderModel))) {
            throw new RuntimeException("添加异常");
        }
        return true;
    }

    @Override
    public Boolean updateFolder(HttpServletRequest request, FolderModel folderModel) {
        if (StringUtils.isEmpty(folderModel.getFolderName())) {
            throw new RuntimeException("文件夹名称不能为空");
        }
        CurrentUser currentUser = userSession.getAttibute(request);
        if (Objects.nonNull(currentUser) && Objects.nonNull(currentUser.getUser())) {
            folderModel.setUsername((String) currentUser.getUser().get("username"));
        }
        if (DaoUtil.isInsertFail(folderMapper.updateFolder(folderModel))) {
            throw new RuntimeException("修改异常");
        }
        return true;
    }

    @Override
    public Boolean deleteFolder(String folderName) {
        if (DaoUtil.isInsertFail(folderMapper.deleteFolder(folderName))) {
            throw new RuntimeException("删除异常");
        }
        return true;
    }
}
