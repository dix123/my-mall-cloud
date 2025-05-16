package com.my.mall.shortlink.util;

import cn.hutool.core.lang.hash.MurmurHash;

/**
 * @Author: haole
 * @Date: 2025/5/13
 **/
public class HashUtil {
    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = 62;

    public static String hashToBase62(String str) {
        int hash = MurmurHash.hash32(str);
        long unsignedValue = hash & 0xFFFFFFFFL;
        return convertToBase62(unsignedValue);
    }

    private static String convertToBase62(long num) {
        StringBuilder sb = new StringBuilder();
        while(num > 0) {
            int index = (int) (num % BASE);
            sb.append(BASE62_CHARS.charAt(index));
            num /= BASE;
        }
        return sb.reverse().toString();
    }
}
