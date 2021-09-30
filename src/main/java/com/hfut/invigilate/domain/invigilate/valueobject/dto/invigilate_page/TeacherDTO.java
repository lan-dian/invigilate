package com.hfut.invigilate.domain.invigilate.valueobject.dto.invigilate_page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeacherDTO {

    @ApiModelProperty("教师姓名")
    private String name;

    @ApiModelProperty("工号")
    private String workId;

    @ApiModelProperty("电话")
    private String telephone;

    @ApiModelProperty("学院")
    private String college;

}
