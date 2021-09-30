package com.hfut.invigilate.domain.invigilate.entity;

import com.hfut.invigilate.domain.exam.entity.ExamInfo;
import com.hfut.invigilate.domain.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Invigilate {

    @ApiModelProperty(value = "编码")
    private Long code;

    @ApiModelProperty(value = "工号")
    private String workId;

    @ApiModelProperty(value = "考试ID")
    private Long examCode;

    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "生成方式")
    private Integer generationMethod;

    private String msg;

    private User user;

    private ExamInfo examInfo;



}
