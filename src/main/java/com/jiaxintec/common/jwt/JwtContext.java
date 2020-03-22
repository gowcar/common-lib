package com.jiaxintec.common.jwt;

import java.util.HashMap;
import java.util.Map;

import static javafx.scene.input.KeyCode.T;

/**
 * Class Name: RContext
 * Author:      Jacky Zhang
 * Create Time: 2019-10-03 下午5:41
 * Description:
 */
public class JwtContext
{
    private static ThreadLocal<Map> ctx = new ThreadLocal();

    public static Jwt getJwt() {
        return (Jwt) holder().get("jwt");
    }

    public static void setJwt(Jwt jwt) {
        holder().put("jwt", jwt);
    }

    public static <T> T getAttribute(String key) {
        return (T) holder().get(key);
    }

    public static <T> void setAttribute(String key, T value) {
        holder().put(key, value);
    }

    public static void clear() {
        holder().clear();
    }

    private static Map holder() {
        Map attr = ctx.get();
        if (attr == null) {
            attr = new HashMap();
            ctx.set(attr);
        }
        return attr;
    }
}
