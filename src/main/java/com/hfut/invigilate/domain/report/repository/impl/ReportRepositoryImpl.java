package com.hfut.invigilate.domain.report.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfut.invigilate.domain.report.entity.Report;
import com.hfut.invigilate.domain.report.repository.ReportRepository;
import com.hfut.invigilate.domain.report.valueobject.ReportDTO;
import com.hfut.invigilate.infra.dal.DO.ReportDO;
import com.hfut.invigilate.infra.dal.dao.ReportMapper;
import com.hfut.invigilate.infra.redis.service.RedisService;
import com.hfut.invigilate.interfaces.model.ServiceDTO;
import com.hfut.invigilate.interfaces.utils.IdWorker;
import io.swagger.models.auth.In;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportRepositoryImpl implements ReportRepository {

    @Resource
    ReportMapper reportMapper;

    @Resource
    IdWorker idWorker;

    @Resource
    RedisService redisService;

    @Override
    public Report get(Long reportCode){
        QueryWrapper<ReportDO> query=new QueryWrapper<>();
        query.eq("code",reportCode);

        ReportDO reportDO = reportMapper.selectOne(query);
        if(reportDO==null){
            return null;
        }
        return getReport(reportDO);
    }

    @Override
    public ServiceDTO page(Integer page, Integer limit, String workId){
        IPage<ReportDO> iPage=new Page<>(page,limit);

        QueryWrapper<ReportDO> query=new QueryWrapper<>();
        query.eq("work_id",workId);
        reportMapper.selectPage(iPage,query);

        Map<String, Object> map=new HashMap<>();
        map.put("n",iPage.getTotal());
        List<ReportDO> records = iPage.getRecords();
        List<ReportDTO> data=new ArrayList<>();

        for (ReportDO record : records) {
            data.add(getReportDTO(record));
        }
        map.put("data",data);

        return ServiceDTO.ok(map);
    }

    private ReportDTO getReportDTO(ReportDO reportDO){
        ReportDTO build = ReportDTO.builder()
                .code(String.valueOf(reportDO.getCode()))
                .msg(reportDO.getMsg())
                .sendTime(reportDO.getSendTime())
                .title(reportDO.getTitle())
                .build();
        return build;
    }


    private Report getReport(ReportDO reportDO) {
        Report build = Report.builder()
                .code(reportDO.getCode())
                .msg(reportDO.getMsg())
                .title(reportDO.getTitle())
                .sendTime(reportDO.getSendTime())
                .workId(reportDO.getWorkId())
                .build();
        return build;
    }

    @Override
    public boolean add(Report report){
        report.setCode(idWorker.nextId());

        ReportDO reportDO = new ReportDO();
        reportDO.setCode(report.getCode())
                .setGmtCreate(LocalDateTime.now())
                .setGmtModified(LocalDateTime.now())
                .setMsg(report.getMsg())
                .setTitle(report.getTitle())
                .setWorkId(report.getWorkId())
                .setSendTime(LocalDateTime.now());
        int insert = reportMapper.insert(reportDO);

        if(insert>0){
            if("0".equals(report.getWorkId())){
                redisService.reportToAdmin(report.getCode());
            }else {
                redisService.reportToTeacher(report.getWorkId(),report.getCode());
            }
            return true;
        }else {
            return false;
        }
    }



}
