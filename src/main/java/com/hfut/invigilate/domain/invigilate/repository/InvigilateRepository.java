package com.hfut.invigilate.domain.invigilate.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.*;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.invigilate_page.InvigilatePageDTO;
import com.hfut.invigilate.infra.dal.DO.InvigilateRecordDO;
import com.hfut.invigilate.domain.invigilate.entity.ExchangeRecode;
import com.hfut.invigilate.domain.invigilate.entity.Invigilate;

import java.time.LocalDate;
import java.util.List;

public interface InvigilateRepository {

    List<InvigilatePageDTO> page(Integer page, Integer limit, InvigilateQueryDTO queryDTO);

    Integer count(InvigilateQueryDTO queryDTO);

    List<IntendDTO> listIntend(Long invigilateCode);

    List<MyInvigilateOnExchangeDTO> listMyInvigilateOnExchange(String workId);

    boolean replace(String workId, Long invigilateCode);

    List<InvigilateExchangeDTO> listInvigilateExchanges(String workId);

    boolean save(Invigilate invigilate);

    boolean delete(QueryWrapper<InvigilateRecordDO> query);

    boolean update(Invigilate invigilate, QueryWrapper<InvigilateRecordDO> query);

    boolean updateByCode(Invigilate invigilate);

    Invigilate get(Long code);

    Invigilate get(QueryWrapper<InvigilateRecordDO> query);

    List<Invigilate> list(QueryWrapper<InvigilateRecordDO> query);


}
