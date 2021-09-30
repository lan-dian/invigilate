package com.hfut.invigilate.application.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfut.invigilate.application.ExcelService;
import com.hfut.invigilate.domain.exam.entity.ExamInfo;
import com.hfut.invigilate.domain.exam.service.ExamService;
import com.hfut.invigilate.domain.invigilate.entity.Invigilate;
import com.hfut.invigilate.domain.invigilate.service.InvigilateService;
import com.hfut.invigilate.domain.invigilate.valueobject.enums.GenerationMethodEnum;
import com.hfut.invigilate.domain.invigilate.valueobject.enums.InvigilateStateEnum;
import com.hfut.invigilate.domain.user.entity.User;
import com.hfut.invigilate.domain.user.entity.UserRole;
import com.hfut.invigilate.domain.user.service.UserService;
import com.hfut.invigilate.interfaces.model.InvigilateInfoDTO;
import com.hfut.invigilate.interfaces.model.dto.TeacherDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExcelServiceImpl implements ExcelService {

    @Resource
    UserService userService;

    @Resource
    ExamService examService;

    @Resource
    InvigilateService invigilateService;

    @Resource
    ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean loadExcelInSQl(List<InvigilateInfoDTO> invigilateInfoDTOS){
        List<UserRole> userRole=new ArrayList<>();
        userRole.add(UserRole.Teacher);
        for (InvigilateInfoDTO invigilateInfoDTO : invigilateInfoDTOS) {
            String classInfo = null;
            try {
                classInfo = objectMapper.writeValueAsString(invigilateInfoDTO.getClassInfo());
            } catch (JsonProcessingException e) {
                log.error("ExcelServiceImpl.loadExcelInSQl Excel数据存入SQL 对象序列化错误 e={ }",e);
            }
            ExamInfo examInfo = ExamInfo.builder()
                    .name(invigilateInfoDTO.getName())
                    .date(invigilateInfoDTO.getDate())
                    .startTime(invigilateInfoDTO.getStartTime())
                    .endTime(invigilateInfoDTO.getEndTime())
                    .address(invigilateInfoDTO.getAddress())
                    .studentNum(invigilateInfoDTO.getStudentNum())
                    .teacherNum(invigilateInfoDTO.getTeachers().size())
                    .teachingClass(classInfo)
                    .build();
            List<TeacherDTO> teacherDTOS = invigilateInfoDTO.getTeachers();
            for (TeacherDTO teacherDTO : teacherDTOS) {
                User user= User.builder()
                        .name(teacherDTO.getName())
                        .workId(teacherDTO.getWorkId())
                        .college(teacherDTO.getCollege())
                        .telephone(teacherDTO.getTelephone())
                        .roles(userRole)
                        .build();
                userService.save(user);
            }
            if(examService.save(examInfo).getSuccess()){//考试插入成功
                Long code = examInfo.getCode();
                log.info("examCode={}",code);
                for (TeacherDTO teacherDTO : teacherDTOS) {
                    Invigilate build = Invigilate.builder()
                            .examCode(code)
                            .workId(teacherDTO.getWorkId())
                            .state(InvigilateStateEnum.Init.getCode())
                            .generationMethod(GenerationMethodEnum.Excel.getCode())
                            .build();
                    invigilateService.save(build);
                }
            }else{
                log.error("ExcelServiceImpl.loadExcelInSQl 监考信息导入数据库 {}",examInfo.getCode());
            }
        }
        return true;
    }

}
