package com.hfut.invigilate.interfaces.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfut.invigilate.interfaces.config.ControllerAspect;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class LogUtil {
    

    private static final ObjectMapper objectMapper=new ObjectMapper();


    /**
     * @param business 业务名
     * @param businessResult 业务结果
     * @param params 参数
     * @param results 返回值
     */
    @SneakyThrows
    public static void info(String business , String businessResult , Object[] params , Object[] results){
        String traceId = ControllerAspect.getThreadTraceId();

        log.info("{} | {} | {} | {} | {}" , traceId , business , businessResult , objectMapper.writeValueAsString(params)  , objectMapper.writeValueAsString(results));
    }

    @SneakyThrows
    public static void info(String business , String businessResult , Object[] params){
        String traceId = ControllerAspect.getThreadTraceId();
        log.info("{} | {} | {} | {}" , traceId , business , businessResult ,objectMapper.writeValueAsString( params));
    }

    @SneakyThrows
    public static void err(String business , String businessResult , Object[] params){
        String traceId = ControllerAspect.getThreadTraceId();
        log.error("{} | {} | {} | {} " , traceId , business , businessResult , objectMapper.writeValueAsString(params));
    }

    @SneakyThrows
    public static void err(String business , String businessResult , Object[] params , Throwable e){
        String traceId = ControllerAspect.getThreadTraceId();
        log.error("{} | {} | {} | {} | {}" , traceId , business , objectMapper.writeValueAsString(businessResult) , params , e);
    }


}
