package com.hfut.invigilate.infra.dal.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.InvigilateQueryDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.invigilate_page.InvigilatePageDTO;
import com.hfut.invigilate.infra.dal.DO.InvigilateRecordDO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.MyInvigilateOnExchangeDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.InvigilateExchangeDTO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 监考表 Mapper 接口
 * </p>
 *
 * @author 常珂洁
 * @since 2021-09-15
 */
public interface InvigilateRecordMapper extends BaseMapper<InvigilateRecordDO> {


    List<InvigilateExchangeDTO> listInvigilateExchange(String workId);

    boolean replace(String workId,Long invigilateCode);

    List<MyInvigilateOnExchangeDTO> listMyExchange(String workId);

    boolean updateStateByCode(Integer state,Long code);

    List<InvigilatePageDTO> page(Integer pos, Integer limit, @Param("q")InvigilateQueryDTO queryDTO);

    Integer count(@Param("q")InvigilateQueryDTO queryDTO);

}
