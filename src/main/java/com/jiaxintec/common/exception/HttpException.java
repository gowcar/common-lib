package com.jiaxintec.common.exception;

import lombok.Data;

/**
 * Class Name:  HttpException
 * Author:      Jacky Zhang
 * Create Time: 2019/6/17 下午5:14
 * Description:
 */
@Data
public class HttpException extends RuntimeException
{
    private int code;

    public HttpException(String message) {
        this(500, message);
    }

    public HttpException(String message, Throwable cause) {
        this(500, message, cause);
    }

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
    }

    public HttpException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}



