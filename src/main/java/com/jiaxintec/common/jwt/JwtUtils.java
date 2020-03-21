package com.jiaxintec.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

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

    public static final String LOGON_IP = "cIP";

    public static final String CODE = "code";

    public static final String AUTHENTI_CODE = "authentiCodeId";

    public static String getToken(Long uid, String uname, String logonIp) {
        Long expired = 30 * 60 * 1000L;
        return JWT.create()
                .withExpiresAt(Date.from(Instant.ofEpochMilli(System.currentTimeMillis() + expired)))
                .withAudience(String.valueOf(uid))
                .withSubject(uname)
                .withClaim(LOGON_IP, logonIp)
                .sign(Algorithm.HMAC256(PRIVATE_KEY));
    }

    public static String getTokenForMail(Long uid, String code, Long authentiCodeId) {
        Long expired = 10 * 60 * 1000L;
        return JWT.create()
                .withExpiresAt(Date.from(Instant.ofEpochMilli(System.currentTimeMillis() + expired)))
                .withAudience(String.valueOf(uid))
                .withClaim(CODE, code)
                .withClaim(AUTHENTI_CODE, authentiCodeId)
                .sign(Algorithm.HMAC256(PRIVATE_KEY));
    }
    public static String getToken(String issuer, Long uid, String uname, Long expired) {
        return JWT.create()
                .withIssuer(issuer)
                .withExpiresAt(Date.from(Instant.ofEpochMilli(System.currentTimeMillis()+expired)))
                .withAudience(String.valueOf(uid))
                .withSubject(uname)
                .sign(Algorithm.HMAC256(PRIVATE_KEY));
    }
    public static String getToken(String issuer, Long uid, String uname) {
        Long expired = 30 * 24 * 60 * 60 * 1000L;
        return JWT.create()
                .withIssuer(issuer)
                .withExpiresAt(Date.from(Instant.ofEpochMilli(System.currentTimeMillis()+expired)))
                .withAudience(String.valueOf(uid))
                .withSubject(uname)
                .sign(Algorithm.HMAC256(PRIVATE_KEY));
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
