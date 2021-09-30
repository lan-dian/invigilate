package com.hfut.invigilate.interfaces.model;

import lombok.Data;

@Data
public class ServiceDTO {

    private Boolean success;
    private String msg;
    private Object data;

    public static ServiceDTO ok(){
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.success=true;
        serviceDTO.msg="成功";
        serviceDTO.data=null;
        return serviceDTO;
    }

    public static ServiceDTO err(String msg){
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.success=false;
        serviceDTO.msg=msg;
        serviceDTO.data=null;
        return serviceDTO;
    }

    public static ServiceDTO err(String msg,Object data){
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.success=false;
        serviceDTO.msg=msg;
        serviceDTO.data=data;
        return serviceDTO;
    }

    public static ServiceDTO ok(Object data){
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.success=true;
        serviceDTO.msg="成功";
        serviceDTO.data=data;
        return serviceDTO;
    }

    public static ServiceDTO back(Boolean state) {
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.success = state;
        if (state) {
            serviceDTO.msg = "成功";
        } else {
            serviceDTO.msg = "失败";
        }
        return serviceDTO;
    }

    public static ServiceDTO build(Boolean state, String msg) {
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.success = state;
        serviceDTO.msg = msg;
        return serviceDTO;
    }

}
