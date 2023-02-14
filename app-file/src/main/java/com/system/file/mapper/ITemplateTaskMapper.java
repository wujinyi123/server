package com.system.file.mapper;

import com.system.file.domain.model.TemplateTaskModel;

public interface ITemplateTaskMapper {
    int addTask(TemplateTaskModel model);

    int updateTask(TemplateTaskModel model);
}
