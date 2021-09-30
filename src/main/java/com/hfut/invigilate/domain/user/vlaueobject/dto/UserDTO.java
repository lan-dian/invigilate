package com.hfut.invigilate.domain.user.vlaueobject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class UserDTO {

    private String name;

    private String workId;

    private String college;

    private String telephone;

    @JsonIgnore
    private String password;

    private List<Integer> roles;

}
