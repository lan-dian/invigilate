package com.hfut.invigilate.infra.dal.DO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 交换意图
 * </p>
 *
 * @author 常珂洁
 * @since 2021-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ExchangeRecode对象", description="交换意图")
@TableName("exchange_recode")
public class ExchangeRecodeDO implements Serializable {

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

    @ApiModelProperty(value = "想要交换的用户id")
    private String workId;

    @ApiModelProperty(value = "想要交换的监考id")
    private Long invigilateCode;

    @ApiModelProperty(value = "考试时间")
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "交换状态")
    private Integer exchangeState;

    @ApiModelProperty(value = "编码")
    private Long code;

    @ApiModelProperty(value = "交换目标")
    private Long targetCode;

    private Long examCode;


}
