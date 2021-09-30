package com.hfut.invigilate.interfaces.webapi.controller;

import com.hfut.invigilate.domain.invigilate.service.InvigilateService;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.InvigilateQueryDTO;
import com.hfut.invigilate.domain.invigilate.valueobject.dto.invigilate_page.InvigilatePageDTO;
import com.hfut.invigilate.interfaces.model.CommonResult;
import com.hfut.invigilate.interfaces.model.ServiceDTO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/invigilate")
public class InvigilateController {

    @Resource
    InvigilateService invigilateService;

    @GetMapping("/page")
    @ApiOperation("监考信息分页")
    public CommonResult page(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "15") Integer limit,
                             @RequestBody(required = false) InvigilateQueryDTO queryDTO){
        if(page<=0 || limit<=0){
            return CommonResult.err("请传入正确的分页信息");
        }
        ServiceDTO serviceDTO = invigilateService.page(page, limit, queryDTO);
        return CommonResult.back(serviceDTO);
    }

    @GetMapping("/add")
    @ApiOperation("添加监考信息")
    public CommonResult add(@RequestParam String workId,@RequestParam Long examCode){
        if(StringUtils.isBlank(workId) || workId.length()!=10){
            CommonResult.err("请传递合法的工号");
        }

        ServiceDTO add = invigilateService.add(workId, examCode);
        return CommonResult.back(add);
    }

    @GetMapping("/delete")
    @ApiOperation("删除监考信息")
    public CommonResult delete(@RequestParam Long invigilateCode){
        ServiceDTO delete = invigilateService.delete(invigilateCode);
        return CommonResult.back(delete);
    }

}
