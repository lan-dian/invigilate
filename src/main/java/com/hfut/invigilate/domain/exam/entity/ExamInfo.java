package com.hfut.invigilate.domain.exam.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamInfo {

    @ApiModelProperty(value = "考试名称")
    private String name;

    @ApiModelProperty(value = "考试日期")
    private LocalDate date;

    @ApiModelProperty(value = "开始时间")
    private LocalTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "考试人数")
    private Integer studentNum;

    @ApiModelProperty(value = "监考人数")
    private Integer teacherNum;

    @ApiModelProperty(value = "教学班")
    private String teachingClass;

    @ApiModelProperty(value = "编码")
    private Long code;


}
