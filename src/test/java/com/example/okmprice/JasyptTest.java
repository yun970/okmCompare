package com.example.okmprice;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

public class JasyptTest {
    @Test
    public void jasyptTest(){
        String value = "scc95953048!";
        String res = jasyptEncoding(value);
        System.out.printf(res);

    }

    private String jasyptEncoding(String value) {
        String key = "8E4557AC841A9E62A889D41C33F55";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWITHMD5ANDDES");
        pbeEnc.setPassword(key);
        return pbeEnc.encrypt(value);
    }
}
