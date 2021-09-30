package com.hfut.invigilate.domain.report.service;

import com.hfut.invigilate.domain.report.entity.Report;
import com.hfut.invigilate.interfaces.model.ServiceDTO;

public interface ReportService {

    boolean add(Report report);

    ServiceDTO page(Integer page, Integer limit, String workId);
}
