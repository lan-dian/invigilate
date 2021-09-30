package com.hfut.invigilate.interfaces.model;

import lombok.Data;

@Data
public class PageDTO {

    private Long n;
    private Object data;

    public static PageDTO build(Long n,Object data){
        PageDTO pageDTO = new PageDTO();
        pageDTO.n=n;
        pageDTO.data=data;
        return pageDTO;
    }

}
