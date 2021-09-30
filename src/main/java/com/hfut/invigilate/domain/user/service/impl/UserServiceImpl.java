package com.hfut.invigilate.domain.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.domain.user.entity.User;
import com.hfut.invigilate.domain.user.entity.UserRole;
import com.hfut.invigilate.domain.user.repository.UserRepository;
import com.hfut.invigilate.domain.user.service.UserService;
import com.hfut.invigilate.domain.user.vlaueobject.dto.UserInfoDTO;
import com.hfut.invigilate.domain.user.vlaueobject.dto.UserPageQueryDTO;
import com.hfut.invigilate.infra.dal.DO.UserDO;
import com.hfut.invigilate.interfaces.model.PageDTO;
import com.hfut.invigilate.interfaces.model.ServiceDTO;
import com.hfut.invigilate.interfaces.utils.LogUtil;
import com.hfut.invigilate.interfaces.utils.TokenUtil;
import jdk.nashorn.internal.parser.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepository userRepository;

    @Resource
    TokenUtil tokenUtil;

    @Override
    public PageDTO page(Integer page, Integer limit, UserPageQueryDTO queryDTO){
        QueryWrapper<UserDO> query=new QueryWrapper<>();
        if(queryDTO==null){
            query=null;
        }else {
            if(StringUtils.isNotBlank(queryDTO.getName())){
                query.like("U.name",queryDTO.getName());
            }
            if(StringUtils.isNotBlank(queryDTO.getWorkId())){
                query.like("U.work_id",queryDTO.getWorkId());
            }
            if(StringUtils.isNotBlank(queryDTO.getCollege())){
                query.like("U.college",queryDTO.getCollege());
            }
            Integer roleId = queryDTO.getRoleId();
            if(roleId!=null && (roleId==0 || roleId==1)) {
                query.eq("R.role",roleId);
            }
        }
        List<User> list = userRepository.list(query);
        long size = list.size();
        List<User> users;
        if(size>limit){
            users = list.subList((page - 1) * limit,page*limit);
        }else {
            users=list;
        }
        return PageDTO.build(size,users);
    }

    @Override
    public ServiceDTO changePassword(String workId, String password, String newPassword){
        //查找用户
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        String newPasswordMd5 = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        QueryWrapper<UserDO> query=new QueryWrapper<>();
        query.eq("work_id",workId)
                .eq("password",passwordMd5);

        boolean update = userRepository.update(User.builder().password(newPasswordMd5).build(), query);

        if(update){
           LogUtil.info("UserServiceImpl_changePassword","用户修改密码成功",new Object[]{workId,passwordMd5,newPasswordMd5});
            return ServiceDTO.ok();
        }else {
            LogUtil.err("UserServiceImpl_changePassword","原始密码错误",new Object[]{workId,passwordMd5});
            return ServiceDTO.err("原始密码错误");
        }
    }

    @Override
    public ServiceDTO login(String workId, String password, int role) {
        User user = null;

        if (workId.length() == 10) {//用户输入的是工号
            user = userRepository.get(workId);
        } else {//用户输入的是姓名
            QueryWrapper<UserDO> query = new QueryWrapper<>();
            query.eq("name", workId);
            user = userRepository.get(query);
        }

        if (user == null) {
            return ServiceDTO.err("用户不存在");
        }

        List<UserRole> roles = user.getRoles();
        for (UserRole userRole : roles) {
            if (userRole.getCode() == role) {
                String md5 = DigestUtils.md5DigestAsHex(password.getBytes());

                if (user.getPassword().equalsIgnoreCase(md5)) {//登陆成功
                    LogUtil.info("UserServiceImpl_login", "用户登陆成功", new Object[]{user, role, workId});

                    Map<String, String> map=new HashMap<>();
                    map.put("token",tokenUtil.getToken(user.getWorkId(),role));
                    return ServiceDTO.ok(map);
                } else {
                    LogUtil.info("UserServiceImpl_login", "用户密码错误", new Object[]{user, role, workId, password});
                    return ServiceDTO.err("密码错误");
                }
            }
        }
        LogUtil.info("UserServiceImpl_login", "用户未找到对应角色", new Object[]{user, role});
        return ServiceDTO.err("登陆角色错误");
    }

    @Override
    public boolean save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserInfoDTO userInfo(String workId) {
        User user = userRepository.get(workId);
        UserInfoDTO build = UserInfoDTO.builder()
                .college(user.getCollege())
                .name(user.getName())
                .telephone(user.getName())
                .workId(workId)
                .build();
        return build;
    }

    @Override
    public boolean changeWorkId(String workId, String newWorkId,String adminId) {
        boolean b = userRepository.changeWorkId(workId, newWorkId);
        if(b){
            LogUtil.info("UserServiceImpl_changeWorkId","用户工号修改成功",new Object[]{workId,newWorkId,adminId});
        }else {
            LogUtil.err("UserServiceImpl_changeWorkId","用户工号修改失败",new Object[]{workId,newWorkId,adminId});
        }
        return b;
    }

    @Override
    public User get(String workId) {
        return userRepository.get(workId);
    }

    @Override
    public boolean update(User user) {
        if(user.getPassword()!=null){
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        return userRepository.updateByWorkId(user);
    }

    @Override
    public boolean delete(String workId) {
        QueryWrapper<UserDO> query=new QueryWrapper<>();
        query.eq("U.work_id",workId);
        return userRepository.delete(query);
    }

}
