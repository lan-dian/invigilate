package com.hfut.invigilate.infra.dal.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hfut.invigilate.domain.exam.valueobject.dto.BadStateExam;
import com.hfut.invigilate.infra.dal.DO.ExamInfoDO;
import com.hfut.invigilate.domain.exam.valueobject.dto.ExamInfoDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.TeacherExamDTO;
import com.hfut.invigilate.infra.dal.DO.InvigilateRecordDO;
import com.hfut.invigilate.infra.dal.DO.UserDO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 考试 Mapper 接口
 * </p>
 *
 * @author 常珂洁
 * @since 2021-09-15
 */
public interface ExamInfoMapper extends BaseMapper<ExamInfoDO> {

    @Deprecated
    List<TeacherExamDTO> listExamInfo(String workId, LocalDate date);

    List<TeacherExamDTO> listTeacherExam(@Param(Constants.WRAPPER) QueryWrapper<InvigilateRecordDO> query);

    ExamInfoDTO getExamInfoByCode(Long code);

    @Deprecated
    List<TeacherExamDTO> listExamInfoByDate(String workId, LocalDate date);

    List<BadStateExam> listBadStateExam();

}
