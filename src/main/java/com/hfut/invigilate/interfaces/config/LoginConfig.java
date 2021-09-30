package com.hfut.invigilate.interfaces.config;

import com.hfut.invigilate.interfaces.webapi.interceptor.AuthHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Resource
    AuthHandlerInterceptor authHandlerInterceptor;

    /**
     * 给除了 /login 的接口都配置拦截器,拦截转向到 authHandlerInterceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //todo 双向拦截器，教师和管理员分别拦截


        /* registry.addInterceptor(authHandlerInterceptor)
                 .addPathPatterns("/teacher/**","/admin/**")
                 .excludePathPatterns("/login");

         */
    }
}