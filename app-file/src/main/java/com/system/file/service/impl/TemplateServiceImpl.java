package com.system.file.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.system.base.component.IUserSession;
import com.system.base.domain.CurrentUser;
import com.system.base.exception.BusinessException;
import com.system.base.util.SnowflakeIdUtil;
import com.system.base.util.SystemChooseUtil;
import com.system.base.util.ThreadPoolUtil;
import com.system.file.domain.dto.template.ExportDTO;
import com.system.file.domain.dto.template.TemplateDTO;
import com.system.file.domain.model.TemplateTaskModel;
import com.system.file.mapper.ITemplateTaskMapper;
import com.system.file.service.ITemplateService;
import com.system.file.util.TemplateUtil;
import com.system.file.util.ThreadPoolTemplateTaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TemplateServiceImpl implements ITemplateService {

    private static final int PAGE_SIZE = 10;

    @Value("${VITE_PROGRAMMER:}")
    private String programmer;
    @Value("${file.export.win}")
    private String exportForWin;
    @Value("${file.export.linux}")
    private String exportForLinux;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IUserSession userSession;
    @Autowired
    private ITemplateTaskMapper templateTaskMapper;

    @Override
    public TemplateTaskModel export(HttpHeaders httpHeaders, ExportDTO dto) {
        if (StringUtils.isEmpty(dto.getTemplateId())) {
            throw new BusinessException("templateId不能为空");
        }
        CurrentUser currentUser = userSession.getAttibute();
        List<PageInfo> exportData = Collections.synchronizedList(new ArrayList<>());
        TemplateDTO templateDTO = TemplateUtil.getExportTemplate(dto.getTemplateId());
        String url = "http://" + programmer + templateDTO.getServerName() + templateDTO.getRequestUrl();
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
        Map<String, Object> map = Objects.nonNull(dto.getParams()) ? dto.getParams() : new HashMap<>();
        ResponseEntity<PageInfo> responseEntity = restTemplate.exchange(url + getParams(map), HttpMethod.GET, httpEntity, PageInfo.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new BusinessException("请求url失败");
        }
        int total = (int) responseEntity.getBody().getTotal();
        int groups = total / PAGE_SIZE + (total % PAGE_SIZE > 0 ? 1 : 0);
        TemplateTaskModel templateTaskModel = new TemplateTaskModel();
        templateTaskModel.setType((byte) 1);
        templateTaskModel.setStatus("work");
        templateTaskModel.setOperaUser((String) currentUser.getUser().get("username"));
        ThreadPoolTemplateTaskUtil.getInstance().startTask(() -> {
            //获取数据
            int[] taskResult = new int[groups];
            String paramsJson = Objects.nonNull(dto.getParams()) ? JSONObject.toJSONString(dto.getParams()) : "{}";
            for (int i = 0; i < groups; i++) {
                int j = i + 1;
                ThreadPoolUtil.getInstance().startTask(() -> {
                    int pageNum = j;
                    Map<String, Object> params = JSONObject.parseObject(paramsJson, Map.class);
                    params.put("pageNum", pageNum);
                    params.put("pageSize", PAGE_SIZE);
                    ResponseEntity<PageInfo> response = restTemplate.exchange(url + getParams(params), HttpMethod.GET, httpEntity, PageInfo.class);
                    if (response.getStatusCode() != HttpStatus.OK) {
                        taskResult[pageNum - 1] = -1;
                        throw new BusinessException("请求url失败");
                    } else {
                        exportData.add(response.getBody());
                        taskResult[pageNum - 1] = 1;
                    }
                });
            }
            //等待数据完成(最多等待5分钟)
            for (int i = 0; i < 300; i++) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int index = 0;
                while (index < taskResult.length && taskResult[index] != 0) {
                    index++;
                }
                if (index == taskResult.length) {
                    break;
                }
            }
            templateTaskModel.setTaskResult(Arrays.toString(taskResult));
            exportData.sort((a, b) -> a.getPageNum() - b.getPageNum());
            List<Map<String, Object>> data = new ArrayList<>();
            for (PageInfo pageInfo : exportData) {
                data.addAll(pageInfo.getList());
            }
            //写入导入数据
            String exportRoot = SystemChooseUtil.choose(exportForWin, exportForLinux);
            String exportName = templateDTO.getPathFile().getName().replace(".xlsx", "_" + System.currentTimeMillis() + ".xlsx");
            File exportFile = new File(exportRoot + "/" + exportName);
            try (
                    ExcelWriter excelWriter = EasyExcel.write(exportFile).withTemplate(templateDTO.getPathFile()).build()
            ) {
                WriteSheet writeSheet = EasyExcel.writerSheet().build();
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.FALSE).build();
                excelWriter.fill(data, fillConfig, writeSheet);
                //设置任务完成
                templateTaskModel.setResult(exportName);
                templateTaskModel.setStatus("finish");
            } catch (Exception e) {
                log.error(e.toString());
                //设置任务异常
                templateTaskModel.setError(e.getMessage());
                templateTaskModel.setStatus("finish");
            }
            if (StringUtils.isNotEmpty(templateTaskModel.getCode())) {
                //更新状态
                templateTaskMapper.updateTask(templateTaskModel);
            }
        });
        //等待全流程完成
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if ("finish".equals(templateTaskModel.getStatus())) {
                return templateTaskModel;
            }
        }
        templateTaskModel.setId(SnowflakeIdUtil.getSnowflakeId());
        templateTaskModel.setCode(UUID.randomUUID().toString().replace("-", ""));
        //添加任务
        templateTaskMapper.addTask(templateTaskModel);
        return templateTaskModel;
    }

    @Override
    public File export(String fileName) {
        String exportRoot = SystemChooseUtil.choose(exportForWin, exportForLinux);
        File file = new File(exportRoot + "/" + fileName);
        if (!file.exists()) {
            throw new BusinessException("文件不存在");
        }
        return file;
    }

    private String getParams(Map<String, Object> map) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (Objects.nonNull(entry.getKey())
                    && Objects.nonNull(entry.getValue())
                    && !StringUtils.isAnyEmpty(entry.getKey(), entry.getValue().toString())) {
                list.add(entry.getKey() + "=" + entry.getValue());
            }
        }
        return "?" + list.stream().collect(Collectors.joining("&"));
    }
}
