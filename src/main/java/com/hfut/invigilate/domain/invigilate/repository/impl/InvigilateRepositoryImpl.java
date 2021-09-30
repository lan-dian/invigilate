package com.hfut.invigilate.domain.invigilate.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.InvigilateQueryDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.invigilate_page.InvigilatePageDTO;
import com.hfut.invigilate.infra.dal.DO.InvigilateRecordDO;
import com.hfut.invigilate.infra.dal.dao.ExamInfoMapper;
import com.hfut.invigilate.infra.dal.dao.ExchangeRecodeMapper;
import com.hfut.invigilate.infra.dal.dao.InvigilateRecordMapper;
import com.hfut.invigilate.domain.invigilate.entity.Invigilate;
import com.hfut.invigilate.domain.invigilate.repository.InvigilateRepository;
import com.hfut.invigilate.interfaces.utils.IdWorker;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.MyInvigilateOnExchangeDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.IntendDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.InvigilateExchangeDTO;
import com.hfut.invigilate.interfaces.utils.LogUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvigilateRepositoryImpl implements InvigilateRepository {

    @Resource
    InvigilateRecordMapper invigilateRecordMapper;

    @Resource
    ExchangeRecodeMapper exchangeRecodeMapper;

    @Resource
    ExamInfoMapper examInfoMapper;

    @Resource
    IdWorker idWorker;

    @Override
    public List<InvigilatePageDTO> page(Integer page, Integer limit, InvigilateQueryDTO queryDTO){
        return invigilateRecordMapper.page((page - 1) * limit, limit, queryDTO);
    }

    @Override
    public Integer count(InvigilateQueryDTO queryDTO){
        return invigilateRecordMapper.count(queryDTO);
    }


    @Override
    public List<IntendDTO> listIntend(Long invigilateCode){
        return exchangeRecodeMapper.listIntendByInvigilateCode(invigilateCode);
    }

    @Override
    public List<MyInvigilateOnExchangeDTO> listMyInvigilateOnExchange(String workId){
        return invigilateRecordMapper.listMyExchange(workId);
    }


    @Override
    public boolean replace(String workId, Long invigilateCode){
        return invigilateRecordMapper.replace(workId, invigilateCode);
    }

    @Override
    public List<InvigilateExchangeDTO> listInvigilateExchanges(String workId){
        return invigilateRecordMapper.listInvigilateExchange(workId);
    }


    @Override
    public boolean save(Invigilate invigilate){
        invigilate.setCode(idWorker.nextId());

        InvigilateRecordDO invigilateRecordDO=new InvigilateRecordDO();
        invigilateRecordDO.setGmtCreate(LocalDateTime.now())
                .setGmtModified(LocalDateTime.now())
                .setWorkId(invigilate.getWorkId())
                .setExamCode(invigilate.getExamCode())
                .setState(invigilate.getState())
                .setGenerationMethod(invigilate.getGenerationMethod())
                .setCode(invigilate.getCode());
        try {
            invigilateRecordMapper.insert(invigilateRecordDO);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(QueryWrapper<InvigilateRecordDO> query) {
        int delete = invigilateRecordMapper.delete(query);

        return delete > 0;
    }

    @Override
    public boolean update(Invigilate invigilate, QueryWrapper<InvigilateRecordDO> query) {
        InvigilateRecordDO invigilateRecordDO = getInvigilateRecordDO(invigilate);

        int update = invigilateRecordMapper.update(invigilateRecordDO, query);

        return update > 0;
    }

    @Override
    public boolean updateByCode(Invigilate invigilate) {
        QueryWrapper<InvigilateRecordDO> query = new QueryWrapper<>();
        query.eq("code", invigilate.getCode());

        InvigilateRecordDO invigilateRecordDO = getInvigilateRecordDO(invigilate);

        int update = invigilateRecordMapper.update(invigilateRecordDO, query);

        return update > 0;
    }

    private InvigilateRecordDO getInvigilateRecordDO(Invigilate invigilate) {
        InvigilateRecordDO invigilateRecordDO = new InvigilateRecordDO();
        invigilateRecordDO.setGmtModified(LocalDateTime.now())
                .setState(invigilate.getState())
                .setGenerationMethod(invigilate.getGenerationMethod())
                .setMsg(invigilate.getMsg())
                .setWorkId(invigilate.getWorkId())
                .setExamCode(invigilate.getExamCode());
        return invigilateRecordDO;
    }

    @Override
    public Invigilate get(Long code) {
        QueryWrapper<InvigilateRecordDO> query = new QueryWrapper<>();
        query.eq("code",code);

        InvigilateRecordDO invigilateRecordDO = invigilateRecordMapper.selectOne(query);

        if(invigilateRecordDO==null){
            return null;
        }

        return getInvigilate(invigilateRecordDO);

    }

    private Invigilate getInvigilate(InvigilateRecordDO invigilateRecordDO) {
        if(invigilateRecordDO==null){
            LogUtil.err("InvigilateRepositoryImpl_getInvigilate","token泄露",new Object[]{});
            throw new IllegalArgumentException("你不能查看其他考试的详细信息");
        }

        return Invigilate.builder()
                .workId(invigilateRecordDO.getWorkId())
                .code(invigilateRecordDO.getCode())
                .examCode(invigilateRecordDO.getExamCode())
                .state(invigilateRecordDO.getState())
                .generationMethod(invigilateRecordDO.getGenerationMethod())
                .msg(invigilateRecordDO.getMsg())
                .build();
    }

    @Override
    public Invigilate get(QueryWrapper<InvigilateRecordDO> query) {
        InvigilateRecordDO invigilateRecordDO = invigilateRecordMapper.selectOne(query);
        if(invigilateRecordDO==null){
            return null;
        }
        return getInvigilate(invigilateRecordDO);
    }

    @Override
    public List<Invigilate> list(QueryWrapper<InvigilateRecordDO> query) {
        List<InvigilateRecordDO> invigilateRecordDOS = invigilateRecordMapper.selectList(query);
        List<Invigilate> invigilates = new ArrayList<>();

        if(invigilateRecordDOS == null){
            return invigilates;
        }

        for (InvigilateRecordDO invigilateRecordDO : invigilateRecordDOS) {
            Invigilate build = getInvigilate(invigilateRecordDO);
            if(build==null){
                continue;
            }
            invigilates.add(build);
        }

        return invigilates;
    }

}
