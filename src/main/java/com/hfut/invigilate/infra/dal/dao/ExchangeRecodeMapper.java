package com.hfut.invigilate.infra.dal.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.ExchangeExamDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.ExchangeInfoDTO;
import com.hfut.invigilate.infra.dal.DO.ExchangeRecodeDO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.IntendDTO;

import java.util.List;

/**
 * <p>
 * 交换意图 Mapper 接口
 * </p>
 *
 * @author 常珂洁
 * @since 2021-09-16
 */
public interface ExchangeRecodeMapper extends BaseMapper<ExchangeRecodeDO> {

    List<IntendDTO> listIntendByInvigilateCode(Long invigilateCode);

    int delByTargetCode(Long targetCode);

    List<ExchangeInfoDTO> listMyIntend(String workId);

}
