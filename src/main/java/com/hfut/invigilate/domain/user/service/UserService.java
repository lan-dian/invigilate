package com.hfut.invigilate.domain.user.service;

import com.hfut.invigilate.domain.user.entity.User;
import com.hfut.invigilate.domain.user.vlaueobject.dto.UserInfoDTO;
import com.hfut.invigilate.domain.user.vlaueobject.dto.UserPageQueryDTO;
import com.hfut.invigilate.interfaces.model.PageDTO;
import com.hfut.invigilate.interfaces.model.ServiceDTO;
import com.hfut.invigilate.interfaces.model.dto.UserLoginDTO;

public interface UserService {

    PageDTO page(Integer page, Integer limit, UserPageQueryDTO queryDTO);

    ServiceDTO changePassword(String workId, String password, String newPassword);

    ServiceDTO login(String workId, String password , int role);

    boolean save(User user);

    UserInfoDTO userInfo(String workId);

    boolean changeWorkId(String workId,String newWorkId,String adminId);

    User get(String workId);

    boolean update(User user);

    boolean delete(String workId);

}
