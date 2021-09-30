package com.hfut.invigilate.domain.invigilate.service;

import com.hfut.invigilate.domain.invigilate.valueobject.dto.ExchangeInfoDTO;
import com.hfut.invigilate.interfaces.model.ServiceDTO;

import java.util.List;

public interface ExchangeService {
    List<ExchangeInfoDTO> listMyIntend(String workId);

    boolean cancel(String workId,Long targetCode);

    ServiceDTO cancelIntend(String workId, Long exchangeCode);
}
