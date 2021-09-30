package com.hfut.invigilate.interfaces.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
import javax.servlet.http.HttpSession;
 
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .ignoredParameterTypes(HttpSession.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hfut.invigilate.interfaces.webapi.controller"))//基础包名
                .paths(PathSelectors.any())
                .build();
    }
}