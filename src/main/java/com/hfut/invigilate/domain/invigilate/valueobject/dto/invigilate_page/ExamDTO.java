package com.hfut.invigilate.domain.invigilate.valueobject.dto.invigilate_page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ExamDTO {

    @ApiModelProperty("考试编码")
    private String code;

    @ApiModelProperty("考试名称")
    private String name;

    @ApiModelProperty("考试日期")
    private LocalDate date;

    @ApiModelProperty("开始时间")
    private LocalTime startTime;

    @ApiModelProperty("结束时间")
    private LocalTime endTime;

    @ApiModelProperty("考试地点")
    private String address;

    @ApiModelProperty("教师人数")
    private Integer teacherNum;

    @ApiModelProperty("学生人数")
    private Integer studentNum;

}
