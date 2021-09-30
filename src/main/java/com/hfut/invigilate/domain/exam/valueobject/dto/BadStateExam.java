package com.hfut.invigilate.domain.exam.valueobject.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BadStateExam {

    @ApiModelProperty("已有老师人数")
    private Integer num;

    @ApiModelProperty("需要老师人数")
    private Integer teacherNum;

    @ApiModelProperty("考试编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("考试地点")
    private String address;

    @ApiModelProperty("日期")
    private LocalDate date;

    @ApiModelProperty("开始时间")
    private LocalTime startTime;

    @ApiModelProperty("结束时间")
    private LocalTime endTime;

}
