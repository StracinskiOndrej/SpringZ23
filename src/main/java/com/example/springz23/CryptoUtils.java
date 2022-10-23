package com.example.springz23;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * A utility class that encrypts or decrypts a file.
 * @author www.codejava.net
 *
 */
public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static void encrypt(String key, InputStream inputStream, File outputFile)
            throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputStream, outputFile);
    }

    public static void decrypt(String key, InputStream inputStream, File outputFile)
            throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, key, inputStream, outputFile);
    }

    private static void doCrypto(int cipherMode, String key, InputStream inputStream,
                                 File outputFile) throws CryptoException {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            //byte[] inputBytes = new byte[(int) inputFile.length()];

            byte[] inputBytes = new byte[16384];
            try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                while (inputStream.read(inputBytes) != -1) {
                    if(inputStream.available() < 16384){
                        outputStream.write(cipher.doFinal(inputBytes));
                    } else {
                        outputStream.write(cipher.update(inputBytes));
                    }
                }
                outputStream.close();
            }
            inputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IOException
                                        | IllegalArgumentException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }
}

