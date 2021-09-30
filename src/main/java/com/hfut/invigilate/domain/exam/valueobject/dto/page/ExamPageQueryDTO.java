package com.hfut.invigilate.domain.exam.valueobject.dto.page;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExamPageQueryDTO {

    private String code;

    private String name;

    private LocalDate date;

    private String address;

}
