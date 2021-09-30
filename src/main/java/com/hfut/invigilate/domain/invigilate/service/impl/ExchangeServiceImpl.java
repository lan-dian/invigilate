package com.hfut.invigilate.domain.invigilate.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.domain.invigilate.entity.ExchangeRecode;
import com.hfut.invigilate.domain.invigilate.entity.Invigilate;
import com.hfut.invigilate.domain.invigilate.repository.ExchangeRepository;
import com.hfut.invigilate.domain.invigilate.repository.InvigilateRepository;
import com.hfut.invigilate.domain.invigilate.service.ExchangeService;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.ExchangeInfoDTO;
import com.hfut.invigilate.infra.dal.DO.ExchangeRecodeDO;
import com.hfut.invigilate.infra.dal.DO.InvigilateRecordDO;
import com.hfut.invigilate.interfaces.model.ServiceDTO;
import com.hfut.invigilate.interfaces.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Resource
    ExchangeRepository exchangeRepository;

    @Resource
    InvigilateRepository invigilateRepository;

    @Override
    public List<ExchangeInfoDTO> listMyIntend(String workId){
        return exchangeRepository.listMyIntend(workId);
    }

    @Override
    @Transactional
    public boolean cancel(String workId,Long targetCode){
        Invigilate invigilate = invigilateRepository.get(targetCode);
        if(invigilate==null || invigilate.getState()==0){
            throw new IllegalArgumentException("请传递合法的考试编号");
        }

        if(!workId.equals(invigilate.getWorkId())){
            throw new IllegalArgumentException("你不能取消别人的调换申请");
        }

        //还原状态码
        //todo 有直接更新而且能更新空值的updateWrapper
        QueryWrapper<InvigilateRecordDO> queryInvigilate=new QueryWrapper<>();
        queryInvigilate.eq("code",targetCode);

        boolean update = invigilateRepository.update(Invigilate.builder().state(0).build(), queryInvigilate);

        if(update){
            QueryWrapper<ExchangeRecodeDO> queryExchange=new QueryWrapper<>();
            queryExchange.eq("target_code",targetCode);
            exchangeRepository.delete(queryExchange);
            return true;
        }
        LogUtil.err("ExchangeServiceImpl_cancel","考试状态更新失败",new Object[]{invigilate,workId,targetCode});
        return false;

    }

    @Override
    public ServiceDTO cancelIntend(String workId, Long exchangeCode){
        ExchangeRecode exchangeRecode = exchangeRepository.get(exchangeCode);
        if(exchangeRecode==null){
            return ServiceDTO.err("请传递合法的交换意图id");
        }
        if(!workId.equals(exchangeRecode.getWorkId())){
            return ServiceDTO.err("你不能取消别人的交换意图");
        }
        QueryWrapper<ExchangeRecodeDO> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("code",exchangeCode);
        boolean delete = exchangeRepository.delete(queryWrapper);
        return ServiceDTO.back(delete);
    }

}
