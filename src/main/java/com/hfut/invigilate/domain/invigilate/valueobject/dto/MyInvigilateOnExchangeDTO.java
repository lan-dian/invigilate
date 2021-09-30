package com.hfut.invigilate.domain.invigilate.valueobject.dto;

import com.hfut.invigilate.interfaces.model.enums.ExamStateEnum;
import com.hfut.invigilate.domain.exam.valueobject.dto.ExamDTO;
import lombok.Data;

@Data
public class MyInvigilateOnExchangeDTO {

    private String invigilateCode;

    private ExamStateEnum state;

    private String msg;

    private ExamDTO exam;

}
