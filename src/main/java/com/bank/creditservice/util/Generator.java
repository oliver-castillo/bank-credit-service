package com.bank.creditservice.util;

import java.security.SecureRandom;
import java.util.Random;

public class Generator {
    public static String generateCardNumber() {
        final Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String generateCvv() {
        final Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
