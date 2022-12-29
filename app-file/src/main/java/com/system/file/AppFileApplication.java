package com.system.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@EnableTransactionManagement
@MapperScan(basePackages = "com.system.file.mapper")
@SpringBootApplication(scanBasePackages = {"com.system.base.component", "com.system.file"})
public class AppFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppFileApplication.class, args);
    }

}
