package com.my.mall.shortlink.admin.tool;

import java.security.SecureRandom;

/**
 * @Author: haole
 * @Date: 2025/5/6
 **/
public class KeyGenerator {
    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final SecureRandom random = new SecureRandom();

    public static String generate(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length()  );
            builder.append(CHARACTERS.charAt(index));
        }
        return builder.toString();
    }
    public static String generate() {
        return generate(6);
    }
}
