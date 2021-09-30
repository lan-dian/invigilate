package com.hfut.invigilate.domain.invigilate.service;

import com.hfut.invigilate.domain.invigilate.entity.Invigilate;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.*;
import com.hfut.invigilate.interfaces.model.ServiceDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InvigilateService {


    ServiceDTO page(Integer page, Integer limit, InvigilateQueryDTO queryDTO);

    List<TeacherExamDTO> getTodayInvigilate(String workId);

    ServiceDTO confirm(Long exchangeCode, String workId);

    List<IntendDTO> listIntend(Long invigilateCode);

    List<MyInvigilateOnExchangeDTO> listMyInvigilateOnExchange(String workId);

    boolean exchange(String workId, Long invigilateCode, Long targetCode);

    boolean replace(String workId, Long invigilateCode);

    List<InvigilateExchangeDTO> listInvigilateExchanges(String workId);

    boolean startExchange(Long invigilateCode, String msg,String workId);

    boolean save(Invigilate invigilate);

    ServiceDTO add(String workId,Long examCode);

    @Transactional
    ServiceDTO delete(Long invigilateCode);
}
