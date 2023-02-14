package com.system.file.util;

import com.system.base.exception.BusinessException;
import com.system.file.domain.dto.template.TemplateDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TemplateUtil {
    public static TemplateDTO getExportTemplate(String id) {
        TemplateDTO dto;
        try {
            File templateFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "template/config_export.xml");
            dto = getTemplate(id, templateFile);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return dto;
    }

    public static TemplateDTO getImportTemplate(String id) {
        TemplateDTO dto;
        try {
            File templateFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "template/config_import.xml");
            dto = getTemplate(id, templateFile);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return dto;
    }

    private static TemplateDTO getTemplate(String id, File templateFile) {
        if (!templateFile.exists()) {
            throw new BusinessException("模板配置文件不存在");
        }
        TemplateDTO dto = new TemplateDTO();
        try (
                InputStream inputStream = new FileInputStream(templateFile)
        ) {
            Document document = new SAXReader().read(inputStream);
            Element rootElement = document.getRootElement();
            List<Element> list = rootElement.elements("template");
            list = list.stream().filter(item -> id.equals(item.attributeValue("id"))).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(list)) {
                throw new BusinessException("id有误");
            }
            Element element = list.get(0);
            dto.setPathFile(ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "template/" + element.attributeValue("path")));
            if (!dto.getPathFile().exists()) {
                throw new BusinessException("模板文件不存在");
            }
            String dataRow = element.attributeValue("data-row");
            if (StringUtils.isNotEmpty(dataRow)) {
                dto.setDateRow(new Integer(dataRow));
            }
            dto.setServerName(element.attributeValue("server-name"));
            dto.setRequestUrl(element.attributeValue("request-url"));
            if (StringUtils.isAnyEmpty(dto.getServerName(), dto.getRequestUrl())) {
                throw new BusinessException("server-name、request-url不能为空");
            }
            List<Element> titleElements = element.elements("title");
            List<String> fields = new ArrayList<>();
            for (Element title : titleElements) {
                fields.add(title.attributeValue("field"));
            }
            dto.setFields(fields);
        } catch (Exception e) {
            throw new BusinessException("模板配置有误：" + e.getMessage());
        }
        return dto;
    }
}
