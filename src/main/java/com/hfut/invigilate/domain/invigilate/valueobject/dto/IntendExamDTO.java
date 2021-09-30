package com.hfut.invigilate.domain.invigilate.valueobject.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IntendExamDTO {
    String invigilateCode;
    String exchangeCode;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String name;
    String address;
}
