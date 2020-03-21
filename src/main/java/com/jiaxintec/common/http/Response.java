package com.jiaxintec.common.http;

import lombok.Data;

/**
 * Class Name:  Response
 * Author:      Jacky Zhang
 * Create Time: 2019/6/17 下午5:25
 * Description:
 */
@Data
public class Response<T> {
    private Integer code = 200;
    private String message = "SUCCESS";
    private T data;

    public Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response() {
    }

    public static Response success() {
        return new Response();
    }

    public static <T> Response success(T data) {
        Response<T> rtn = new Response<>();
        rtn.setData(data);
        return rtn;
    }

    public static <T> Response success(String message, T data) {
        Response<T> rtn = new Response<>();
        rtn.setData(data);
        rtn.setMessage(message);
        rtn.setData(data);
        return rtn;
    }

    public static Response fail(Integer code, String message) {
        Response rtn = new Response<>();
        rtn.setCode(code);
        rtn.setMessage(message);
        return rtn;
    }
}
