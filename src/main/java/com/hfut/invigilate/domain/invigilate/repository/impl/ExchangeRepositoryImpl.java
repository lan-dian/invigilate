package com.hfut.invigilate.domain.invigilate.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.domain.exam.entity.ExamInfo;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.ExchangeInfoDTO;
import com.hfut.invigilate.infra.dal.DO.ExchangeRecodeDO;
import com.hfut.invigilate.infra.dal.dao.ExchangeRecodeMapper;
import com.hfut.invigilate.domain.invigilate.entity.ExchangeRecode;
import com.hfut.invigilate.domain.invigilate.repository.ExchangeRepository;
import com.hfut.invigilate.interfaces.utils.IdWorker;
import com.hfut.invigilate.interfaces.utils.LogUtil;
import org.springframework.boot.info.InfoProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeRepositoryImpl implements ExchangeRepository {

    @Resource
    ExchangeRecodeMapper exchangeRecodeMapper;

    @Resource
    IdWorker idWorker;


    @Override
    public List<ExchangeInfoDTO> listMyIntend(String workId){
        List<ExchangeInfoDTO> exchangeInfoDTOS = exchangeRecodeMapper.listMyIntend(workId);
        LogUtil.info("ExchangeRepositoryImpl_listMyIntend","获取自己的交换意图",new Object[]{workId,exchangeInfoDTOS});
        return exchangeInfoDTOS;

    }

    @Override
    public boolean delete(QueryWrapper<ExchangeRecodeDO> query) {
        int delete = exchangeRecodeMapper.delete(query);
        return delete > 0;
    }

    @Override
    public boolean update(ExchangeRecode exchangeRecode, QueryWrapper<ExchangeRecodeDO> query) {
        ExchangeRecodeDO exchangeRecodeDO = getExchangeRecodeDO(exchangeRecode);

        int update = exchangeRecodeMapper.update(exchangeRecodeDO, query);

        return update > 0;
    }

    private ExchangeRecodeDO getExchangeRecodeDO(ExchangeRecode exchangeRecode) {
        ExamInfo examInfo = exchangeRecode.getExamInfo();
        ExchangeRecodeDO exchangeRecodeDO = new ExchangeRecodeDO();
        exchangeRecodeDO.setGmtModified(LocalDateTime.now())
                .setWorkId(exchangeRecode.getWorkId())
                .setInvigilateCode(exchangeRecode.getInvigilateCode())
                .setTargetCode(exchangeRecode.getTargetCode())
                .setEndTime(LocalDateTime.of(examInfo.getDate(),examInfo.getEndTime()))
                .setStartTime(LocalDateTime.of(examInfo.getDate(),examInfo.getStartTime()))
                .setAddress(examInfo.getAddress())
                .setExamName(examInfo.getName())
                .setExchangeState(exchangeRecode.getExchangeState());
        return exchangeRecodeDO;
    }

    @Override
    public boolean updateByCode(ExchangeRecode exchangeRecode) {
        QueryWrapper<ExchangeRecodeDO> query = new QueryWrapper<>();
        query.eq("code", exchangeRecode.getCode());

        ExchangeRecodeDO exchangeRecodeDO = getExchangeRecodeDO(exchangeRecode);

        int update = exchangeRecodeMapper.update(exchangeRecodeDO, query);

        return update > 0;
    }

    @Override
    public ExchangeRecode get(Long code) {
        QueryWrapper<ExchangeRecodeDO> query = new QueryWrapper<>();
        query.eq("code",code);

        ExchangeRecodeDO exchangeRecodeDO = exchangeRecodeMapper.selectOne(query);

        if(exchangeRecodeDO==null){
            return null;
        }

        return getExchangeRecode(exchangeRecodeDO);
    }

    @Override
    public ExchangeRecode get(QueryWrapper<ExchangeRecodeDO> query) {

        ExchangeRecodeDO exchangeRecodeDO = exchangeRecodeMapper.selectOne(query);

        if(exchangeRecodeDO==null){
            return null;
        }

        return getExchangeRecode(exchangeRecodeDO);
    }

    @Override
    public boolean save(ExchangeRecode exchangeRecode) {
        exchangeRecode.setCode(idWorker.nextId());

        ExamInfo examInfo = exchangeRecode.getExamInfo();

        ExchangeRecodeDO exchangeRecodeDO = new ExchangeRecodeDO();
        exchangeRecodeDO.setGmtCreate(LocalDateTime.now())
                .setGmtModified(LocalDateTime.now())
                .setWorkId(exchangeRecode.getWorkId())
                .setInvigilateCode(exchangeRecode.getInvigilateCode())
                .setTargetCode(exchangeRecode.getTargetCode())
                .setCode(exchangeRecode.getCode())
                .setEndTime(LocalDateTime.of(examInfo.getDate(),examInfo.getEndTime()))
                .setStartTime(LocalDateTime.of(examInfo.getDate(),examInfo.getStartTime()))
                .setAddress(examInfo.getAddress())
                .setExamName(examInfo.getName())
                .setExamCode(examInfo.getCode())
                .setExchangeState(0);
        try {
            int insert = exchangeRecodeMapper.insert(exchangeRecodeDO);
            return insert > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<ExchangeRecode> list(QueryWrapper<ExchangeRecodeDO> query) {

        List<ExchangeRecodeDO> exchangeRecodeDOS = exchangeRecodeMapper.selectList(query);
        List<ExchangeRecode> exchangeRecodes = new ArrayList<>();

        if(exchangeRecodeDOS == null){
            return exchangeRecodes;
        }

        for (ExchangeRecodeDO exchangeRecodeDO : exchangeRecodeDOS) {
            ExchangeRecode build = getExchangeRecode(exchangeRecodeDO);

            if(build==null){
                continue;
            }

            exchangeRecodes.add(build);
        }

        return exchangeRecodes;
    }

    private ExchangeRecode getExchangeRecode(ExchangeRecodeDO exchangeRecodeDO) {
        ExamInfo examInfo = ExamInfo.builder()
                .name(exchangeRecodeDO.getExamName())
                .date(exchangeRecodeDO.getStartTime().toLocalDate())
                .startTime(exchangeRecodeDO.getStartTime().toLocalTime())
                .endTime(exchangeRecodeDO.getEndTime().toLocalTime())
                .address(exchangeRecodeDO.getAddress())
                .code(exchangeRecodeDO.getExamCode())
                .build();

        return ExchangeRecode.builder()
                .workId(exchangeRecodeDO.getWorkId())
                .invigilateCode(exchangeRecodeDO.getInvigilateCode())
                .exchangeState(exchangeRecodeDO.getExchangeState())
                .code(exchangeRecodeDO.getCode())
                .targetCode(exchangeRecodeDO.getTargetCode())
                .examInfo(examInfo)
                .build();
    }

}
