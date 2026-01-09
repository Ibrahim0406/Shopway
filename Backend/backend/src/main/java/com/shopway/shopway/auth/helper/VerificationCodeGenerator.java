package com.shopway.shopway.auth.helper;

import java.util.Random;

public class VerificationCodeGenerator {

    /*
        * Generiše nasumični verifikacioni kod od 6 cifara.
        *
        * @return verifikacioni kod kao String
        */
    public static String generateCode(){
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
