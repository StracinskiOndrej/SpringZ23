package com.example.springz23.utilities;

import com.example.springz23.CryptoException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

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
    public static void encryptKey(Key key, InputStream inputStream, File outputFile)
            throws CryptoException {
        doCryptoKey(Cipher.ENCRYPT_MODE, key, inputStream, outputFile);
    }

    public static void decrypt(String key, InputStream inputStream, File outputFile)
            throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, key, inputStream, outputFile);
    }

    public static void decryptByKey(Key key, InputStream inputStream, File outputFile)
            throws CryptoException {
        doCryptoKey(Cipher.DECRYPT_MODE, key, inputStream, outputFile);
    }
    public static void decryptKey(Key key, InputStream inputStream, File outputFile)
            throws CryptoException {
        doCryptoKey(Cipher.DECRYPT_MODE, key, inputStream, outputFile);
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
                int count = inputStream.read(inputBytes);

                while (count >= 0) {
                    outputStream.write(cipher.update(inputBytes, 0, count)); // HERE I WAS DOING doFinal() method

                    //AND HERE WAS THE BadPaddingExceotion -- the first pass in the while structure

                    count = inputStream.read(inputBytes);
                }
                outputStream.write(cipher.doFinal()); // AND I DID NOT HAD THIS LINE BEFORE
                outputStream.flush();
            }
            inputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IOException
                                        | IllegalArgumentException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }

    private static void doCryptoKey(int cipherMode, Key key, InputStream inputStream,
                                 File outputFile) throws CryptoException {
        try {
            Key secretKey = key;
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(cipherMode, secretKey);

            byte[] inputBytes = new byte[16384];
            try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                int count = inputStream.read(inputBytes);

                while (count >= 0) {
                    outputStream.write(cipher.update(inputBytes, 0, count));

                    count = inputStream.read(inputBytes);
                }
                outputStream.write(cipher.doFinal());
                outputStream.flush();
            }
            inputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IOException
                 | IllegalArgumentException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }
}

