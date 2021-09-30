package com.hfut.invigilate.domain.report.service.impl;

import com.hfut.invigilate.domain.report.entity.Report;
import com.hfut.invigilate.domain.report.repository.ReportRepository;
import com.hfut.invigilate.domain.report.service.ReportService;
import com.hfut.invigilate.interfaces.model.ServiceDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    ReportRepository reportRepository;

    @Override
    public boolean add(Report report){
        return reportRepository.add(report);
    }

    @Override
    public ServiceDTO page(Integer page, Integer limit, String workId){
        return reportRepository.page(page,limit,workId);
    }


}
