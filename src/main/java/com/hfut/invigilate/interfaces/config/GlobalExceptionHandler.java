package com.hfut.invigilate.interfaces.config;

import com.hfut.invigilate.interfaces.model.CommonResult;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public CommonResult HttpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e){
        String message = e.getCause().getMessage();
        return CommonResult.err(message);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public CommonResult illegalArgumentExceptionHandler(IllegalArgumentException e){
        return CommonResult.err(e.getMessage());
    }
 
    @ExceptionHandler(value = Exception.class)
	public CommonResult exceptionHandler(Exception e){
        //todo 把状态码改成-2，如果发现-2要给后端报警
       	return CommonResult.err(String.valueOf(e));
    }

}