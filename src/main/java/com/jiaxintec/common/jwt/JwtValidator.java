package com.jiaxintec.common.jwt;

/**
 * Class Name:  JwtValidator
 * Author:      Jacky Zhang
 * Create Time: 2020-03-21 下午2:50
 * Description:
 */
public interface JwtValidator
{
    boolean validate(Jwt jwt);
}
