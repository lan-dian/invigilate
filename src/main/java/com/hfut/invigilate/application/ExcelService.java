package com.hfut.invigilate.application;

import com.hfut.invigilate.interfaces.model.InvigilateInfoDTO;

import java.util.List;

public interface ExcelService {
    boolean loadExcelInSQl(List<InvigilateInfoDTO> invigilateInfoDTOS);
}
