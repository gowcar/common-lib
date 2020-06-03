package com.jiaxintec.common.exception;

import lombok.Data;

/**
 * Class Name:  HttpException
 * Author:      Jacky Zhang
 * Create Time: 2019/6/17 下午5:14
 * Description:
 */
@Data
public class Http500Exception extends HttpException
{
    public Http500Exception(String message) {
        this(500, message);
    }

    public Http500Exception(String message, Throwable cause) {
        this(500, message, cause);
    }

    public Http500Exception(int code, String message) {
        super(500, code, message);
    }

    public Http500Exception(int code, String message, Throwable cause) {
        super(500, code, message, cause);
    }
}



