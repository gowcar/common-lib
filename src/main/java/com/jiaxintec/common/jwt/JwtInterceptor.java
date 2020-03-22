package com.jiaxintec.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jiaxintec.common.exception.HttpException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Class Name:  JwtInterceptor
 * Author:      Jacky Zhang
 * Create Time: 2020-03-21 下午1:17
 * Description:
 */
public class JwtInterceptor implements HandlerInterceptor
{
    private final JwtValidator validator;

    public JwtInterceptor() {
        this(null);
    }

    public JwtInterceptor(JwtValidator validator) {
        this.validator = validator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        Class clz = method.getDeclaringClass();
        PassThrough declaredAnnotation = method.getDeclaredAnnotation(PassThrough.class);
        if (declaredAnnotation != null) return true;
        if ( !clz.getName().endsWith("Controller") || !clz.getName().startsWith("com.jiaxintec")) {
            return true;
        }
        if (request.getRequestURL().toString().contains("swagger")) {
            return true;
        }

        String origin = request.getHeader(HttpHeaders.ORIGIN);
        String headers = request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
        if (origin != null) {
            response.addHeader("Access-Control-Allow-Origin", origin);
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, HEAD");
            if (headers != null) {
                response.addHeader("Access-Control-Allow-Headers", headers);
            }
            response.addHeader("Access-Control-Max-Age", "3600");
        }

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            throw new HttpException(401, "无效token，请登录");
        }

        if (!JwtUtils.verify(token)) {
            throw new HttpException(401, "token验证失败，请重新登录");
        }
        Jwt jwt = decode(token);
        if ((jwt.getExpiredAt()- System.currentTimeMillis()) < 0) {
            throw new HttpException(401, "token已经失效，请重新登录");
        }
        if ((jwt.getExpiredAt() - System.currentTimeMillis()) < 5 * 60 * 1000) {
            String newToken = JwtUtils.makeToken(jwt.getUid(), jwt.getContent(), response);
            jwt = decode(newToken);
        }
        JwtContext.setJwt(jwt);
        return validator != null ? validator.validate(jwt) : true;
    }

    private Jwt decode(String token) {
        try {
            DecodedJWT jwtData = JWT.decode(token);
            Jwt jwt = Jwt.builder()
                    .uid(Long.valueOf(jwtData.getAudience().get(0)))
                    .expiredAt(jwtData.getExpiresAt().getTime())
                    .content(jwtData.getClaim("content").asString())
                    .build();
            return jwt;
        } catch (JWTDecodeException j) {
            throw new HttpException(401, "token已经失效，请重新登录");
        }
    }
}
