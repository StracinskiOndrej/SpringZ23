package com.example.springz23.services;

import com.example.springz23.db.Stock;
import com.example.springz23.db.StockRepository;
import com.example.springz23.db.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    @Autowired
    StockRepository repository;

    public void save(final Stock stock) {
        repository.save(stock);
    }
}
