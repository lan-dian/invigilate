package com.hfut.invigilate.interfaces.webapi.controller;


import com.hfut.invigilate.application.ExcelService;
import com.hfut.invigilate.domain.exam.service.ExamService;
import com.hfut.invigilate.domain.exam.valueobject.dto.BadStateExam;
import com.hfut.invigilate.domain.user.service.UserService;
import com.hfut.invigilate.interfaces.model.InvigilateInfoDTO;
import com.hfut.invigilate.interfaces.model.dto.TokenDTO;
import com.hfut.invigilate.interfaces.utils.ExcelParseUtil;
import com.hfut.invigilate.interfaces.model.CommonResult;
import com.hfut.invigilate.interfaces.utils.TokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author 常珂洁
 * @since 2021-09-15
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    ExcelService excelService;

    @Resource
    UserService userService;

    @Resource
    ExamService examService;

    @Resource
    TokenUtil tokenUtil;

    @PostMapping("/upload/excel")
    @ApiOperation("管理员上传考试文件")
    public CommonResult uploadExcel(MultipartFile file){
        if(file.isEmpty()){
            return CommonResult.err("文件为空");
        }
        try {
            List<InvigilateInfoDTO> invigilateInfoDTOS = ExcelParseUtil.parseExcel(file.getInputStream());
            boolean b = excelService.loadExcelInSQl(invigilateInfoDTOS);
            if(b){
                return CommonResult.ok();
            }else{
                return CommonResult.err("数据保存出错");
            }
        } catch (IOException e) {
            return CommonResult.err("文件格式不合法");
        }
    }

    @GetMapping("/badExam")
    @ApiOperation("列出所有缺少人数的考试")
    public CommonResult listBadExam(){
        List<BadStateExam> badStateExams = examService.listBadStateExam();
        return CommonResult.body(badStateExams);
    }



}

