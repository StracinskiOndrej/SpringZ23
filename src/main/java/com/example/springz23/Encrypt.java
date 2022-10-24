package com.example.springz23;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Encrypt {
    private final File encrypted;
    private static String key;
    public Encrypt(MultipartFile file, String keyStr) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {

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

    public void EncryptKey(InputStream keyStr, Key keyPriv, String keyOut) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {

        try {
            File encKey = new File("encKey.txt");
            CryptoUtils.encryptKey(keyPriv, keyStr, encKey);
        } catch (CryptoException e) {
            throw new RuntimeException(e);
        }
        outputKey(keyOut);
    }




    private void outputKey(String keyText) {
        System.out.println(keyText);

        try {
            key = "key_public.text";
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
