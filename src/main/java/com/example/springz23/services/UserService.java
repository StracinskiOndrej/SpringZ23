package com.example.springz23.services;

import com.example.springz23.db.UserAccount;
import com.example.springz23.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository repository;

    public void save(final UserAccount user) {
        repository.save(user);
    }

    public Optional<UserAccount> getUser(String id) {
        return repository.findById(id);
    }

}
