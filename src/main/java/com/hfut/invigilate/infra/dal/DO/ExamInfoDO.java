package com.hfut.invigilate.infra.dal.DO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * 考试
 * </p>
 *
 * @author 常珂洁
 * @since 2021-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ExamInfo对象", description="考试")
@TableName("exam_info")
public class ExamInfoDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

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
