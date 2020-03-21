package com.jiaxintec.common.unique;

import java.security.SecureRandom;

/**
 * RandomUUID
 * Created By Jacky Zhang on 2018/4/16 下午8:23
 */
public class RandomUUID {
    private final static char[] _NORMAL_CHARS = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    private static final SecureRandom _SEEDS = new SecureRandom();
    private static final int _COUNT = _NORMAL_CHARS.length;
    private static final int _LENGTH = 16;

    private RandomUUID() {
    }

    public static String next() {
        byte[] bytes = new byte[_COUNT];
        _SEEDS.nextBytes(bytes);

        StringBuilder buf = new StringBuilder();

        for (int n = 0; n < _LENGTH; ++n) {
            if (n == 4 || n == 6 || n == 8 || n == 10)
                buf.append('-');

            int hex = bytes[n] & 255;
            buf.append(_NORMAL_CHARS[hex >> 4]);
            buf.append(_NORMAL_CHARS[hex & (_COUNT - 1)]);
        }

        return buf.toString();
    }

}
