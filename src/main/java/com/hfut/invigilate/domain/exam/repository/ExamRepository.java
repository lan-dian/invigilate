package com.hfut.invigilate.domain.exam.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.domain.exam.valueobject.dto.BadStateExam;
import com.hfut.invigilate.infra.dal.DO.ExamInfoDO;
import com.hfut.invigilate.domain.exam.entity.ExamInfo;
import com.hfut.invigilate.domain.exam.valueobject.dto.ExamInfoDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.TeacherExamDTO;
import com.hfut.invigilate.infra.dal.DO.InvigilateRecordDO;
import com.hfut.invigilate.interfaces.model.PageDTO;

import java.time.LocalDate;
import java.util.List;

public interface ExamRepository {

    ExamInfo get(QueryWrapper<ExamInfoDO> query);

    ExamInfo get(Long code);

    List<ExamInfo> list(QueryWrapper<ExamInfoDO> query);

    boolean save(ExamInfo examInfo);

    boolean update(ExamInfo examInfo,QueryWrapper<ExamInfoDO> query);

    boolean updateByCode(ExamInfo examInfo);

    boolean delete(QueryWrapper<ExamInfoDO> query);

    PageDTO page(Integer page, Integer limit, QueryWrapper<ExamInfoDO> query);

    List<BadStateExam> listBadStateExam();

    ExamInfoDTO getExamInfoDTO(Long examCode);

    List<TeacherExamDTO> getInvigilate(QueryWrapper<InvigilateRecordDO> query);

    List<TeacherExamDTO> getInvigilate(String workId, LocalDate date);


}
