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
    private int status;
    private int code;

    public HttpException(int status, int code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpException(int status, int code, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = code;
    }
}



