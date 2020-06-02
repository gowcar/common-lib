package com.jiaxintec.common.exception;

import lombok.Data;

/**
 * Class Name:  HttpException
 * Author:      Jacky Zhang
 * Create Time: 2019/6/17 下午5:14
 * Description:
 */
@Data
public class Http400Exception extends HttpException
{
    private int status;
    private int code;

    public Http400Exception(String message) {
        this(400, message);
    }

    public Http400Exception(String message, Throwable cause) {
        this(400, message, cause);
    }

    public Http400Exception(int code, String message) {
        super(400, code, message);
    }

    public Http400Exception(int code, String message, Throwable cause) {
        super(400, code, message, cause);
    }
}
