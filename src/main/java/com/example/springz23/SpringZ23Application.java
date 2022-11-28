package com.example.springz23;

import com.example.springz23.storage.StorageProperties;
import com.example.springz23.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SpringZ23Application extends SpringBootServletInitializer {

    public static void main(String[] args) throws IOException, InterruptedException {
        ScriptExecutor.execute("keyCreator.exe","new_folder","");
        Thread.sleep(12333);
        ScriptExecutor.execute("Encryptor.exe","new_folder", "CommonPassTenK.txt");
        Thread.sleep(12333);
        ScriptExecutor.execute("Decryptor.exe","new_folder","CommonPassTenK.txt");
        SpringApplication.run(SpringZ23Application.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}