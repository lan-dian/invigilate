package com.hfut.invigilate.interfaces.config;

import com.hfut.invigilate.interfaces.utils.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0,0);
    }

}
