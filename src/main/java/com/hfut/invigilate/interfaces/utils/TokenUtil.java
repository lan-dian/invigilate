package com.hfut.invigilate.interfaces.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hfut.invigilate.domain.user.entity.UserRole;
import com.hfut.invigilate.interfaces.model.dto.TokenDTO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtil {

    @Value("${token.privateKey}")
    private String privateKey;


    /**
     * 加密token.
     */
    public String getToken(String workId, Integer roleId) {
        String token = JWT.create()
                .withClaim("workId" ,workId)
                .withClaim("roleId", roleId)
                .sign(Algorithm.HMAC256(privateKey));
        return token;
    }

    /**
     * 解析token.
     */
    public TokenDTO parseToken(String token) {
        DecodedJWT decodedjwt = JWT.require(Algorithm.HMAC256(privateKey))
                .build().verify(token);
        Claim workId = decodedjwt.getClaim("workId");
        Claim roleId = decodedjwt.getClaim("roleId");
        return new TokenDTO(workId.asString(),roleId.asInt());
    }
}