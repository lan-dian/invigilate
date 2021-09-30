package com.hfut.invigilate.interfaces.config;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfut.invigilate.interfaces.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class ControllerAspect {

    @Resource
    ObjectMapper objectMapper;


    private String name = "easy-travel-server";

    @Pointcut("execution(public * com.hfut.invigilate.interfaces.webapi.controller.*.*(..))")
    public void controllerLog(){}


    //todo 把这些字段包含在一个类里面 修改下面日志的格式，做到统一
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    private static ThreadLocal<String> requestId = new ThreadLocal<>();

    private ThreadLocal<String> interfaceName = new ThreadLocal<>();

    private ThreadLocal<String> param = new ThreadLocal<>();

    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");


    @Before("controllerLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 设置请求开始时间
        startTime.set(System.currentTimeMillis());
        Date stTimeDate = new Date(startTime.get());
        String dateStr = dataFormat.format(stTimeDate);
        // 设置请求标识
        String requestIdStr = UUID.randomUUID().toString();
        requestId.set(requestIdStr);
        // 提取全部参数  paramJson
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> paramJson=new HashMap<>();
        while(paramNames.hasMoreElements()){
            String paramName = paramNames.nextElement();
            paramJson.put(paramName, request.getParameter(paramName));
        }

        // 提取接口标识（url中截取）
        String requestUrl = request.getRequestURL().toString();
        int start = requestUrl.lastIndexOf("/")+1;
        String interfaceNameStr = null;
        if (requestUrl.contains("?")){
            interfaceNameStr = requestUrl.substring(start, requestUrl.indexOf("?"));
        } else {
            interfaceNameStr = requestUrl.substring(start);
        }
        param.set(objectMapper.writeValueAsString(paramJson));
        interfaceName.set(interfaceNameStr);
        // 将requst的唯一标识放置在request中，在其他环节可以穿起来
        request.setAttribute("requestId", requestId.get());
    }

    public static String getThreadTraceId(){
        return requestId.get();
    }

    @AfterReturning(returning="rvt",pointcut="controllerLog()")
    public void doAfterReturning(JoinPoint joinPoint,Object rvt) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("finished" + " " + name + " " + interfaceName.get() + " " + requestId.get() + " "
                 + request.getRequestURL().toString() + " " + param.get()
                 + (System.currentTimeMillis() - startTime.get())
                 + " " + rvt.toString());


        requestId.remove();
    }

    @AfterThrowing(throwing="ex", pointcut="controllerLog()")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
         // 发生地点
         int lineNum = 0;
         String className = null;
         String methodName = null;
         StackTraceElement[] st = ex.getStackTrace();
         for (StackTraceElement stackTraceElement : st) {
             lineNum = stackTraceElement.getLineNumber();
             className = stackTraceElement.getClassName();
             methodName = stackTraceElement.getMethodName();
            System.out.println("[类:" + className + "]调用"
            + methodName + "时在第" + lineNum
            + "行代码处发生异常!异常类型:" + ex.getClass().getName());
            break;
         }
         String exceptionMessage = "[类:" + className + "]调用"+ methodName + "时在第" + lineNum + "行代码处发生异常!异常类型:" + ex.getClass().getName();

        /*
         log.info("exception" + " " + name + " " + interfaceName.get() + " " + requestId.get() + " "
                  + request.getRequestURL().toString() + " " + param.get()
                  + " " + exceptionMessage);
        */
        LogUtil.err(className+"_"+methodName,"系统报警",new Object[]{lineNum+"行",ex.getCause()});

        requestId.remove();
    }
}