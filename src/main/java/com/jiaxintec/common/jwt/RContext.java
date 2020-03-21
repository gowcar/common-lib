package com.jiaxintec.common.jwt;

/**
 * Class Name: RContext
 * Author:      Jacky Zhang
 * Create Time: 2019-10-03 下午5:41
 * Description:
 */
public class RContext
{
    private static ThreadLocal context = new ThreadLocal();
    public static <T> T getInfo() {
        return (T) context.get();
    }

    public static <T> void setInfo(T info) {
        context.set(info);
    }

    public static void clearInfo() {
        context.remove();
    }
}
