package com.jiaxintec.common.jwt;

import lombok.Builder;
import lombok.Data;

/**
 * Class Name:  ContextInfo
 * Author:      Jacky Zhang
 * Create Time: 2020-03-21 下午2:50
 * Description:
 */
@Data
@Builder
public class Jwt
{
    Long uid;
    String content;
    Long expiredAt;
}
