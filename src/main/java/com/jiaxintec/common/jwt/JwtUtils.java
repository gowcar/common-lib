package com.jiaxintec.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * Class Name:  JwtUtils
 * Author:      Jacky Zhang
 * Create Time: 2019-10-02 上午4:54
 * Description:
 */
@Slf4j
public class JwtUtils {
    private static String PRIVATE_KEY = "ACy5YqxZB1uWSwcVLSNLcA==";
    private static final Long EXPIRED = 30 * 24 * 60 * 60 * 1000L;

    public static String makeToken(Long uid, String content, Map<String, String> attrs) {
        JWTCreator.Builder builder = JWT.create()
                .withIssuer("JIAXIN")
                .withExpiresAt(Date.from(Instant.ofEpochMilli(System.currentTimeMillis() + EXPIRED )))
                .withAudience(String.valueOf(uid))
                .withSubject("SUB")
                .withClaim("content", content);
        if (attrs != null && attrs.size() > 0) {
            for (Map.Entry<String, String> entry : attrs.entrySet()) {
                builder = builder.withClaim(entry.getKey(), entry.getValue());
            }
        }
        return builder.sign(Algorithm.HMAC256(PRIVATE_KEY));
    }

    public static String makeToken(Long uid, String content, Map<String, String> attrs, HttpServletResponse response) {
        String token = makeToken(uid, content, attrs);
        response.setHeader("Authorization", token);
        log.debug("设置了token : {}", token);
        return token;
    }

    public static boolean verify(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(PRIVATE_KEY)).build();
        try {
            jwtVerifier.verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
