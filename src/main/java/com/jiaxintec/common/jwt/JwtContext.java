package com.jiaxintec.common.jwt;

/**
 * Class Name: RContext
 * Author:      Jacky Zhang
 * Create Time: 2019-10-03 下午5:41
 * Description:
 */
public class JwtContext
{
    private static ThreadLocal context = new ThreadLocal();
    public static <T> T getJwt() {
        return (T) context.get();
    }

    public static <T> void setJwt(T jwt) {
        context.set(jwt);
    }

    public static void clear() {
        context.remove();
    }
}
