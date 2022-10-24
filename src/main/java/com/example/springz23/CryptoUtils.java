package com.example.springz23;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

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

    public static void EncI(String path) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] m = Files.readAllBytes(Path.of(path+"/toEncrypt.txt"));

        //generovanie nahodneho symetrickeho kluca
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128); /* 128-bit AES */
        SecretKey sk = kgen.generateKey();


        //generovanie IV
        SecureRandom srandom = new SecureRandom();
        byte[] iv = new byte[16];
        srandom.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        //cipher mode enc dec spec
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, sk, new GCMParameterSpec(128, iv));
        //cipherMode => DECRYPT_MODE / ENCRYPT_MODE

        //encrypt message m => c2
        byte[] mEnc = cipher.doFinal(m);


        //nacitanie VKA zo suboru
        byte[] keyb = Files.readAllBytes(Paths. get(path+"/fKey.key"));
        X509EncodedKeySpec ks = new X509EncodedKeySpec(keyb);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey vka = kf.generatePublic(ks);

        //zasifrovanie sk pomocou vka => c1
        FileOutputStream out = new FileOutputStream(path+"/encrypted" + ".enc");
        {
            cipher = Cipher.getInstance("RSA/ECB/OAEPwithSHA-256andMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, vka); // Encrypt using A's public key
            byte[] c1 = cipher.doFinal(sk.getEncoded());
            out.write(c1); //zapisanie encrypt secure key do file
        }
        //zapisanie IV do suboru
        out.write(iv);

        //vytvorenie signature
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(sk);
        byte[] macResult = mac.doFinal(mEnc);

        //zapisanie c2 do suboru
        out.write(mEnc);

        out.close();
    }

}

