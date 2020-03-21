package com.jiaxintec.common.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class Name:  PassThrogh
 * Author:      Jacky Zhang
 * Create Time: 2020-03-21 下午2:26
 * Description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassThrough
{
}
