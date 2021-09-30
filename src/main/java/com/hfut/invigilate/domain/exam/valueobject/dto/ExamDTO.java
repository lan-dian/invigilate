package com.hfut.invigilate.domain.exam.valueobject.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ExamDTO {

    private String name;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String address;
    private Integer studentNum;

}
