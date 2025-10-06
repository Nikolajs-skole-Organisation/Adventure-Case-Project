package com.example.backend.model;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class BookingCodeGenerator {
    private static final char[] ALPHABET = "ABCDEFGHIJKLMNPQRSTUVWXYZ23456789".toCharArray();
    private static final int LENGTH = 10;
    private final SecureRandom random = new SecureRandom();

    public String generate(){
        char[] buf = new char[LENGTH];
        for (int i = 0; i < LENGTH; i++){
            buf[i] = ALPHABET[random.nextInt(ALPHABET.length)];
        }
        return new String(buf);
    }
}
