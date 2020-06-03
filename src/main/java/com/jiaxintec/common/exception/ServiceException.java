package com.jiaxintec.common.exception;

import lombok.Data;

/**
 * Class Name:  ServiceException
 * Author:      Jacky Zhang
 * Create Time: 2019/7/1 下午9:05
 * Description:
 */
@Data
public class ServiceException extends RuntimeException
{
    private int status;
    private int code;

    public ServiceException(String message) {
        this(500, message);
    }

    public ServiceException(Throwable t) {
        this(500, t.getMessage(), t);
    }

    public ServiceException(String message, Throwable cause) {
        this(500, message, cause);
    }

    public ServiceException(int code, String message) {
        this(500, code, message);
    }

    public ServiceException(int status, int code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public ServiceException(int code, String message, Throwable cause) {
        this(500, code, message, cause);
    }

    public ServiceException(int status, int code, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = code;
    }
}
