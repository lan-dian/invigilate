package com.hfut.invigilate.domain.invigilate.valueobject.dto;

import com.hfut.invigilate.domain.invigilate.valueobject.dto.IntendExamDTO;
import lombok.Data;

import java.util.List;

@Data
public class IntendDTO {

    String workId;
    String name;
    String telephone;
    String college;
    List<IntendExamDTO> exam;

}
