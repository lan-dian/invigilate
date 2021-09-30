package com.hfut.invigilate.domain.user.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.infra.dal.DO.UserDO;
import com.hfut.invigilate.domain.user.entity.User;
import com.hfut.invigilate.interfaces.model.dto.UserLoginDTO;

import java.util.List;


public interface UserRepository {

    boolean changeWorkId(String workId, String newWorkId);

    UserLoginDTO getUserLoginDTO(String workId, int role);

    boolean save(User user);

    boolean delete(QueryWrapper<UserDO> query);

    boolean update(User user, QueryWrapper<UserDO> query);

    boolean updateByWorkId(User user);

    User get(String workId);

    User get(QueryWrapper<UserDO> query);

    List<User> list(QueryWrapper<UserDO> query);

}
