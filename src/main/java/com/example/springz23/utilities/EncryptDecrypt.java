package com.example.springz23.utilities;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class EncryptDecrypt {

    public byte[] encode(byte[] toEncode, byte[] key) throws Exception {

        PublicKey publicKey = loadPublicKey(key);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(toEncode);
    }

    public byte[] decode(byte[] toDecode, byte[] key) throws Exception {

        PrivateKey privateKey = loadPrivateKey(key);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(toDecode);

    }

    public PublicKey loadPublicKey(byte[] bytes) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println(Base64.getEncoder().encodeToString(bytes));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytes);
        return keyFactory.generatePublic(publicKeySpec);
    }

    private PrivateKey loadPrivateKey(byte[] bytes) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }


}