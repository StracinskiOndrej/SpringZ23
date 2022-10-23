package com.example.springz23;

import org.springframework.web.multipart.MultipartFile;

import javax.crypto.*;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class Encrypt {

    public Encrypt(MultipartFile file) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        String keyStr = generateKey();
        try {
            CryptoUtils.encrypt(keyStr, file.getInputStream(), new File("encrypted.enc"));
        } catch (CryptoException e) {
            throw new RuntimeException(e);
        }
        outputKey(keyStr);
    }

    private String generateKey() {
        SecretKey secretKey = null;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // for example
            secretKey = keyGen.generateKey();
        } catch (Exception ignored) {
        }
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    private void outputKey(String keyText) {

        File output = new File("key");
        FileWriter writer = null;
        try {
            writer = new FileWriter(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.write(keyText);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
