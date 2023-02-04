package com.system.xiumei.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IDemoMapper {
    List listDemo();

    int addDemo(@Param("code") String code);

    int updateDemo(@Param("code") String code, @Param("result") String result);
}
