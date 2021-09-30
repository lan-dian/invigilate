package com.hfut.invigilate.interfaces.model;

import com.hfut.invigilate.interfaces.model.enums.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResult {

    private int code;
    private String msg;
    private Object data;

    public static CommonResult back(boolean success){
        if(success){
            return new CommonResult(0,"成功",null);
        }else {
            return new CommonResult(-1,"失败",null);
        }
    }

    public static CommonResult ok(){
        return new CommonResult(0,"成功!",null);
    }

    public static CommonResult ok(String msg){
        return new CommonResult(0,msg,null);
    }

    public static CommonResult body(Object data){
        return new CommonResult(0,"成功!",data);
    }

    public static CommonResult body(Object data, String msg){
        return new CommonResult(0,msg,data);
    }

    public static CommonResult err(String msg){
        return new CommonResult(-1,msg,null);
    }

    public static CommonResult err(){
        return new CommonResult(-1,"失败",null);
    }

    public static CommonResult err(ErrorEnum err){
        return new CommonResult(err.getCode(),err.getMsg(),null);
    }

    public static CommonResult err(int code, String msg){
        return new CommonResult(code,msg,null);
    }

    public static CommonResult back(ServiceDTO serviceDTO){return new CommonResult(serviceDTO.getSuccess()?0:-1, serviceDTO.getMsg(),serviceDTO.getData());}

}