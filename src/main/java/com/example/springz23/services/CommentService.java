package com.example.springz23.services;

import com.example.springz23.db.Comment;
import com.example.springz23.db.CommentRepository;
import com.example.springz23.db.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    CommentRepository repository;

    public void save(final Comment comment) {
        repository.save(comment);
    }
}
