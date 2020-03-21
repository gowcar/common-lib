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
    private int code;

    public ServiceException(String message) {
        this(500, message);
    }

    public ServiceException(Throwable cause) {
        this(500, cause.getMessage(), cause);
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}
