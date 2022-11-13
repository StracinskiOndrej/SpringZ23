package com.example.springz23.services;

import com.example.springz23.db.SentFile;
import com.example.springz23.db.SentFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SentFileService {
    @Autowired
    SentFileRepository repository;

    public void save(final SentFile file) {
        repository.save(file);
    }

    public Optional<SentFile> getSentFile(Long id) {
        return repository.findById(id);
    }
    public List<SentFile> getSentFile(String reciever) {
        Iterable<SentFile> allSentFiles =  repository.findAll();
        List<SentFile> recieversFiles = new ArrayList<>();
        allSentFiles.forEach(sentFile -> {
            if(sentFile.getReciever().equals(reciever)){
                recieversFiles.add(sentFile);
            }
        });

        return recieversFiles;
    }
    public List<SentFile> getSentFileByName(String fileName) {
        Iterable<SentFile> allSentFiles =  repository.findAll();
        List<SentFile> files = new ArrayList<>();
        allSentFiles.forEach(sentFile -> {
            if(sentFile.getFileName().equals(fileName)){
                files.add(sentFile);
            }
        });

        return files;
    }

    public void deleteSentFile(Long id){
        repository.deleteById(id);
    }

}