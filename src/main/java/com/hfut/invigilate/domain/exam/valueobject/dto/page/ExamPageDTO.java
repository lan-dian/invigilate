package com.hfut.invigilate.domain.exam.valueobject.dto.page;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ExamPageDTO {

    private String code;

    private String name;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String address;

    private Integer studentNum;

    private Integer teacherNum;

}
