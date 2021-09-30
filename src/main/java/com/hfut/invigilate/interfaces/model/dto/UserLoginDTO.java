package com.hfut.invigilate.interfaces.model.dto;

import com.hfut.invigilate.domain.user.entity.UserRole;
import lombok.Data;

@Data
public class UserLoginDTO {

    private String workId;
    private String name;
    private String password;
    private Integer rodeId;

}
