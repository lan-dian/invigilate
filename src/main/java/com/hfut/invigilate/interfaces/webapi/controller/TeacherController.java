package com.hfut.invigilate.interfaces.webapi.controller;

import com.hfut.invigilate.domain.exam.service.ExamService;
import com.hfut.invigilate.domain.invigilate.service.ExchangeService;
import com.hfut.invigilate.domain.invigilate.service.InvigilateService;
import com.hfut.invigilate.domain.user.entity.User;
import com.hfut.invigilate.domain.user.service.UserService;
import com.hfut.invigilate.domain.user.vlaueobject.dto.UserInfoDTO;
import com.hfut.invigilate.interfaces.model.CommonResult;
import com.hfut.invigilate.interfaces.model.ServiceDTO;
import com.hfut.invigilate.interfaces.model.dto.TokenDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.InvigilateExchangeDTO;
import com.hfut.invigilate.interfaces.model.enums.ExamStateEnum;
import com.hfut.invigilate.interfaces.utils.TokenUtil;
import com.hfut.invigilate.domain.exam.valueobject.dto.ExamInfoDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.MyInvigilateOnExchangeDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.TeacherExamDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.IntendDTO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    ExamService examService;

    @Resource
    InvigilateService invigilateService;

    @Resource
    ExchangeService exchangeService;

    @Resource
    UserService userService;

    @Resource
    TokenUtil tokenUtil;

    @PostMapping("/password")
    @ApiOperation(value = "修改密码",notes = "首次登陆修改密码,不用传递初始密码,之后修改需要传递")
    public CommonResult changePassword(@RequestHeader String token,
                                       @RequestParam(defaultValue = "123456") String password,
                                       @RequestParam String newPassword){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();

        if(StringUtils.isBlank(newPassword)){
            return CommonResult.err("新密码不能为空");
        }
        if ("123456".equals(newPassword)){
            return CommonResult.err("新密码不能为初始密码");
        }
        if(newPassword.equals(password)){
            return CommonResult.err("新密码和原始密码不能相同");
        }
        //todo 密码强度验证

        ServiceDTO serviceDTO = userService.changePassword(workId, password, newPassword);
        return CommonResult.back(serviceDTO);
    }

    @GetMapping("/info")
    @ApiOperation("查看自己的个人信息")
    public CommonResult info(@RequestHeader String token){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();

        UserInfoDTO userInfoDTO = userService.userInfo(workId);

        return CommonResult.body(userInfoDTO);
    }

    @GetMapping("/invigilate")
    @ApiOperation("得到自己的监考信息")
    public CommonResult getInvigilate(@RequestHeader String token){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();

        List<TeacherExamDTO> invigilate = examService.getInvigilate(workId);
        return CommonResult.body(invigilate);
    }

    @GetMapping("/today")
    @ApiOperation("得到今天的监考信息")
    public CommonResult getTodayInvigilate(@RequestHeader String token){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();

        List<TeacherExamDTO> invigilate = invigilateService.getTodayInvigilate(workId);
        return CommonResult.body(invigilate);
    }

    @GetMapping("/test")
    @ApiOperation(value = "登陆测试接口(仅用于测试)")
    public CommonResult test(@RequestHeader String token){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();
        return CommonResult.body("恭喜你登陆成功"+workId);
    }

    @GetMapping("/unfinished")
    @ApiOperation("未完成")
    public CommonResult getUnFinished(@RequestHeader String token){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();

        List<TeacherExamDTO> invigilate = examService.getInvigilate(workId);
        invigilate.removeIf(next -> next.getExamStateEnum() == ExamStateEnum.FINISHED);

        return CommonResult.body(invigilate);

    }



    @GetMapping("/exam")
    @ApiOperation("得到考试的详细信息")
    public CommonResult getExam(@RequestParam Long examCode,@RequestHeader String token){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();
        //TODO token和examCode不一致会报空指针！！！

        ExamInfoDTO examInfo = examService.getExamInfo(examCode,workId);
        if (examInfo==null){
            return CommonResult.err("考试详细信息查询失败");
        }

        return CommonResult.body(examInfo);
    }

    @GetMapping("/my")
    @ApiOperation("查看我发起的调换申请")
    public CommonResult listMyExchange(@RequestHeader String token){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();
        List<MyInvigilateOnExchangeDTO> myInvigilateOnExchangeDTOS = invigilateService.listMyInvigilateOnExchange(workId);
        return CommonResult.body(myInvigilateOnExchangeDTOS);
    }

    @GetMapping("/list")
    @ApiOperation("列出其他人的调换申请")
    public CommonResult listInvigilateExchange(@RequestHeader String token) {
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();
        List<InvigilateExchangeDTO> invigilateExchangeDTOS = invigilateService.listInvigilateExchanges(workId);
        return CommonResult.body(invigilateExchangeDTOS);
    }

    @GetMapping("/list-intend")
    @ApiOperation("查看待确认调换的信息(从这里选一个确认和谁调换)")
    public CommonResult listIntend(@RequestParam Long invigilateCode){
        List<IntendDTO> intendDTOS = invigilateService.listIntend(invigilateCode);
        return CommonResult.body(intendDTOS);
    }

    @GetMapping("/cancel-intent")
    @ApiOperation("取消我的交换意图")
    public CommonResult cancelIntent(@RequestHeader String token,@RequestParam Long exchangeCode){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();

        ServiceDTO serviceDTO = exchangeService.cancelIntend(workId, exchangeCode);
        return CommonResult.back(serviceDTO);
    }


}
