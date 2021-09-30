package com.hfut.invigilate.domain.invigilate.valueobject.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvigilateQueryDTO {

    @ApiModelProperty("工号")
    private String workId;

    @ApiModelProperty("教师名称")
    private String teacherName;

    @ApiModelProperty("考试名称")
    private String examName;

    @ApiModelProperty("考试时间")
    private String date;

}
