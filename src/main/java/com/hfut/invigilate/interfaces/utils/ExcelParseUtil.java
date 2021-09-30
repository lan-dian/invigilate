package com.hfut.invigilate.interfaces.utils;

import com.hfut.invigilate.interfaces.model.InvigilateInfoDTO;
import com.hfut.invigilate.interfaces.model.dto.ClassInfoDTO;
import com.hfut.invigilate.interfaces.model.dto.TeacherDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ExcelParseUtil {

    private static final int maxTeacher=6;
    private static final Pattern addressNumPattern = Pattern.compile("(.*?)\\s\\((\\d+)\\)");
    private static final Pattern dateTimePattern = Pattern.compile("(\\d+年\\d+月(\\d+)日)[\\s\\S]*?(\\d+:\\d+)~(\\d+:\\d+)");//静态提取
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private static void setDateTime(InvigilateInfoDTO invigilateInfoDTO, String dateTime){
        Matcher matcher = dateTimePattern.matcher(dateTime);
        if (matcher.find()) {
            invigilateInfoDTO.setDate(LocalDate.parse(matcher.group(1),dateFormatter));
            //我也不知道为什么2没有,但谁在乎呢
            invigilateInfoDTO.setStartTime(LocalTime.parse(matcher.group(3),timeFormatter));
            invigilateInfoDTO.setEndTime(LocalTime.parse(matcher.group(4),timeFormatter));
        } else {
            log.error("ExcelParseHelper.setDateTime 工具类 解析Excel文件 正则提取失败 invigilateInfo={},dateTime={}", invigilateInfoDTO,dateTime);
        }
    }

    private static void setAddressNum(InvigilateInfoDTO invigilateInfoDTO, String addressNumber){
        Matcher matcher = addressNumPattern.matcher(addressNumber);
        if(matcher.find()){
            invigilateInfoDTO.setAddress(matcher.group(1));
            invigilateInfoDTO.setStudentNum(Integer.parseInt(matcher.group(2)));
        }else {
            log.error("ExcelParseHelper.setAddressNum 工具类 解析Excel文件 正则提取失败 invigilateInfo={},addressNumber={}", invigilateInfoDTO,addressNumber);
        }
    }

    @Deprecated
    private static String getWorkId(String name){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(name.getBytes(StandardCharsets.UTF_8));
        BigInteger bigInt = new BigInteger(1,md5.digest());
        return bigInt.toString(10).substring(0,10);
    }

    public static List<InvigilateInfoDTO> parseExcel(InputStream excel) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excel);
        List<InvigilateInfoDTO> result = new ArrayList<>();
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {//遍历每个表格
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            String datetime = null;
            boolean isSame;//不是同一场考试
            InvigilateInfoDTO invigilate=null;
            List<TeacherDTO> teacherDTOS;
            for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {//遍历每行
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);

                String addressAndStudentNumber = xssfRow.getCell(7).getStringCellValue();

                isSame = StringUtils.isBlank(addressAndStudentNumber); //如果考试地点为空，说明还是上一场考试

                String teacher1 = xssfRow.getCell(8).getStringCellValue();
                String teacher2 = xssfRow.getCell(9).getStringCellValue();

                String college=xssfRow.getCell(0).getStringCellValue();
                String code=xssfRow.getCell(1).getStringCellValue();
                String name = xssfRow.getCell(2).getStringCellValue();
                String description=xssfRow.getCell(3).getStringCellValue();
                xssfRow.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                String studentNum=xssfRow.getCell(4).getStringCellValue();
                String teacher=xssfRow.getCell(5).getStringCellValue().replace("\\n"," ");

                if (isSame) {//同一个考试
                    List<TeacherDTO> temp = invigilate.getTeachers();
                    temp.add(new TeacherDTO(teacher1,getWorkId(teacher1),getWorkId(teacher1),"学院"));
                    temp.add(new TeacherDTO(teacher2,getWorkId(teacher2),getWorkId(teacher2),"学院"));
                }else {//新的考试
                    invigilate=new InvigilateInfoDTO();
                    invigilate.setName(name);
                    ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                    invigilate.setClassInfo(classInfoDTO);
                    classInfoDTO.setCollege(college);
                    classInfoDTO.setCode(code);
                    classInfoDTO.setName(name);
                    classInfoDTO.setDescription(description);
                    classInfoDTO.setStudentNum(studentNum);
                    classInfoDTO.setTeachers(teacher);
                    result.add(invigilate);
                    teacherDTOS =new ArrayList<>(maxTeacher);
                    teacherDTOS.add(new TeacherDTO(teacher1,getWorkId(teacher1),getWorkId(teacher1),"学院"));
                    teacherDTOS.add(new TeacherDTO(teacher2,getWorkId(teacher2),getWorkId(teacher2),"学院"));
                    invigilate.setTeachers(teacherDTOS);
                    setAddressNum(invigilate,addressAndStudentNumber);
                }

                if (StringUtils.isBlank(xssfRow.getCell(6).getStringCellValue())) {//关于时间日期
                    assert datetime != null;
                    if(!isSame){//不是同一场考试,设置一下时间
                        setDateTime(invigilate,datetime);
                    }
                } else {//第一次读到日期，肯定是新考试
                    datetime = xssfRow.getCell(6).getStringCellValue();
                    setDateTime(invigilate,datetime);
                }
            }
        }
        return result;
    }

}