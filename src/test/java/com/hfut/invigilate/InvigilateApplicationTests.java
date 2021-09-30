package com.hfut.invigilate;

import com.hfut.invigilate.interfaces.model.dto.TokenDTO;
import com.hfut.invigilate.interfaces.utils.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class InvigilateApplicationTests {

    @Resource
    TokenUtil tokenUtil;

    @Test
    void login(){
        String token = tokenUtil.getToken("11111", 1);

        TokenDTO tokenDTO = tokenUtil.parseToken(token);
        System.out.println(tokenDTO.getWorkId());

    }



}
