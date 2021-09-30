package com.hfut.invigilate.domain.invigilate.entity;

import com.hfut.invigilate.domain.exam.entity.ExamInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExchangeRecode {

    @ApiModelProperty(value = "想要交换的用户id")
    private String workId;

    @ApiModelProperty(value = "想要交换的监考id")
    private Long invigilateCode;

    @ApiModelProperty(value = "交换状态 0代表未交换,1代表交换成功")
    private Integer exchangeState;

    @ApiModelProperty(value = "编码")
    private Long code;

    @ApiModelProperty(value = "交换目标")
    private Long targetCode;

    private ExamInfo examInfo;

}
