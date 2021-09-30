package com.hfut.invigilate.domain.invigilate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.domain.exam.entity.ExamInfo;
import com.hfut.invigilate.domain.exam.repository.ExamRepository;
import com.hfut.invigilate.domain.exam.valueobject.dto.ExamInfoDTO;
import com.hfut.invigilate.domain.invigilate.entity.ExchangeRecode;
import com.hfut.invigilate.domain.invigilate.entity.Invigilate;
import com.hfut.invigilate.domain.invigilate.repository.ExchangeRepository;
import com.hfut.invigilate.domain.invigilate.repository.InvigilateRepository;
import com.hfut.invigilate.domain.invigilate.service.InvigilateService;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.*;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.invigilate_page.InvigilatePageDTO;
import com.hfut.invigilate.domain.user.entity.User;
import com.hfut.invigilate.domain.user.entity.UserRole;
import com.hfut.invigilate.domain.user.repository.UserRepository;
import com.hfut.invigilate.infra.dal.DO.ExchangeRecodeDO;
import com.hfut.invigilate.infra.dal.DO.InvigilateRecordDO;
import com.hfut.invigilate.interfaces.model.ServiceDTO;
import com.hfut.invigilate.interfaces.model.enums.ExamStateEnum;
import com.hfut.invigilate.interfaces.utils.IdWorker;
import com.hfut.invigilate.interfaces.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.ws.RequestWrapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class InvigilateServiceImpl implements InvigilateService {

    @Resource
    InvigilateRepository invigilateRepository;

    @Resource
    ExamRepository examRepository;

    @Resource
    ExchangeRepository exchangeRepository;

    @Resource
    UserRepository userRepository;

    @Resource
    IdWorker idWorker;

    @Override
    public ServiceDTO page(Integer page, Integer limit, InvigilateQueryDTO queryDTO){
        Integer count = invigilateRepository.count(queryDTO);
        List<InvigilatePageDTO> data = invigilateRepository.page(page, limit, queryDTO);
        Map<String, Object> map=new HashMap<>();
        map.put("n",count);
        map.put("data",data);
        return ServiceDTO.ok(map);
    }

    @Override
    public List<TeacherExamDTO> getTodayInvigilate(String workId) {
        QueryWrapper<InvigilateRecordDO> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("IR.work_id",workId)
                .eq("EI.date",LocalDate.now());
        List<TeacherExamDTO> invigilates = examRepository.getInvigilate(queryWrapper);
        for (TeacherExamDTO invigilate : invigilates) {
            invigilate.setExamStateEnum(ExamStateEnum.TODAY);
        }
        return invigilates;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public ServiceDTO confirm(Long exchangeCode, String workId) {
        ExchangeRecode exchange = exchangeRepository.get(exchangeCode);
        if(exchange==null){
            LogUtil.err("InvigilateServiceImpl_confirm","交换id不合法",new Object[]{exchangeCode,workId});
            return ServiceDTO.err("请传递合法的交换Id");
        }
        //现在我是确认方
        Long targetCode = exchange.getTargetCode();

        Invigilate targetInvigilate = invigilateRepository.get(targetCode);
        if (!workId.equals(targetInvigilate.getWorkId())) {
            LogUtil.err("InvigilateServiceImpl_confirm","修改别人的数据",new Object[]{workId,exchange,exchangeCode});
            return ServiceDTO.err("你不能同意别人的调换申请");
        }

        //得到别人的考试信息
        Long invigilateCode = exchange.getInvigilateCode();
        ExamInfo otherExamInfo = exchange.getExamInfo();

        //得到自己的考试信息
        Long targetExamCode = targetInvigilate.getExamCode();
        ExamInfo myExamInfo = examRepository.get(targetExamCode);
        //时间冲突检查
        //别人的考试放在我这里，和我的考试有时间冲突，应该跳过准备监考的id！
        //列出自己的全部考试信息，应该只和对应的天数有关！
        QueryWrapper<InvigilateRecordDO> querySelf=new QueryWrapper<>();
        querySelf.eq("IR.work_id",workId)
                .eq("EI.date",otherExamInfo);
        List<TeacherExamDTO> selfExams = examRepository.getInvigilate(querySelf);
        for (TeacherExamDTO selfExam : selfExams) {
            if(selfExam.getExamCode().equals(String.valueOf(myExamInfo.getCode()))){
                continue;//准要交换的考试，跳过去，不影响
            }
            if ((selfExam.getStartTime().compareTo(otherExamInfo.getEndTime()) <= 0) &&
                    (selfExam.getEndTime().compareTo(otherExamInfo.getStartTime()) >= 0)) {
                LogUtil.err("InvigilateServiceImpl_confirm","考试时间冲突",new Object[]{selfExam,otherExamInfo});
                return ServiceDTO.err("你确认的考试时间和你已有的时间冲突");
            }
        }

        QueryWrapper<InvigilateRecordDO> queryOther=new QueryWrapper<>();
        queryOther.eq("IR.work_id",exchange.getWorkId())
                .eq("EI.date",selfExams);
        List<TeacherExamDTO> othersExams = examRepository.getInvigilate(queryOther);
        for (TeacherExamDTO othersExam : othersExams) {
            if(othersExam.getExamCode().equals(String.valueOf(otherExamInfo.getCode()))){
                continue;
            }
            if ((othersExam.getStartTime().compareTo(myExamInfo.getEndTime()) <= 0) &&
                    (othersExam.getEndTime().compareTo(myExamInfo.getStartTime()) >= 0)) {
                LogUtil.err("InvigilateServiceImpl_confirm","考试时间冲突",new Object[]{otherExamInfo,myExamInfo});
                return ServiceDTO.err("你确认的考试时间和你已有的时间冲突");
            }
        }

        //可以交换
        boolean a = exchangeInvigilate(workId, invigilateCode);
        boolean b = exchangeInvigilate(exchange.getWorkId(), targetCode);
        if (a && b) {//删除所有交换记录
            //把交换的记录码设置成1
            QueryWrapper<ExchangeRecodeDO> updateQuery=new QueryWrapper<>();
            updateQuery.eq("code",exchange.getCode());
            exchangeRepository.update(ExchangeRecode.builder().exchangeState(1).build(),updateQuery);

            //删除其他交换的记录
            QueryWrapper<ExchangeRecodeDO> deleteQuery=new QueryWrapper<>();
            deleteQuery.eq("target_code",targetInvigilate.getCode())
                    .eq("exchange_code",0);
            exchangeRepository.delete(deleteQuery);
        }
        return ServiceDTO.ok();
    }

    private boolean exchangeInvigilate(String workId,Long invigilateCode){
        QueryWrapper<InvigilateRecordDO> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("code",invigilateCode);
        return invigilateRepository.update(Invigilate.builder().workId(workId).state(0).build(), queryWrapper);
    }

    @Override
    public List<IntendDTO> listIntend(Long invigilateCode) {
        return invigilateRepository.listIntend(invigilateCode);
    }


    @Override
    public List<MyInvigilateOnExchangeDTO> listMyInvigilateOnExchange(String workId) {
        return invigilateRepository.listMyInvigilateOnExchange(workId);
    }

    @Override
    @Transactional
    public boolean exchange(String workId, Long invigilateCode, Long targetCode) {
        Invigilate invigilate = invigilateRepository.get(invigilateCode);
        if (invigilate == null) {
            throw new IllegalArgumentException("请传递正确的监考id");
        }

        Invigilate targetInvigilate = invigilateRepository.get(targetCode);
        if (targetInvigilate.getState() == 0) {
            throw new IllegalArgumentException("不能与未发起调度状态的监考发起交换");
        }

        //监考的考试必须是自己的
        if (!invigilate.getWorkId().equals(workId)) {
            throw new IllegalArgumentException("这不是你的监考，你无权发起调换申请");
        }

        Long examCode = invigilate.getExamCode();
        ExamInfo examInfo = examRepository.get(examCode);
        if (examInfo.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("这场监考已完毕，你不能把它作为想要交换的监考");
        } else if (examInfo.getDate().isEqual(LocalDate.now()) && examInfo.getStartTime().isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("今天的这场考试已经开始，你不能把它作为交换条件");
        }

        //构建交换记录
        ExchangeRecode exchangeRecode = ExchangeRecode.builder()
                .exchangeState(0)
                .invigilateCode(invigilateCode)
                .targetCode(targetCode)
                .workId(workId)
                .examInfo(examInfo)
                .build();

        boolean save = exchangeRepository.save(exchangeRecode);

        if (save) {//符合交换条件
            QueryWrapper<InvigilateRecordDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("code", targetCode);
            LogUtil.info("InvigilateServiceImpl_exchange", "发起监考调换意图", new Object[]{workId, invigilate, targetInvigilate});
            return invigilateRepository.update(Invigilate.builder().state(4).build(), queryWrapper);
        }
        return false;
    }

    @Override
    public boolean replace(String workId, Long invigilateCode) {
        Invigilate invigilate = invigilateRepository.get(invigilateCode);
        if (invigilate == null) {
            throw new IllegalArgumentException("请传递合法的监考id");
        }

        Integer state = invigilate.getState();
        if (state != 3 && state != 4) {
            throw new IllegalArgumentException("不能顶替未发起调换申请的监考");
        }

        Long examCode = invigilate.getExamCode();
        ExamInfoDTO examInfo = examRepository.getExamInfoDTO(examCode);
        LocalDate examDate = examInfo.getDate();
        //今天之前或者时间今天但是时间已过的考试不能顶替
        if (examDate.isBefore(LocalDate.now())
                || ((examDate.isEqual(LocalDate.now()) && examInfo.getStartTime().isBefore(LocalTime.now())))) {
            throw new IllegalArgumentException("不能顶替已经完成监考或者已经开始的监考");
        }

        //自己监考的时间和这个有冲突
        QueryWrapper<InvigilateRecordDO> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("IR.work_id",workId)
                .eq("EI.date",examDate);
        List<TeacherExamDTO> selfInvigilates = examRepository.getInvigilate(queryWrapper);

        for (TeacherExamDTO selfInvigilate : selfInvigilates) {
            if (selfInvigilate.getStartTime().isBefore(examInfo.getEndTime()) && selfInvigilate.getEndTime().isAfter(examInfo.getStartTime())) {
                throw new IllegalArgumentException("你的时间和这场考试发生冲突，不能交换");
            }
        }

        QueryWrapper<InvigilateRecordDO> query=new QueryWrapper<>();
        query.eq("code",invigilateCode);
        return invigilateRepository.update(Invigilate.builder().state(0).workId(workId).build(),query);
    }

    @Override
    public List<InvigilateExchangeDTO> listInvigilateExchanges(String workId) {
        return invigilateRepository.listInvigilateExchanges(workId);
    }

    @Override
    public boolean startExchange(Long invigilateCode, String msg, String workId) {
        //不能换别人的
        Invigilate invigilate = invigilateRepository.get(invigilateCode);
        if (!workId.equals(invigilate.getWorkId())) {
            LogUtil.err("InvigilateServiceImpl_startExchange", "修改别人的信息", new Object[]{invigilateCode, msg, workId});
            throw new IllegalArgumentException("你不能对别人的监考发起调度申请");
        }
        //不能调度已处于调度状态的
        Integer state = invigilate.getState();
        if (state == 3 || state == 4) {
            throw new IllegalArgumentException("你已对该考试发起调度申请，不要重复操作");
        }
        //只能调度今天及以后的
        ExamInfo examInfo = examRepository.get(invigilate.getExamCode());
        if (examInfo.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("你不能对已经监考完毕的考试发起调度申请");
        }
        QueryWrapper<InvigilateRecordDO> query = new QueryWrapper<>();
        query.eq("code", invigilateCode);
        LogUtil.info("InvigilateServiceImpl_startExchange", "发起调换申请", new Object[]{query, invigilate});
        return invigilateRepository.update(Invigilate.builder().state(3).msg(msg).build(), query);
    }

    @Override
    public boolean save(Invigilate invigilate) {
        return invigilateRepository.save(invigilate);
    }

    @Override
    public ServiceDTO add(String workId, Long examCode) {
        User user = userRepository.get(workId);
        if(user==null){
            LogUtil.err("InvigilateServiceImpl_add","用户不存在",new Object[]{workId});
            return ServiceDTO.err("用户不存在");
        }
        if(!user.getRoles().contains(UserRole.Teacher)){
            LogUtil.err("InvigilateServiceImpl_add","用户不是老师",new Object[]{workId,user});
            return ServiceDTO.err("用户不是老师");
        }

        ExamInfo examInfo = examRepository.get(examCode);
        if(examInfo==null){
            LogUtil.err("InvigilateServiceImpl_add","考试不存在",new Object[]{examCode});
            return ServiceDTO.err("考试不存在");
        }

        Integer teacherNum = examInfo.getTeacherNum();
        ExamInfoDTO examInfoDTO = examRepository.getExamInfoDTO(examCode);
        List<TeacherListDTO> teachers = examInfoDTO.getTeachers();
        if(teachers.size()>=teacherNum){
            LogUtil.err("InvigilateServiceImpl_add","教师人数已满",new Object[]{teacherNum,examInfoDTO});
            return ServiceDTO.err("教师人数已满不能添加");
        }

        for (TeacherListDTO teacher : teachers) {
            if(teacher.getWorkId().equals(workId)){
                LogUtil.err("InvigilateServiceImpl_add","教师已存在与监考中",new Object[]{workId,examInfo,examInfoDTO});
                return ServiceDTO.err("教师已存在与监考中");
            }
        }
        Invigilate build = Invigilate.builder()
                .state(0)
                .generationMethod(1)
                .msg(null)
                .examCode(examCode)
                .workId(workId)
                .build();
        boolean save = invigilateRepository.save(build);
        return ServiceDTO.back(save);
    }

    @Override
    @Transactional
    public ServiceDTO delete(Long invigilateCode){
        Invigilate invigilate = invigilateRepository.get(invigilateCode);
        if (invigilate==null){
            LogUtil.err("InvigilateServiceImpl_delete","要删除的监考不存在",new Object[]{invigilateCode});
            return ServiceDTO.err("要删除的监考不存在");
        }
        QueryWrapper<InvigilateRecordDO> queryInvigilate=new QueryWrapper<>();
        queryInvigilate.eq("code",invigilateCode);
        boolean delete = invigilateRepository.delete(queryInvigilate);

        QueryWrapper<ExchangeRecodeDO> queryExchange=new QueryWrapper<>();
        queryExchange.eq("target_code",invigilateCode)
                .or()
                .eq("invigilate_code",invigilateCode);

        exchangeRepository.delete(queryExchange);
        return ServiceDTO.back(delete);
    }

}
