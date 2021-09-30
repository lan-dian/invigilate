package com.hfut.invigilate.domain.report.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.domain.report.entity.Report;
import com.hfut.invigilate.interfaces.model.ServiceDTO;

import java.util.List;

public interface ReportRepository {
    Report get(Long reportCode);

    ServiceDTO page(Integer page, Integer limit, String workId);

    boolean add(Report report);

}
