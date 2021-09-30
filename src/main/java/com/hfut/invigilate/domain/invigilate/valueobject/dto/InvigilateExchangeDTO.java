package com.hfut.invigilate.domain.invigilate.valueobject.dto;

import com.hfut.invigilate.domain.exam.valueobject.dto.ExamDTO;
import lombok.Data;

@Data
public class InvigilateExchangeDTO {

    String msg;
    String invigilateCode;
    TeacherDTO teacher;
    ExamDTO exam;

}
