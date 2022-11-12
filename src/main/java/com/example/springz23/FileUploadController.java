package com.example.springz23;

import com.example.springz23.db.UserAccount;
import com.example.springz23.services.UserService;
import com.example.springz23.storage.StorageFileNotFoundException;
import com.example.springz23.storage.StorageService;
import com.example.springz23.utilities.Decrypt;
import com.example.springz23.utilities.Encrypt;
import com.example.springz23.utilities.Salt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value= "/")
public class FileUploadController {
    private final StorageService storageService;

    @Autowired
    private UserService userService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/register")
    public String register(@RequestParam(value = "name") String username,
                           @RequestParam(value = "password") String pw) throws NoSuchAlgorithmException {
        if (userService.getUser(username).isPresent()) {
            return "error"; // redirect to some error html
        } else {
            byte[] salt = Salt.getSalt();
            byte[] hash = Salt.getSaltedHash(pw, salt);
            UserAccount account = new UserAccount(username, salt, hash);
            userService.save(account);
            return "ok"; // redirect to some logged html
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam(value = "name") String username,
                              @RequestParam(value = "password") String pw) throws NoSuchAlgorithmException {

        if (userService.getUser(username).isPresent()) {
            Optional<UserAccount> user = userService.getUser(username);
            byte[] newSaltedHash = Salt.getSaltedHash(pw, user.get().getSalt());

            if (Arrays.equals(newSaltedHash, user.get().getSaltedHash())) {
                System.out.println("1");
                //return new RedirectView("http://147.175.121.147/z45/index.html");
                return "OK";
            } else {
                System.out.println("2");

                // Not correct password
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                //return new RedirectView("http://147.175.121.147/z45/login.html");
                return "wrong password";
            }
        } else {
            System.out.println("3");

            // Name not in db
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            //return new RedirectView("http://147.175.121.147/z45/login.html");
            return "wrong username";
        }
    }

    @PostMapping("/encrypt")
    public ResponseEntity<InputStreamResource> encryptFile(HttpServletResponse response, @RequestParam("file") MultipartFile file,
                                                           RedirectAttributes redirectAttributes) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        response.setContentType("multipart/text");
        String path = file.getOriginalFilename();

        File f = new File(path);
        f.mkdir();

        try (FileOutputStream out = new FileOutputStream( path+"/"+file.getName())) {
            out.write(file.getBytes());
        }


        Encrypt encrypt = new Encrypt(file, path);

        InputStream in = new FileInputStream(encrypt.getRealPath());
        return ResponseEntity.ok()
                .body(new InputStreamResource(in));
        //return "redirect:http://147.175.121.147/z23/index.html";
    }
    @GetMapping("/key")
    public ResponseEntity<InputStreamResource> getKey(HttpServletResponse response, RedirectAttributes redirectAttributes) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        response.setContentType("multipart/text");
        String tmp = Encrypt.getKeyPath();
        if(tmp==null || tmp.equals("") || tmp.equals(" "))
            return ResponseEntity.ok()
                    .body(new InputStreamResource(InputStream.nullInputStream()));
        InputStream in = new FileInputStream(Encrypt.getKeyPath());
        return ResponseEntity.ok()
                .body(new InputStreamResource(in));
        //return "redirect:http://147.175.121.147/z23/index.html";
    }

    @PostMapping("/decrypt")
    public ResponseEntity<InputStreamResource> decryptFile(@RequestParam("file") MultipartFile file, @RequestParam("key") MultipartFile key,
                                                           RedirectAttributes redirectAttributes) throws Exception {


        String path = file.getOriginalFilename();
        File f = new File(path);
        f.mkdir();
        Decrypt decrypt = new Decrypt(file, key, path);
        InputStream in = new FileInputStream(decrypt.getRealPath());
        return ResponseEntity.ok()
                .body(new InputStreamResource(in));
        //return "redirect:http://147.175.121.147/z23/index.html";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}