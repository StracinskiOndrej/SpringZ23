package com.example.springz23.services;

import com.example.springz23.db.SentFile;
import com.example.springz23.db.SentFileRepository;
import com.example.springz23.db.UserAccount;
import com.example.springz23.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SentFileService {
    @Autowired
    SentFileRepository repository;

    public void save(final SentFile file) {
        repository.save(file);
    }


}