package com.jiaxintec.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Date;

/**
 * Class Name:  JwtUtils
 * Author:      Jacky Zhang
 * Create Time: 2019-10-02 上午4:54
 * Description:
 */
public class JwtUtils {
    private static String PRIVATE_KEY = "ACy5YqxZB1uWSwcVLSNLcA==";
    private static final Long EXPIRED = 30 * 24 * 60 * 60 * 1000L;

    public static String makeToken(Long uid, String content) {
        return JWT.create()
                .withIssuer("JIAXIN")
                .withExpiresAt(Date.from(Instant.ofEpochMilli(System.currentTimeMillis() + EXPIRED )))
                .withAudience(String.valueOf(uid))
                .withSubject("SUB")
                .withClaim("content", content)
                .sign(Algorithm.HMAC256(PRIVATE_KEY));
    }

    public static String makeToken(Long uid, String content, HttpServletResponse response) {
        String token = makeToken(uid, content);
        response.setHeader("token", token);
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
