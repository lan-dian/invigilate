package com.hfut.invigilate.domain.user.vlaueobject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDTO {

    private String name;

    private String workId;

    private String college;

    private String telephone;

}
