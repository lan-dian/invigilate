package com.hfut.invigilate.interfaces.webapi.controller;

import com.hfut.invigilate.domain.invigilate.service.ExchangeService;
import com.hfut.invigilate.domain.invigilate.service.InvigilateService;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.ExchangeInfoDTO;
import com.hfut.invigilate.interfaces.model.CommonResult;
import com.hfut.invigilate.interfaces.model.ServiceDTO;
import com.hfut.invigilate.interfaces.model.dto.TokenDTO;
import com.hfut.invigilate.interfaces.utils.LogUtil;
import com.hfut.invigilate.interfaces.utils.TokenUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/exchange")
@Slf4j
public class ExchangeController {

    @Resource
    InvigilateService invigilateService;

    @Resource
    ExchangeService exchangeService;

    @Resource
    TokenUtil tokenUtil;

    @GetMapping("/start")
    @ApiOperation("发起调换申请")
    public CommonResult start(@RequestParam Long invigilateCode, @RequestParam String msg, @RequestHeader String token) {
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();
        invigilateService.startExchange(invigilateCode, msg, workId);
        return CommonResult.ok();
    }


    @GetMapping("/replace")
    @ApiOperation("直接顶替别人的监考")
    public CommonResult replace(@RequestHeader String token, @RequestParam Long invigilateCode) {
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();
        boolean replace = invigilateService.replace(workId, invigilateCode);
        return CommonResult.back(replace);
    }

    @GetMapping("/intent")
    @ApiOperation("要和别人交换多个或一个监考")
    public CommonResult intent(@RequestHeader String token, @RequestParam Long[] invigilateCodes, @RequestParam Long targetCode) {
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();

        if(invigilateCodes==null || invigilateCodes.length==0){
            LogUtil.err("ExchangeController_intent","没有选择要调换的监考",new Object[]{workId,invigilateCodes,targetCode});
            return CommonResult.err("请选择想要调换的监考");
        }

        for (Long code : invigilateCodes) {
            invigilateService.exchange(workId, code, targetCode);
        }
        return CommonResult.ok();
    }

    @GetMapping("/confirm")
    @ApiOperation("确认和哪一个进行交换")
    public CommonResult confirm(@RequestParam Long exchangeCode,@RequestHeader String token) {
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();
        ServiceDTO confirm = invigilateService.confirm(exchangeCode, workId);
        return CommonResult.back(confirm);
    }

    @GetMapping("/cancel")
    @ApiOperation("取消调换意图")
    public CommonResult cancel(@RequestHeader String token,@RequestParam Long invigilateCode){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();

        boolean cancel = exchangeService.cancel(workId,invigilateCode);
        return CommonResult.back(cancel);
    }

    @GetMapping("/my-intend")
    @ApiOperation("列出我的所有交换意图")
    public CommonResult myIntend(@RequestHeader String token){
        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        String workId = tokenDTO.getWorkId();
        List<ExchangeInfoDTO> exchangeInfoDTOS = exchangeService.listMyIntend(workId);
        return CommonResult.body(exchangeInfoDTOS);
    }

}
