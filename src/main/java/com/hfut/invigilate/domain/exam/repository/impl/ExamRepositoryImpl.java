package com.hfut.invigilate.domain.exam.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfut.invigilate.domain.exam.valueobject.dto.BadStateExam;
import com.hfut.invigilate.infra.dal.DO.ExamInfoDO;
import com.hfut.invigilate.infra.dal.DO.InvigilateRecordDO;
import com.hfut.invigilate.infra.dal.dao.ExamInfoMapper;
import com.hfut.invigilate.domain.exam.entity.ExamInfo;
import com.hfut.invigilate.domain.exam.repository.ExamRepository;
import com.hfut.invigilate.domain.exam.valueobject.dto.ExamInfoDTO;
import com.hfut.invigilate.domain.exam.valueobject.dto.page.ExamPageDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.TeacherExamDTO;
import com.hfut.invigilate.interfaces.model.PageDTO;
import com.hfut.invigilate.interfaces.utils.IdWorker;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExamRepositoryImpl implements ExamRepository {

    @Resource
    ExamInfoMapper examInfoMapper;

    @Resource
    IdWorker idWorker;

    @Override
    public List<BadStateExam> listBadStateExam(){
        return examInfoMapper.listBadStateExam();
    }

    @Override
    public ExamInfoDTO getExamInfoDTO(Long examCode) {
        return examInfoMapper.getExamInfoByCode(examCode);
    }

    @Override
    public List<TeacherExamDTO> getInvigilate(QueryWrapper<InvigilateRecordDO> query){
        return examInfoMapper.listTeacherExam(query);
    }

    @Override
    public List<TeacherExamDTO> getInvigilate(String workId, LocalDate date) {
        return examInfoMapper.listExamInfo(workId, date);
    }

    @Override
    public PageDTO page(Integer page, Integer limit, QueryWrapper<ExamInfoDO> query) {
        IPage<ExamInfoDO> iPage = new Page<>(page, limit);
        examInfoMapper.selectPage(iPage, query);

        long n = iPage.getTotal();
        List<ExamPageDTO> data = new ArrayList<>();
        List<ExamInfoDO> examInfoDOs = iPage.getRecords();

        for (ExamInfoDO examInfoDO : examInfoDOs) {
            ExamPageDTO build = ExamPageDTO.builder()
                    .code(String.valueOf(examInfoDO.getCode()))
                    .name(examInfoDO.getName())
                    .date(examInfoDO.getDate())
                    .startTime(examInfoDO.getStartTime())
                    .endTime(examInfoDO.getEndTime())
                    .address(examInfoDO.getAddress())
                    .studentNum(examInfoDO.getStudentNum())
                    .teacherNum(examInfoDO.getTeacherNum())
                    .build();
            data.add(build);
        }
        return PageDTO.build(n, data);
    }

    @Override
    public ExamInfo get(QueryWrapper<ExamInfoDO> query) {
        ExamInfoDO examInfoDO = examInfoMapper.selectOne(query);
        return getExamInfo(examInfoDO);
    }

    @Override
    public ExamInfo get(Long code) {
        QueryWrapper<ExamInfoDO> query = new QueryWrapper<>();
        query.eq("code", code);
        ExamInfoDO examInfoDO = examInfoMapper.selectOne(query);
        return getExamInfo(examInfoDO);
    }

    private ExamInfo getExamInfo(ExamInfoDO examInfoDO) {
        ExamInfo build = ExamInfo.builder()
                .code(examInfoDO.getCode())
                .name(examInfoDO.getName())
                .date(examInfoDO.getDate())
                .startTime(examInfoDO.getStartTime())
                .endTime(examInfoDO.getEndTime())
                .address(examInfoDO.getAddress())
                .studentNum(examInfoDO.getStudentNum())
                .teacherNum(examInfoDO.getTeacherNum())
                .build();
        return build;
    }

    @Override
    public boolean save(ExamInfo examInfo) {
        examInfo.setCode(idWorker.nextId());//生成code,便于service回调

        ExamInfoDO examInfoDO = new ExamInfoDO();
        examInfoDO.setGmtCreate(LocalDateTime.now())
                .setGmtModified(LocalDateTime.now())
                .setName(examInfo.getName())
                .setDate(examInfo.getDate())
                .setStartTime(examInfo.getStartTime())
                .setEndTime(examInfo.getEndTime())
                .setAddress(examInfo.getAddress())
                .setStudentNum(examInfo.getStudentNum())
                .setTeacherNum(examInfo.getTeacherNum())
                .setTeachingClass(examInfo.getTeachingClass())
                .setCode(examInfo.getCode());

        try {
            int insert = examInfoMapper.insert(examInfoDO);
            return insert > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(ExamInfo examInfo, QueryWrapper<ExamInfoDO> query) {
        ExamInfoDO examInfoDO = getExamInfoDO(examInfo);

        int update = examInfoMapper.update(examInfoDO, query);
        return update > 0;
    }

    @Override
    public boolean updateByCode(ExamInfo examInfo) {
        QueryWrapper<ExamInfoDO> query = new QueryWrapper<>();
        query.eq("code", examInfo.getCode());

        ExamInfoDO examInfoDO = getExamInfoDO(examInfo);

        int update = examInfoMapper.update(examInfoDO, query);
        return update > 0;
    }

    private ExamInfoDO getExamInfoDO(ExamInfo examInfo) {
        ExamInfoDO examInfoDO = new ExamInfoDO();
        examInfoDO.setGmtModified(LocalDateTime.now())
                .setName(examInfo.getName())
                .setDate(examInfo.getDate())
                .setStartTime(examInfo.getStartTime())
                .setEndTime(examInfo.getEndTime())
                .setAddress(examInfo.getAddress())
                .setStudentNum(examInfo.getStudentNum())
                .setTeacherNum(examInfo.getTeacherNum())
                .setTeachingClass(examInfo.getTeachingClass());
        return examInfoDO;
    }

    @Override
    public boolean delete(QueryWrapper<ExamInfoDO> query) {
        int delete = examInfoMapper.delete(query);
        return delete > 0;
    }

    @Override
    public List<ExamInfo> list(QueryWrapper<ExamInfoDO> query) {

        List<ExamInfoDO> examInfoDOS = examInfoMapper.selectList(query);
        List<ExamInfo> examInfos = new ArrayList<>();
        if (examInfoDOS == null) {
            return examInfos;
        }
        for (ExamInfoDO examInfoDO : examInfoDOS) {
            ExamInfo build = getExamInfo(examInfoDO);
            examInfos.add(build);
        }
        return examInfos;
    }

}
