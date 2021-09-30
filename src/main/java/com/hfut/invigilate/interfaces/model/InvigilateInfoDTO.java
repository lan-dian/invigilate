package com.hfut.invigilate.interfaces.model;

import com.hfut.invigilate.interfaces.model.dto.ClassInfoDTO;
import com.hfut.invigilate.interfaces.model.dto.TeacherDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Slf4j
public class InvigilateInfoDTO {

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private List<TeacherDTO> teachers;

    private Integer studentNum;

    private String address;

    private String name;

    private ClassInfoDTO classInfo;

}