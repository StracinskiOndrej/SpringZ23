package com.example.springz23.utilities;

import com.example.springz23.CryptoException;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Decrypt {
    private final File decrypt;
    public Decrypt(MultipartFile file, MultipartFile keyFile, String path) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        try {
            String key;
            Tika tika = new Tika();
            String type = tika.detect(file.getBytes());
            System.out.println(type);
            type = MimeTypes.getDefaultExt(type);
            byte[] encoded = keyFile.getBytes();
            key = new String(encoded);
            decrypt = new File(path+"/decrypted." + "txt");
            CryptoUtils.decrypt(key, file.getInputStream(), decrypt);
        } catch (CryptoException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRealPath(){
        System.out.println(decrypt.getAbsolutePath());
        return decrypt.getAbsolutePath();
    }
}
