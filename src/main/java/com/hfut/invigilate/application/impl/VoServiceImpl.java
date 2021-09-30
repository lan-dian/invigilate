package com.hfut.invigilate.application.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.application.VoService;
import com.hfut.invigilate.infra.dal.DO.UserDO;
import com.hfut.invigilate.infra.dal.dao.UserMapper;
import com.hfut.invigilate.domain.user.vlaueobject.dto.UserDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VoServiceImpl implements VoService {

    @Resource
    UserMapper userMapper;

    @Override
    public List<UserDTO> getUser(){
        QueryWrapper<UserDO> query=new QueryWrapper<>();
        query.eq("name","叶同奇");

        return userMapper.list(null);
    }

}
