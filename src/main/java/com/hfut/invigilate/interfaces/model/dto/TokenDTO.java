package com.hfut.invigilate.interfaces.model.dto;

import com.hfut.invigilate.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {

    private String workId;
    private Integer roleId;

}
