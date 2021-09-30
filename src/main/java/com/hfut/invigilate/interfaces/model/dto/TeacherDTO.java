package com.hfut.invigilate.interfaces.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO {

    private String name;
    private String workId;
    private String telephone;
    private String college;

}
