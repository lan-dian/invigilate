package com.hfut.invigilate.domain.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.domain.exam.valueobject.dto.BadStateExam;
import com.hfut.invigilate.domain.invigilate.entity.ExchangeRecode;
import com.hfut.invigilate.domain.invigilate.repository.ExchangeRepository;
import com.hfut.invigilate.infra.dal.DO.ExamInfoDO;
import com.hfut.invigilate.infra.dal.DO.ExchangeRecodeDO;
import com.hfut.invigilate.infra.dal.DO.InvigilateRecordDO;
import com.hfut.invigilate.domain.exam.entity.ExamInfo;
import com.hfut.invigilate.domain.exam.repository.ExamRepository;
import com.hfut.invigilate.domain.exam.service.ExamService;
import com.hfut.invigilate.domain.exam.valueobject.dto.page.ExamPageQueryDTO;
import com.hfut.invigilate.domain.invigilate.entity.Invigilate;
import com.hfut.invigilate.domain.invigilate.repository.InvigilateRepository;
import com.hfut.invigilate.interfaces.model.PageDTO;
import com.hfut.invigilate.interfaces.model.ServiceDTO;
import com.hfut.invigilate.domain.exam.valueobject.dto.ExamInfoDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.TeacherExamDTO;
import com.hfut.invigilate.interfaces.model.enums.ExamStateEnum;
import com.hfut.invigilate.interfaces.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    @Resource
    ExamRepository examRepository;

    @Resource
    ExchangeRepository exchangeRepository;

    @Resource
    InvigilateRepository invigilateRepository;

    @Override
    public List<BadStateExam> listBadStateExam(){
        return examRepository.listBadStateExam();
    }

    @Override
    @Transactional
    public ServiceDTO delete(Long examCode){
        QueryWrapper<ExamInfoDO> queryExam=new QueryWrapper<>();
        queryExam.eq("code",examCode);
        boolean delete = examRepository.delete(queryExam);
        if(delete){
            //删除对应的监考
            QueryWrapper<InvigilateRecordDO> queryInvigilate=new QueryWrapper<>();
            queryInvigilate.eq("exam_code",examCode);

            List<Invigilate> invigilates = invigilateRepository.list(queryInvigilate);
            invigilateRepository.delete(queryInvigilate);

            QueryWrapper<ExchangeRecodeDO> queryExchange=new QueryWrapper<>();
            queryExchange.eq("exam_code",examCode);
            exchangeRepository.delete(queryExchange);

            for (Invigilate invigilate : invigilates) {
                QueryWrapper<ExchangeRecodeDO> query=new QueryWrapper<>();
                query.eq("exam_code",invigilate.getExamCode());
                exchangeRepository.delete(query);
            }

            return ServiceDTO.ok();
        }
        return ServiceDTO.err("失败");
    }

    @Override
    public PageDTO page(Integer page, Integer limit, ExamPageQueryDTO q){
        if(q==null){
            return examRepository.page(page,limit,null);
        }

        QueryWrapper<ExamInfoDO> query=new QueryWrapper<>();
        if(StringUtils.isNotBlank(q.getCode())){
            query.eq("code",q.getCode());
        }
        if(StringUtils.isNotBlank(q.getName())){
            query.like("name",q.getName());
        }
        if(q.getDate()!=null){
            query.eq("date", q.getDate());
        }
        if(StringUtils.isNotBlank(q.getAddress())){
            query.like("address",q.getAddress());
        }
        return examRepository.page(page,limit,query);

    }

    @Override
    public ServiceDTO save(ExamInfo examInfo){
        //判断是否有冲突(时间和地点)
        QueryWrapper<ExamInfoDO> query=new QueryWrapper<>();
        query.eq("date",examInfo.getDate());
        List<ExamInfo> examInfos = examRepository.list(query);
        for (ExamInfo info : examInfos) {
            if(info.getAddress().equals(examInfo.getAddress())
                    && info.getEndTime().isAfter(examInfo.getStartTime())
                        && info.getStartTime().isBefore(examInfo.getEndTime())){
                LogUtil.err("ExamServiceImpl_save","时间或地点冲突无法保存",new Object[]{});
                return ServiceDTO.err("时间和地点冲突无法保存",info);
            }
        }
        //可以正常保存
        boolean save = examRepository.save(examInfo);
        return ServiceDTO.back(save);

    }

    @Override
    @Transactional
    public ServiceDTO update(ExamInfo examInfo) {
        if(examInfo.getTeachingClass()==null){
            examInfo.setTeachingClass("无教学班信息");
        }

        LocalTime startTime = examInfo.getStartTime();
        LocalTime endTime = examInfo.getEndTime();
        //判断是否和其他考试有时间冲突
        QueryWrapper<ExamInfoDO> queryExam=new QueryWrapper<>();
        queryExam.eq("date",examInfo.getDate());
        List<ExamInfo> examInfos = examRepository.list(queryExam);

        for (ExamInfo info : examInfos) {
            if(info.getCode().equals(examInfo.getCode())){
                continue;
            }
            if(startTime.compareTo(info.getEndTime())<=0 && endTime.compareTo(info.getStartTime())>=0){
                return ServiceDTO.err("有时间冲突,你不能把考试修改为该时间", info);
            }
        }

        boolean b = examRepository.updateByCode(examInfo);
        if(b){
            //联级更新交换id
            QueryWrapper<ExchangeRecodeDO> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("exam_code",examInfo.getCode());
            exchangeRepository.update(ExchangeRecode.builder().examInfo(examInfo).build(), queryWrapper);
        }else {
            return ServiceDTO.err("考试id传递错误");
        }
        return ServiceDTO.ok();
    }

    @Override
    public ExamInfoDTO getExamInfo(Long examCode,String workId){
        ExamInfoDTO examInfoDTO = examRepository.getExamInfoDTO(examCode);

        QueryWrapper<InvigilateRecordDO> query=new QueryWrapper<>();
        query.eq("exam_code",examCode)
                .eq("work_id",workId);
        Invigilate invigilate = invigilateRepository.get(query);

        examInfoDTO.setInvigilateCode(String.valueOf(invigilate.getCode()));

        return examInfoDTO;
    }

    @Override
    public List<TeacherExamDTO> getInvigilate(String workId){
        LocalDate now = LocalDate.now();
        LocalDate thisWeek = now.minusDays(now.getDayOfWeek().getValue());//本周的前一天

        List<TeacherExamDTO> invigilates = examRepository.getInvigilate(workId, thisWeek);
        for (TeacherExamDTO teacherExamDTO : invigilates) {
            ExamStateEnum examStateEnum = teacherExamDTO.getExamStateEnum();//有状态的跳过
            if (examStateEnum == ExamStateEnum.TO_BE_CONFIRMED || examStateEnum == ExamStateEnum.TO_BE_REPLACED){
                continue;
            }
            //没有状态的按照本周之后的返回
            if(teacherExamDTO.getDate().isBefore(now)){
                teacherExamDTO.setExamStateEnum(ExamStateEnum.FINISHED);
            }else if(teacherExamDTO.getDate().isAfter(now)){
                teacherExamDTO.setExamStateEnum(ExamStateEnum.UNFINISHED);
            }else {
                teacherExamDTO.setExamStateEnum(ExamStateEnum.TODAY);
            }
        }
        return invigilates;
    }

}
