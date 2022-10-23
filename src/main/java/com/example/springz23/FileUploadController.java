package com.example.springz23;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springz23.storage.StorageFileNotFoundException;
import com.example.springz23.storage.StorageService;

import javax.crypto.spec.SecretKeySpec;

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


    //Encryption
    @PostMapping("/")
    public MultipartFile handleEncryption(@RequestParam("file") MultipartFile file,
                                             @RequestParam("key") String key,
                                             RedirectAttributes redirectAttributes) throws Exception {
        final String ALGORITHM="RSA";
        System.out.println(key + "PAKO JE GAY");
        //Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        storageService.store(file);
        File file2 = new File(file.getName());
        System.out.println("MAME FILE");
        file.transferTo(file2);
        System.out.println("PRESLO TRANSFER");
        FileOutputStream f = CryptoUtils.encrypt(key, file2);
        System.out.printf("mars");
        //f.write(s.getBytes());
        System.out.printf("kokot");
        //storageService.store((MultipartFile) f);
        System.out.println("hello");
        return file;

        //return "redirect:http://147.175.121.147/z23/index.html";
    }

    //Decryption
    @PostMapping("/decrypt")
    public void handleDecryption(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        storageService.store(file);

        final String TRANSFORMATION="AES";

        // utils.decrypt()

        //return "redirect:http://147.175.121.147/z23/index.html";
    }

    @PostMapping("/vby")
    public void handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);

        //return "redirect:http://147.175.121.147/z23/index.html";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}