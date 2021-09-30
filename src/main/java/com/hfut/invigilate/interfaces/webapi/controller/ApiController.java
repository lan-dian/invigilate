package com.hfut.invigilate.interfaces.webapi.controller;

import com.hfut.invigilate.application.VoService;
import com.hfut.invigilate.domain.report.entity.Report;
import com.hfut.invigilate.domain.report.service.ReportService;
import com.hfut.invigilate.domain.user.service.UserService;
import com.hfut.invigilate.domain.user.vlaueobject.dto.UserDTO;
import com.hfut.invigilate.infra.redis.service.RedisService;
import com.hfut.invigilate.interfaces.model.CommonResult;
import com.hfut.invigilate.interfaces.model.ServiceDTO;
import com.hfut.invigilate.interfaces.model.dto.TokenDTO;
import com.hfut.invigilate.interfaces.model.dto.UserLoginDTO;
import com.hfut.invigilate.interfaces.utils.LogUtil;
import com.hfut.invigilate.interfaces.utils.TokenUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
public class ApiController {

    @Resource
    UserService userService;

    @Resource
    VoService voService;

    @Resource
    TokenUtil tokenUtil;

    @Resource
    ReportService reportService;

    @Resource
    RedisService redisService;

    @GetMapping("/read")
    @ApiOperation("读信息")
    public CommonResult read(@RequestHeader String token,@RequestParam Long[] reportCodes){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        Integer roleId = tokenDTO.getRoleId();
        String workId;
        if(roleId==1){
            workId="0";
        }else {
            workId=tokenDTO.getWorkId();
        }

        if(reportCodes==null || reportCodes.length==0){
            return CommonResult.err("请传递通知id");
        }

        for (Long reportCode : reportCodes) {
            redisService.readReport(workId,reportCode);
        }

        return CommonResult.ok();
    }


    @GetMapping("/unRead")
    @ApiOperation("得到未读信息")
    public CommonResult getUnRead(@RequestHeader String token){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        Integer roleId = tokenDTO.getRoleId();
        String workId;
        if(roleId==1){
            workId="0";
        }else {
            workId=tokenDTO.getWorkId();
        }
        Set<Integer> unRead = redisService.getUnRead(workId);
        return CommonResult.body(unRead);
    }

    @PostMapping("/report")
    @ApiOperation(value = "发布通知",notes = "code,sentTime不用传递,msg不传递,默认和title一样")
    public CommonResult report(@RequestBody Report report){
        if(StringUtils.isBlank(report.getWorkId())){
            return CommonResult.err("必须传递工号");
        }
        if(StringUtils.isBlank(report.getTitle())){
            return CommonResult.err("标题不能为空");
        }
        if(StringUtils.isBlank(report.getMsg())){
            report.setMsg(report.getTitle());
        }
        report.setSendTime(LocalDateTime.now());

        boolean add = reportService.add(report);
        return CommonResult.back(add);
    }


    @GetMapping("/information")
    @ApiOperation("得到全部通知信息")
    public CommonResult getInformation(@RequestHeader String token,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "15") Integer limit){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        Integer roleId = tokenDTO.getRoleId();
        String workId;
        if(roleId==1){
            workId="0";
        }else {
            workId=tokenDTO.getWorkId();
        }

        if(page<=0 || limit<=0){
            return CommonResult.err("请传入合法的考试信息");
        }

        ServiceDTO serviceDTO = reportService.page(page, limit, workId);
        return CommonResult.back(serviceDTO);
    }

    @GetMapping("/test")
    public CommonResult test(){

        List<UserDTO> user = voService.getUser();
        return CommonResult.body(user);

    }

    @PostMapping("/login")
    @ApiOperation("用户登陆")
    public CommonResult login(@RequestParam String workId, @RequestParam String password, @RequestParam(defaultValue = "0") Integer roleId) {
        //参数校验
        if(StringUtils.isBlank(workId)){
            return CommonResult.err("请输入姓名或工号");
        }
        if(StringUtils.isBlank(password)){
            return CommonResult.err("请输入密码");
        }
        if(roleId !=0 && roleId !=1){
            return CommonResult.err("角色Id不合法");
        }

        ServiceDTO login = userService.login(workId, password, roleId);
        return CommonResult.back(login);

    }

}
