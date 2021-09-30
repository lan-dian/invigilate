package com.hfut.invigilate.domain.report.entity;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    private Long code;

    private String workId;

    private String msg;

    private String title;

    private LocalDateTime sendTime;

}
