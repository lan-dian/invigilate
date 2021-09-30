package com.hfut.invigilate.interfaces.webapi.controller;

import com.hfut.invigilate.domain.user.entity.User;
import com.hfut.invigilate.domain.user.entity.UserRole;
import com.hfut.invigilate.domain.user.service.UserService;
import com.hfut.invigilate.domain.user.vlaueobject.dto.UserPageQueryDTO;
import com.hfut.invigilate.interfaces.model.CommonResult;
import com.hfut.invigilate.interfaces.model.PageDTO;
import com.hfut.invigilate.interfaces.model.dto.TokenDTO;
import com.hfut.invigilate.interfaces.utils.LogUtil;
import com.hfut.invigilate.interfaces.utils.TokenUtil;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    TokenUtil tokenUtil;


    /*private static final Set<Integer> availableRoles;

    static {
        availableRoles=new HashSet<>();
        availableRoles.add(0);
        availableRoles.add(1);
    }*/

    @PostMapping("/add")
    @ApiOperation("添加用户")
    public CommonResult addUser(@RequestBody User user){
        //todo 权限管理
        if(StringUtils.isBlank(user.getWorkId()) || user.getWorkId().length()!=10){
            return CommonResult.err("请输入合法的工号");
        }
        if(StringUtils.isBlank(user.getName())){
            return CommonResult.err("请输入姓名");
        }
        if(StringUtils.isBlank(user.getCollege())){
            return CommonResult.err("请输入学院");
        }
        if(user.getRoles().size()==0){
            return CommonResult.err("请至少选择一个角色");
        }
        //去重后排除额外元素
        /*List<UserRole> roles = user.getRoles();
        Set<Integer> roleIds=new HashSet<>();
        roles.forEach(role->roleIds.add(role.getCode()));
        if(!availableRoles.containsAll(roleIds)){
            LogUtil.err("UserController_addUser","角色选择错误",new Object[]{user});
            return CommonResult.err("请选择正确的角色");
        }*/
        //设置默认值
        if(StringUtils.isBlank(user.getTelephone())){
            user.setTelephone("暂时未填写");
        }
        if(StringUtils.isBlank(user.getPassword())){
            user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        }

        boolean save = userService.save(user);
        if(save){
            return CommonResult.ok();
        }else {
            return CommonResult.err("工号重复");
        }
    }

    @PostMapping("/page")
    @ApiOperation("分页查询")
    public CommonResult page(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "15") Integer limit,
                             @RequestBody(required = false) UserPageQueryDTO queryDTO){
        if(page<=0 || limit<=0){
            return CommonResult.err("请传入合法的分页信息");
        }
        PageDTO pageDTO = userService.page(page, limit, queryDTO);

        return CommonResult.body(pageDTO);
    }

    @GetMapping("/get")
    @ApiOperation("获取用户信息(修改时填充)")
    public CommonResult get(@RequestParam String workId){
        if(StringUtils.isBlank(workId) || workId.length()!=10){
            return CommonResult.err("请输入合法的工号");
        }

        User user = userService.get(workId);
        if(user==null){
            return CommonResult.err("用户不存在");
        }
        return CommonResult.body(user);
    }

    @PostMapping("/update")
    @ApiOperation("修改用户基本信息(不包括工号)")
    public CommonResult edit(@RequestBody User user){
        String workId = user.getWorkId();
        if(StringUtils.isBlank(workId) || workId.length()!=10){
            return CommonResult.err("必须传递合法的工号");
        }
        if(user.getRoles().size()==0){
            return CommonResult.err("请至少选择一个角色");
        }

        boolean update = userService.update(user);

        return CommonResult.back(update);
    }

    @GetMapping("/changeWorkId")
    @ApiOperation("修改用户工号")
    public CommonResult changeWorkId(@RequestHeader String token,@RequestParam String workId,@RequestParam String newWorkId){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String adminId = tokenDTO.getWorkId();

        boolean b = userService.changeWorkId(workId, newWorkId, adminId);
        return CommonResult.back(b);
    }

    @GetMapping("/delete")
    @ApiOperation("删除用户")
    public CommonResult delete(@RequestHeader String token,@RequestParam String workId){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String adminId = tokenDTO.getWorkId();

        if(StringUtils.isBlank(workId) || workId.length()!=10){
            return CommonResult.err("必须传递合法的工号");
        }

        boolean delete = userService.delete(workId);
        if(delete){
            LogUtil.info("UserController_delete","删除用户",new Object[]{workId,adminId});
            return CommonResult.ok();
        }else {
            LogUtil.err("UserController_delete","用户删除失败",new Object[]{workId,adminId});
            return CommonResult.err();
        }

    }

}
