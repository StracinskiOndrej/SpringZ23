package com.example.springz23;

import com.example.springz23.storage.StorageFileNotFoundException;
import com.example.springz23.storage.StorageService;
import org.apache.tika.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.crypto.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Controller
@CrossOrigin
public class FileUploadController {
    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

//    @GetMapping("/")
//    public String listUploadedFiles(Model model) throws IOException {
//
//        model.addAttribute("files", storageService.loadAll().map(
//                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//                                "serveFile", path.getFileName().toString()).build().toUri().toString())
//                .collect(Collectors.toList()));
//
//        return "uploadForm";
//    }

//    @GetMapping("/files/{filename:.+}")
//    @ResponseBody
//    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
//
//        Resource file = storageService.loadAsResource(filename);
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//    }

    @PostMapping("/encrypt")
    public ResponseEntity<InputStreamResource> encryptFile(HttpServletResponse response, @RequestParam("file") MultipartFile file,
                                                           RedirectAttributes redirectAttributes) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        response.setContentType("multipart/text");
        try {
            storageService.store(file);
        }
        catch (Exception e){
            return ResponseEntity.
                    badRequest().body(new InputStreamResource(InputStream.nullInputStream()));
        }
        String keyStr = generateKey();
        InputStream keyStream = IOUtils.toInputStream(keyStr);
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        try (FileOutputStream out = new FileOutputStream( file.getName()+"_private.key")) {
            out.write(keyPair.getPrivate().getEncoded());
        }

        try (FileOutputStream out = new FileOutputStream( file.getName()+"_public.key")) {
            out.write(keyPair.getPublic().getEncoded());
        }
        Encrypt encrypt = new Encrypt(file, keyStr);

        encrypt.EncryptKey(keyStream,keyPair.getPrivate(), keyPair.getPublic().getEncoded().toString() );

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
        try {
            storageService.store(file);
        }
        catch (Exception e){
            return ResponseEntity.
                    badRequest().body(new InputStreamResource(InputStream.nullInputStream()));
        }
        Decrypt decrypt = new Decrypt(file, key);
        InputStream in = new FileInputStream(decrypt.getRealPath());
        return ResponseEntity.ok()
                .body(new InputStreamResource(in));
        //return "redirect:http://147.175.121.147/z23/index.html";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
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

}