package com.system.xiumei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@EnableTransactionManagement
@MapperScan(basePackages = "com.system.xiumei.mapper")
@SpringBootApplication(scanBasePackages = {"com.system"})
public class AppXiumeiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppXiumeiApplication.class, args);
    }

}
