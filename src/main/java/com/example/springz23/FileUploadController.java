package com.example.springz23;

import com.example.springz23.db.SentFile;
import com.example.springz23.db.UserAccount;
import com.example.springz23.services.SentFileService;
import com.example.springz23.services.UserService;
import com.example.springz23.storage.StorageFileNotFoundException;
import com.example.springz23.storage.StorageService;
import com.example.springz23.utilities.Encrypt;
import com.example.springz23.utilities.EncryptDecrypt;
import com.example.springz23.utilities.MyKeyPairGenerator;
import com.example.springz23.utilities.Salt;
import org.passay.*;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(value= "/")
public class FileUploadController {

    @Autowired
    private UserService userService;

    @Autowired
    private SentFileService sentFileService;

    @Autowired
    public FileUploadController(StorageService storageService) {
    }

    @PostMapping("/name")
    public String getName(@RequestParam(value = "user") String user
    ){

        if (userService.getUser(user).isPresent()) {

            Optional<UserAccount> account = userService.getUser(user);
            return account.get().getName();

        } else {
            return "Receiver username not found.";
        }
    }
    @PostMapping ("/lastname")
    public String getLastName(@RequestParam(value = "user") String user
    ) throws NoSuchAlgorithmException {

        if (userService.getUser(user).isPresent()) {

            Optional<UserAccount> account = userService.getUser(user);
            return account.get().getLastName();

        } else {
            return "Receiver username not found.";
        }
    }
    @PostMapping("/money")
    public String getMoney(@RequestParam(value = "user") String user
    ) throws NoSuchAlgorithmException {

        if (userService.getUser(user).isPresent()) {

            Optional<UserAccount> account = userService.getUser(user);
            Double money = account.get().getMoney();
            return money.toString();

        } else {
            return "Receiver username not found.";
        }
    }
    @PostMapping("/send")
    public String send(@RequestParam(value = "receiver") String receiver,
                           @RequestParam(value = "sender") String sender,
                           @RequestParam(value = "amount") Integer amount
                           ) throws NoSuchAlgorithmException {

        if (userService.getUser(receiver).isPresent()) {

            Optional<UserAccount> senderAccount = userService.getUser(sender);
            Optional<UserAccount> receiverAccount = userService.getUser(receiver);
            Double senderMoney = senderAccount.get().getMoney();
            Double receiverMoney = receiverAccount.get().getMoney();

            if(senderMoney >= amount){

                senderMoney = senderMoney - amount;
                receiverMoney = receiverMoney + amount;
                senderAccount.get().setMoney(senderMoney);
                receiverAccount.get().setMoney(receiverMoney);

                userService.save(senderAccount.get());
                userService.save(receiverAccount.get());
                return "Money has been sent.";
            }
            else{
                return "You don't have enough money.";
            }
        } else {
            return "Receiver username not found.";
        }
    }
    @PostMapping("/register")
    public String register(@RequestParam(value = "userName") String username,
                           @RequestParam(value = "name") String name,
                           @RequestParam(value = "lastName") String lastName,
                           @RequestParam(value = "password") String pw
    ) throws NoSuchAlgorithmException, IOException {
        if (userService.getUser(username).isPresent()) {
            return "Username already exists"; // redirect to some error html
        } else {
            File file = new File("./CommonPassTenK.txt");
            InputStream commonPassStream = new FileInputStream(file);
            InputStreamReader r = new InputStreamReader(commonPassStream);
            Reader[] readers = new Reader[1];
            readers[0] = r;
            WordListDictionary wordListDictionary = new WordListDictionary(
                    WordLists.createFromReader(readers, false, new ArraysSort()));
            DictionaryRule dictionaryRule = new DictionaryRule(wordListDictionary);
            //DictionarySubstringRule dictionarySubstringRule = new DictionarySubstringRule(wordListDictionary);
            PasswordData passwordData = new PasswordData(pw);
            passwordData.setUsername(username);

            PasswordValidator passwordValidator = new PasswordValidator(
                    new UsernameRule(),
                    new CharacterRule(EnglishCharacterData.LowerCase, 1),
                    new CharacterRule(EnglishCharacterData.UpperCase, 1),
                    new CharacterRule(EnglishCharacterData.Digit, 1),
                    new LengthRule(8, 24),
                    dictionaryRule

            );
            RuleResult validate = passwordValidator.validate(passwordData);
            if(!validate.isValid()){
                String errorCodes = "";
                for(RuleResultDetail rrd : validate.getDetails()){
                    errorCodes += rrd.getErrorCode()+", ";
                }
                return errorCodes;
            }else {
                byte[] salt = Salt.getSalt();
                byte[] hash = Salt.getSaltedHash(pw, salt);
                MyKeyPairGenerator.createKeys();
                byte[] privKey = MyKeyPairGenerator.aPrivate.getEncoded();
                byte[] pubKey = MyKeyPairGenerator.aPublic.getEncoded();
                System.out.println("Private key: \n" +Base64.getEncoder().encodeToString(privKey));
                System.out.println("Public key: \n" +Base64.getEncoder().encodeToString(pubKey));
                UserAccount account = new UserAccount(username, salt, hash, name, lastName, Base64.getEncoder().encode(privKey), Base64.getEncoder().encode(pubKey));
                userService.save(account);
                return "User created"; // redirect to some logged html
            }
        }
    }
    @PostMapping("/logout")
    public String deleteCookie(@RequestParam(value = "id") String id) {
        if(userService.getUser(id).isPresent()){
            userService.getUser(id).get().setSession("");
            userService.save(userService.getUser(id).get());
            return "login.html";
        }
        else{
            return "index.html";
        }
    }

