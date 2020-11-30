package com.shaun.useraccountauthentication.springsecurityauthorizationserver.util;

import java.util.Random;

public class PassUtil {

    public static final String PASS_CHARACTOR_SET = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

    public static String generatePass(int digit) {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        char[] c = PASS_CHARACTOR_SET.toCharArray();
        for (int k = 0; k <= 6; k++) {
            int index = random.nextInt(c.length);
            result.append(c[index]);
        }
        return result.toString();
    }

}
