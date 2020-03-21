package com.jiaxintec.common.unique;

import java.util.Random;

/**
 * Class Name:  RandomUtils
 * Author:      Jacky Zhang
 * Create Time: 2019/9/25 下午2:43
 * Description:
 */
public class RandomUtils
{
    public static String randomNumber(int length) {
        return random(length, 0, 0, false, true);
    }

    public static String randomString(int length) {
        return random(length, 0, 0, true, true);
    }

    private static String random(int length, int start, int end, boolean letters, boolean numbers) {
        int count = length;
        char[] chars = null;
        Random random = new Random();

        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        } else if (chars != null && chars.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        } else {
            if (start == 0 && end == 0) {
                if (chars != null) {
                    end = chars.length;
                } else if (!letters && !numbers) {
                    end = 2147483647;
                } else {
                    end = 123;
                    start = 32;
                }
            } else if (end <= start) {
                throw new IllegalArgumentException("Parameter end (" + end + ") must be greater than start (" + start + ")");
            }

            char[] buffer = new char[count];
            int gap = end - start;

            while(true) {
                while(true) {
                    while(count-- != 0) {
                        char ch;
                        if (chars == null) {
                            ch = (char)(random.nextInt(gap) + start);
                        } else {
                            ch = chars[random.nextInt(gap) + start];
                        }

                        if (letters && Character.isLetter(ch) || numbers && Character.isDigit(ch) || !letters && !numbers) {
                            if (ch >= '\udc00' && ch <= '\udfff') {
                                if (count == 0) {
                                    ++count;
                                } else {
                                    buffer[count] = ch;
                                    --count;
                                    buffer[count] = (char)('\ud800' + random.nextInt(128));
                                }
                            } else if (ch >= '\ud800' && ch <= '\udb7f') {
                                if (count == 0) {
                                    ++count;
                                } else {
                                    buffer[count] = (char)('\udc00' + random.nextInt(128));
                                    --count;
                                    buffer[count] = ch;
                                }
                            } else if (ch >= '\udb80' && ch <= '\udbff') {
                                ++count;
                            } else {
                                buffer[count] = ch;
                            }
                        } else {
                            ++count;
                        }
                    }

                    return new String(buffer);
                }
            }
        }
    }

    public static void main(String[] args) {
        for(int i = 0; i < 1000; i++) {
//            System.out.println(RandomUtils.randomString(5));
        }
        for(int i = 0; i < 1000; i++) {
            int len = 8;
            String fmt = "%0" + len + "d";
            System.out.println(String.format(fmt, Integer.parseInt(RandomUtils.randomNumber(8))));
        }
    }
}
