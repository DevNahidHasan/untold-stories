package com.nahid.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.keygen.KeyGenerators;

@SpringBootApplication
public class UntoldStoriesApplication {

    public static void main(String[] args) {
        SpringApplication.run(UntoldStoriesApplication.class, args);

//        System.out.println(KeyGenerators.string().generateKey());
    }

}
