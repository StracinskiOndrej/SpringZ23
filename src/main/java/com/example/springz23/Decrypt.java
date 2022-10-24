package com.example.springz23;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Decrypt {
    private File decrypt;
    private File decryptKey;
    public Decrypt(MultipartFile file, MultipartFile keyFile) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        try {
            String key;
            Tika tika = new Tika();
            String type = tika.detect(file.getBytes());
            type = MimeTypes.getDefaultExt(type);
            byte[] encoded = keyFile.getBytes();
            key = new String(encoded);
            decrypt = new File("decrypted." + "txt");
            CryptoUtils.decrypt(key, file.getInputStream(), decrypt);
        } catch (CryptoException e) {
            throw new RuntimeException(e);
        }
    }

    public void DecryptByKey(MultipartFile file, Key key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        try {
            decrypt = new File("decrypted.txt");
            CryptoUtils.decryptByKey(key, file.getInputStream(), decrypt);
        } catch (CryptoException e) {
            throw new RuntimeException(e);
        }
    }

    public Decrypt() {
    }

    public void DecryptKey(File file, Key key, String path) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        try {
            InputStream targetStream = new FileInputStream(file);
            this.decryptKey = new File(path+"/decryptedKey.key");
            CryptoUtils.decryptKey(key, targetStream, decryptKey);
        } catch (CryptoException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRealPath(){
        return decrypt.getAbsolutePath();
    }
    public String getKeyPath(){
        return decryptKey.getAbsolutePath();
    }
}
