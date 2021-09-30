package com.hfut.invigilate.domain.exam.service;

import com.hfut.invigilate.domain.exam.entity.ExamInfo;
import com.hfut.invigilate.domain.exam.valueobject.dto.BadStateExam;
import com.hfut.invigilate.domain.exam.valueobject.dto.ExamInfoDTO;
import com.hfut.invigilate.domain.exam.valueobject.dto.page.ExamPageQueryDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.TeacherExamDTO;
import com.hfut.invigilate.interfaces.model.PageDTO;
import com.hfut.invigilate.interfaces.model.ServiceDTO;

import java.util.List;

public interface ExamService {
    ExamInfoDTO getExamInfo(Long examCode,String workId);

    List<TeacherExamDTO> getInvigilate(String workId);

    PageDTO page(Integer page, Integer limit, ExamPageQueryDTO query);

    ServiceDTO save(ExamInfo examInfo);

    ServiceDTO update(ExamInfo examInfo);

    List<BadStateExam> listBadStateExam();

    ServiceDTO delete(Long examCode);
}
