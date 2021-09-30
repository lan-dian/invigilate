package com.hfut.invigilate;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableSwaggerBootstrapUI
@MapperScan("com.hfut.invigilate.infra.dal.dao")
public class InvigilateApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvigilateApplication.class, args);
    }

}