    @PostMapping("/cookie")
    public String readCookie(@RequestParam(value = "id") String id) {
        if(userService.getUser(id).isPresent()) {
            return userService.getUser(id).get().getSession();
        }
        return "wrong";
    }

    @PostMapping("/login")
    public String login(@RequestParam(value = "name") String username,
                                        @RequestParam(value = "password") String pw) throws NoSuchAlgorithmException {

        if (userService.getUser(username).isPresent()) {
            Optional<UserAccount> user = userService.getUser(username);
            byte[] newSaltedHash = Salt.getSaltedHash(pw, user.get().getSalt());
            Integer k;
            k = user.get().getI();
            try {
                Thread.sleep(k*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            user.get().setI(k*2);
            userService.save(user.get());

            if (Arrays.equals(newSaltedHash, user.get().getSaltedHash())) {
                System.out.println("1");
                user.get().setI(1);
                byte[] salt= user.get().getSalt();
                byte[] saltedName = Salt.getSaltedHash(username,salt);
                UUID uuid = UUID.nameUUIDFromBytes(saltedName);
                user.get().setSession(uuid.toString());
                //add a cookie to the response
                userService.save(user.get());
                //return new RedirectView("http://147.175.121.147/z45/index.html");
                return "OK";
            } else {
                System.out.println("2");
                // Not correct password
                //return new RedirectView("http://147.175.121.147/z45/login.html");
                return "Not correct password";
            }
        } else {
            System.out.println("3");
            // Name not in db
            //return new RedirectView("http://147.175.121.147/z45/login.html");
            return "Name not found";
        }
    }

    @PostMapping("/sendFile")
    public String encryptFile(HttpServletResponse response, @RequestParam("file") MultipartFile file,
                              @RequestParam("sender") String sender,
                              @RequestParam("reciever") String reciever) throws Exception {
        response.setContentType("multipart/text");
        String path = file.getOriginalFilename();

//        byte[] privateKey = userService.getUser(reciever).get().getPrivateKey();
//        byte[] publicKey = userService.getUser(reciever).get().getPublicKey();
        byte[] privateKey = "placeholder".getBytes();
        byte[] publicKey = "placeholder".getBytes();
        new File("./filesToSend").mkdir();
        File f = new File("./filesToSend/"+path);
//        EncryptDecrypt cryptoRSAUtil = new EncryptDecrypt();
//        byte[] encoded = cryptoRSAUtil.encode(file.getBytes(),Base64.getDecoder().decode(publicKey));
        try (FileOutputStream out = new FileOutputStream( "./filesToSend/"+path)) {
            out.write(file.getBytes());
        }
        SentFile sf = new SentFile(sender, reciever, path, privateKey, publicKey);
        sentFileService.save(sf);

        return "sent";
    }

    @GetMapping("/searchForRecievers/{searchString}")
    public List<String> getRecievers(@PathVariable("searchString") String searchString){
        Iterable<UserAccount> users = userService.getAllUsers();
        List<String> userNames = new ArrayList<>();
        users.forEach((element)  -> {
            String userName = element.getUsername();
            if(userName.startsWith(searchString)){
                userNames.add(element.getUsername());
            }
        });
        return userNames;
    }
    @GetMapping("/checkMassages/{reciever}")
    public  HashMap<Long, String> getMassages(@PathVariable("reciever") String reciever){
        List<SentFile> sf = sentFileService.getSentFile(reciever);
        HashMap<Long, String> idName = new HashMap<>();
        sf.forEach(sentFile -> idName.put(sentFile.getId(), sentFile.getFileName()));
        return idName;
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

    @PutMapping("/recieveFile/{id}")
    public ResponseEntity<InputStreamResource> decryptFile(@PathVariable("id") Long id) throws Exception {

        Optional<SentFile> sentFileO = sentFileService.getSentFile(id);
        if (sentFileO.isPresent()){
            SentFile sentFile = sentFileO.get();
            File f = new File("./filesToSend/"+sentFile.getFileName());

//            EncryptDecrypt cryptoRSAUtil = new EncryptDecrypt();
//            byte[] decoded = cryptoRSAUtil.decode(Files.readAllBytes(f.toPath()), Base64.getDecoder().decode(sentFile.getPrivateKey()));
//            List<SentFile> toDelete= sentFileService.getSentFileByName(sentFile.getFileName());
//            toDelete.forEach((sf) -> sentFileService.deleteSentFile(sf.getId()));
//            try (FileOutputStream out = new FileOutputStream( "./filesToSend/"+sentFile.getFileName())) {
//                out.write(decoded);
//            }
//            f = new File("./filesToSend/"+sentFile.getFileName());
            List<SentFile> toDelete= sentFileService.getSentFileByName(sentFile.getFileName());
            toDelete.forEach((sf) -> sentFileService.deleteSentFile(sf.getId()));
//            System.out.println(Base64.getEncoder().encodeToString(decoded));

            InputStream in = new FileInputStream(f);
            f.delete();
            return ResponseEntity.ok()
                    .body(new InputStreamResource(in));
        }
        return null;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}