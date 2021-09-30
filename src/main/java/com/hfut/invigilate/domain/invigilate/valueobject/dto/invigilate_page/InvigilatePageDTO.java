package com.hfut.invigilate.domain.invigilate.valueobject.dto.invigilate_page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InvigilatePageDTO {

    @ApiModelProperty("监考id")
    private String code;

    @ApiModelProperty("状态")
    private Integer state;

    @ApiModelProperty("消息")
    private String msg;

    @ApiModelProperty("考试")
    private ExamDTO examDTO;

    @ApiModelProperty
    private TeacherDTO teacherDTO;

}
