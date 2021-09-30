package com.hfut.invigilate.domain.user.vlaueobject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageQueryDTO {

    private String name;

    private String workId;

    private String telephone;

    private String college;

    private Integer roleId;

}
