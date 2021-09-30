package com.hfut.invigilate.domain.user.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.infra.dal.DO.UserDO;
import com.hfut.invigilate.infra.dal.DO.UserRoleDO;
import com.hfut.invigilate.infra.dal.dao.UserMapper;
import com.hfut.invigilate.infra.dal.dao.UserRoleMapper;
import com.hfut.invigilate.domain.user.entity.User;
import com.hfut.invigilate.domain.user.entity.UserRole;
import com.hfut.invigilate.domain.user.repository.UserRepository;
import com.hfut.invigilate.domain.user.vlaueobject.dto.UserDTO;
import com.hfut.invigilate.interfaces.model.dto.UserLoginDTO;
import com.hfut.invigilate.interfaces.utils.IdWorker;
import com.hfut.invigilate.interfaces.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    @Resource
    UserMapper userMapper;

    @Resource
    UserRoleMapper userRoleMapper;

    @Resource
    IdWorker idWorker;

    @Override
    public boolean changeWorkId(String workId, String newWorkId){
        return userMapper.updateWorkId(workId,newWorkId);
    }

    @Override
    public UserLoginDTO getUserLoginDTO(String workId, int role) {
        return userMapper.getUserByWorkIdAndRoleId(workId, role);
    }

    @Override
    public boolean delete(QueryWrapper<UserDO> query) {
        int delete = userMapper.delete(query);//联级删除包括用户角色
        //TODO 联机删除用户其他的数据,因为不影响使用，所以暂时没有删除
        return delete > 0;
    }

    @Override
    public boolean update(User user, QueryWrapper<UserDO> query) {
        //todo 模仿下面的代码，完成角色的自动更新
        UserDO userDO = new UserDO();
        userDO.setGmtModified(LocalDateTime.now())
                .setName(user.getName())
                .setPassword(user.getPassword())
                .setTelephone(user.getTelephone())
                .setCollege(user.getCollege());

        int update = userMapper.update(userDO, query);
        return update > 0;
    }

    @Override
    @Transactional
    public boolean updateByWorkId(User user) {
        QueryWrapper<UserDO> query = new QueryWrapper<>();
        query.eq("work_id", user.getWorkId());

        UserDO userDO = new UserDO();
        userDO.setTelephone(user.getTelephone())
                .setName(user.getName())
                .setCollege(user.getCollege())
                .setGmtModified(LocalDateTime.now());

        List<UserRole> roles = user.getRoles();
        Set<Integer> newRoleId=new HashSet<>();
        roles.forEach(R->newRoleId.add(R.getCode()));

        QueryWrapper<UserRoleDO> queryRole=new QueryWrapper<>();
        queryRole.eq("work_id",user.getWorkId());
        List<UserRoleDO> userRoleDOS = userRoleMapper.selectList(queryRole);

        Set<Integer> oldRoleId=new HashSet<>();
        userRoleDOS.forEach(R-> oldRoleId.add(R.getRole()));

        Set<Integer> add=new HashSet<>();//新增的角色
        add.addAll(newRoleId);
        add.removeAll(oldRoleId);

        Set<Integer> sub=new HashSet<>();//删除的角色
        sub.addAll(oldRoleId);
        sub.removeAll(newRoleId);

        for (Integer roleId : sub) {
            QueryWrapper<UserRoleDO> q=new QueryWrapper<>();
            q.eq("work_id",user.getWorkId())
                    .eq("role",roleId);
            userRoleMapper.delete(q);
        }

        LogUtil.info("UserRepositoryImpl_updateByWorkId","角色删除",new Object[]{user.getWorkId(),sub});

        for (Integer roleId : add) {
            UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setRole(roleId)
                    .setWorkId(user.getWorkId())
                    .setCode(idWorker.nextId())
                    .setGmtModified(LocalDateTime.now())
                    .setGmtCreate(LocalDateTime.now());
            userRoleMapper.insert(userRoleDO);
        }

        LogUtil.info("UserRepositoryImpl_updateByWorkId","角色新增",new Object[]{user.getWorkId(),add});

        int update = userMapper.update(userDO, query);
        return update > 0;
    }

    @Override
    public User get(String workId) {
        UserDTO userDTO = userMapper.getByWorkId(workId);

        return getUser(userDTO);
    }

    @Override
    public User get(QueryWrapper<UserDO> query) {
        UserDTO userDTO = userMapper.get(query);

        return getUser(userDTO);
    }

    private User getUser(UserDTO userDTO) {
        if(userDTO==null){
            return null;
        }
        List<Integer> roleCode = userDTO.getRoles();
        List<UserRole> roles = new ArrayList<>();
        roleCode.forEach(Code -> {
            roles.add(UserRole.getByCode(Code));
        });

        User build = User.builder()
                .college(userDTO.getCollege())
                .workId(userDTO.getWorkId())
                .name(userDTO.getName())
                .roles(roles)
                .telephone(userDTO.getTelephone())
                .password(userDTO.getPassword())
                .build();
        return build;
    }


    @Override
    @Transactional
    public boolean save(User user) {
        UserDO userDO = new UserDO();
        userDO.setGmtCreate(LocalDateTime.now())
                .setGmtModified(LocalDateTime.now())
                .setName(user.getName())
                .setWorkId(user.getWorkId())
                .setCollege(user.getCollege())
                .setTelephone(user.getTelephone());

        int insert = 0;
        try {
            insert = userMapper.insert(userDO);
        } catch (Exception e) {
            LogUtil.err("UserRepositoryImpl_save", "用户插入失败", new Object[]{user});
            return false;
        }

        if (insert > 0) {
            List<UserRole> roles = user.getRoles();//插入每个角色
            for (UserRole role : roles) {
                UserRoleDO userRoleDO = new UserRoleDO();
                userRoleDO.setGmtCreate(LocalDateTime.now())
                        .setGmtModified(LocalDateTime.now())
                        .setWorkId(user.getWorkId())
                        .setRole(role.getCode())
                        .setCode(idWorker.nextId());
                try {
                    userRoleMapper.insert(userRoleDO);
                } catch (Exception e) {
                    LogUtil.err("UserRepositoryImpl_save", "用户角色插入失败", new Object[]{roles});
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public List<User> list(QueryWrapper<UserDO> query) {
        List<User> users = new ArrayList<>();

        List<UserDTO> userDTOS = userMapper.list(query);
        for (UserDTO userDTO : userDTOS) {
            users.add(getUser(userDTO));
        }
        return users;
    }

}
