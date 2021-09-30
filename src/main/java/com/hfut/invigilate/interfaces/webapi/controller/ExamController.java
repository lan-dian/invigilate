package com.hfut.invigilate.interfaces.webapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.invigilate.domain.exam.entity.ExamInfo;
import com.hfut.invigilate.domain.exam.service.ExamService;
import com.hfut.invigilate.domain.exam.valueobject.dto.page.ExamPageQueryDTO;
import com.hfut.invigilate.interfaces.model.CommonResult;
import com.hfut.invigilate.interfaces.model.PageDTO;
import com.hfut.invigilate.interfaces.model.ServiceDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@Slf4j
@RequestMapping("/exam")
public class ExamController {

    @Resource
    ExamService examService;


    @GetMapping("/delete")
    @ApiOperation("删除考试")
    public CommonResult delete(@RequestParam Long examCode){
        ServiceDTO delete = examService.delete(examCode);
        return CommonResult.back(delete);
    }

    @PostMapping("/update")
    @ApiOperation("修改考试状态")
    public CommonResult update(@RequestBody ExamInfo examInfo){
        if(examInfo.getCode()==null){
            return CommonResult.err("必须传递考试id");
        }
        if(StringUtils.isBlank(examInfo.getName())){
            return CommonResult.err("请传递考试名称");
        }
        if(examInfo.getDate()==null){
            return CommonResult.err("请传递考试日期");
        }
        if(examInfo.getStartTime()==null || examInfo.getEndTime()==null){
            return CommonResult.err("请传递考试时间");
        }
        if(examInfo.getEndTime().isAfter(examInfo.getStartTime())){
            return CommonResult.err("结束时间必须晚于开始时间");
        }
        if(StringUtils.isBlank(examInfo.getAddress())){
            return CommonResult.err("请传递考试地点");
        }
        if(examInfo.getStudentNum()==null || examInfo.getStudentNum()<=0){
            return CommonResult.err("请传递合法的考生人数");
        }
        if(examInfo.getTeacherNum()==null || examInfo.getTeacherNum()<=0){
            return CommonResult.err("请传递合法的老师人数");
        }

        ServiceDTO update = examService.update(examInfo);
        return CommonResult.back(update);
    }

    @PostMapping("/add")
    @ApiOperation("添加新的考试")
    public CommonResult add(@RequestBody ExamInfo examInfo) {
        if (StringUtils.isBlank(examInfo.getName())) {
            return CommonResult.err("考试名次不能为空");
        }

        if (examInfo.getDate() == null || examInfo.getStartTime() == null || examInfo.getEndTime() == null) {
            return CommonResult.err("请输入与考试时间有关的信息");
        }

        LocalDate date = examInfo.getDate();
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        if (date.isBefore(nowDate)
                || (date.isEqual(nowDate) && nowTime.isAfter(examInfo.getStartTime()))) {
            return CommonResult.err("你不能增加已经过去时间的考试");
        }

        if (StringUtils.isBlank(examInfo.getAddress())) {
            return CommonResult.err("考试地点不能为空");
        }

        if (examInfo.getStudentNum() <= 0) {
            return CommonResult.err("请输入合法的考试人数");
        }

        if (examInfo.getTeacherNum() <= 0) {
            return CommonResult.err("请输入合法监考人数");
        }

        examInfo.setTeachingClass("新添加的考试");//TODO 改用新的对象解决这个问题

        ServiceDTO save = examService.save(examInfo);
        return CommonResult.back(save);
    }

    @PostMapping("/page")
    @ApiOperation("考试信息,分页查询")
    public CommonResult page(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "15") Integer limit,
                             @RequestBody(required = false) ExamPageQueryDTO query){
        if(page<=0 || limit<=0){
            return CommonResult.err("请传入合法的分页信息");
        }

        PageDTO iPage = examService.page(page, limit, query);
        return CommonResult.body(iPage);

    }


}
