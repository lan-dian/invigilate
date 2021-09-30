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
 * 监考表
 * </p>
 *
 * @author 常珂洁
 * @since 2021-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="InvigilateRecord对象", description="监考表")
@TableName("invigilate_record")
public class InvigilateRecordDO implements Serializable {

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

    @ApiModelProperty(value = "工号")
    private String workId;

    @ApiModelProperty(value = "考试ID")
    private Long examCode;

    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "生成方式")
    private Integer generationMethod;

    @ApiModelProperty(value = "编码")
    private Long code;

    private String msg;//调换者空余时间备注


}
