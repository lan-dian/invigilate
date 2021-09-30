package com.hfut.invigilate.domain.report.valueobject;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReportDTO {

    private String code;

    private String msg;

    private String title;

    private LocalDateTime sendTime;

}
