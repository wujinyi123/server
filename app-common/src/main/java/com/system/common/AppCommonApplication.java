package com.system.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@EnableTransactionManagement
@MapperScan(basePackages = "com.system.common.mapper")
@SpringBootApplication(scanBasePackages = {"com.system.base.component", "com.system.common"})
public class AppCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppCommonApplication.class, args);
    }

}
