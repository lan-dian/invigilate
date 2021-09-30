package com.hfut.invigilate.domain.exam.valueobject.dto;

import com.hfut.invigilate.domain.invigilate.valueobject.dto.TeacherListDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ExamInfoDTO {

    private String name;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String address;

    private Integer studentNum;

    private String classInfo;

    private String examCode;

    private String invigilateCode;

    private List<TeacherListDTO> teachers;


}
