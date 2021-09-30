package com.hfut.invigilate.domain.invigilate.valueobject.dto;

import com.hfut.invigilate.domain.exam.entity.ExamInfo;
import com.hfut.invigilate.domain.exam.valueobject.dto.ExamInfoDTO;
import lombok.Data;

@Data
public class ExchangeInfoDTO {

    private Long exchangeCode;

    private ExchangeExamDTO self;

    private ExchangeExamDTO target;

}
