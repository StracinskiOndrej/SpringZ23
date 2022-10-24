package com.example.springz23;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class Encrypt {
    private final File encrypted;
    private static String key;
    public Encrypt(MultipartFile file) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        String keyStr = generateKey();
        try {
            Tika tika = new Tika();
            String type = tika.detect(file.getBytes());
            type = MimeTypes.getDefaultExt(type);
            this.encrypted =new File("encrypted."+type);
            CryptoUtils.encrypt(keyStr, file.getInputStream(), encrypted);
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
        System.out.println(keyText);

        try {
            key = "key_"+encrypted.getName();
            Files.writeString(Path.of(key), keyText);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getRealPath(){
        System.out.println(encrypted.getPath());
        return this.encrypted.getPath();
    }
    public static String getKeyPath(){
        return key;
    }
}
