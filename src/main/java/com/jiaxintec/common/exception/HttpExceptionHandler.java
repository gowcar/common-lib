package com.jiaxintec.common.exception;

import com.jiaxintec.common.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Class Name:  ExceptionHandler
 * Author:      Jacky Zhang
 * Create Time: 2019/7/1 下午8:48
 * Description:
 */
@Slf4j
@RestControllerAdvice
public class HttpExceptionHandler
{
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Response<Object>> handleHttpException(HttpException ex) {
        Throwable cause = ex;
        if (ex.getCause() != null) {
            cause = ex.getCause();
        }
        log.error(cause.getMessage());
        Response<Object> response = new Response<>();
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(ex.getCode()).body(response);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Response<Object>> handleServiceException(ServiceException ex) {
        Throwable cause = ex;
        if (ex.getCause() != null) {
            cause = ex.getCause();
        }
        log.error(cause.getMessage());
        Response<Object> response = new Response<>();
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(ex.getCode()).body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        Response<Object> response = new Response<>();
        response.setCode(500);
        response.setMessage("服务器错误，请联系管理员");
        return ResponseEntity.status(500).body(response);
    }
}
