package com.example.springz23.utilities;

import java.io.IOException;
import java.security.*;

public class MyKeyPairGenerator {
    public static PrivateKey aPrivate;
    public static PublicKey aPublic;


    public static void createKeys() throws NoSuchAlgorithmException, IOException {

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair kp = generator.generateKeyPair();
        aPrivate = kp.getPrivate();
        aPublic = kp.getPublic();

        System.out.println("Private key: " + aPrivate.getFormat());

        System.out.println("Public key: " + aPublic.getFormat());
    }
}