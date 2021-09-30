package com.hfut.invigilate.domain.invigilate.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.ExchangeInfoDTO;
import com.hfut.invigilate.infra.dal.DO.ExchangeRecodeDO;
import com.hfut.invigilate.domain.invigilate.entity.ExchangeRecode;

import java.util.List;

public interface ExchangeRepository {

    List<ExchangeInfoDTO> listMyIntend(String workId);

    boolean delete(QueryWrapper<ExchangeRecodeDO> query);

    boolean update(ExchangeRecode exchangeRecode, QueryWrapper<ExchangeRecodeDO> query);

    boolean updateByCode(ExchangeRecode exchangeRecode);

    ExchangeRecode get(Long code);

    ExchangeRecode get(QueryWrapper<ExchangeRecodeDO> query);

    boolean save(ExchangeRecode exchangeRecode);

    List<ExchangeRecode> list(QueryWrapper<ExchangeRecodeDO> query);
}
