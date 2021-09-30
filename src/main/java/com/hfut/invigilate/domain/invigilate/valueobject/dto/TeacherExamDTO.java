package com.hfut.invigilate.domain.invigilate.valueobject.dto;

import com.hfut.invigilate.interfaces.model.enums.ExamStateEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TeacherExamDTO {

    private String name;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String address;

    private Integer studentNum;

    private String examCode;

    private ExamStateEnum examStateEnum;

    private String invigilateCode;


}
