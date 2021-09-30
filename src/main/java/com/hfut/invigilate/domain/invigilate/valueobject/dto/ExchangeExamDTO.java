package com.hfut.invigilate.domain.invigilate.valueobject.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
public class ExchangeExamDTO {

    private String examName;

    private String address;

    private String date;

    private String startTime;

    private String endTime;

}
