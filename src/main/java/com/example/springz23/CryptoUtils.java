package com.example.springz23;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils
{
    private static final String ALGORITHM="AES";
    private static final String TRANSFORMATION="AES";

    public static FileOutputStream encrypt(String key, File inputFile) throws Exception
    {
        System.out.println(key);
        System.out.println(key.getBytes());
        System.out.println("JEBEM SA DO RITI");
        File file = new File("output.txt");
        System.out.println("enc MARS");
        FileOutputStream f = doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, file);
        return f;
    }

    public static void decrypt(String key, File inputFile, File outputFile) throws Exception
    {
        //doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    private static FileOutputStream doCrypto(int cipherMode, String key, File inputFile, File outputFile) throws Exception
    {
        try{
            System.out.println("crypto1");
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            System.out.println(secretKey.toString());
            cipher.init(cipherMode, secretKey);
            System.out.println("crypto3");
            FileInputStream inputStream = new FileInputStream(inputFile);
            System.out.println("crypto4");
            byte[] inputBytes = new byte[(int)inputFile.length()];
            System.out.println("cryptokokot");
            inputStream.read(inputBytes);
            System.out.println("cryptokokotpica");
            byte[] outputBytes = cipher.doFinal(inputBytes);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);
            inputStream.close();
            outputStream.close();
            System.out.println("cryptokokotpicajebanica");
            return outputStream;
        }
        catch(NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException|BadPaddingException
                | IllegalBlockSizeException|IOException ex)
        {
            throw new Exception("Errorencrypting/decryptingfile"+ex.getMessage(),ex);
        }
    }
}