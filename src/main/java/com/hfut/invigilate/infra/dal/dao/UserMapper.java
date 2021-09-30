package com.hfut.invigilate.infra.dal.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hfut.invigilate.infra.dal.DO.UserDO;
import com.hfut.invigilate.domain.user.vlaueobject.dto.UserDTO;
import com.hfut.invigilate.interfaces.model.dto.UserLoginDTO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author 常珂洁
 * @since 2021-09-15
 */
public interface UserMapper extends BaseMapper<UserDO> {

    UserLoginDTO getUserByWorkIdAndRoleId(String workId, int role);

    UserDTO getByWorkId(String workId);

    UserDTO get(@Param(Constants.WRAPPER) QueryWrapper<UserDO> query);

    List<UserDTO> list(@Param(Constants.WRAPPER) QueryWrapper<UserDO> query);

    // List<UserDTO> page(Integer pos,Integer limit, @Param(Constants.WRAPPER) QueryWrapper<UserDO> query);

    int delete(@Param(Constants.WRAPPER) QueryWrapper<UserDO> query);

    boolean updateWorkId(String workId,String newWorkId);

}