package com.example.springz23.services;

import com.example.springz23.db.OwnedStock;
import com.example.springz23.db.OwnedStockRepository;
import com.example.springz23.db.Stock;
import com.example.springz23.db.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnedStockService {

    @Autowired
    OwnedStockRepository repository;

    public void save(final OwnedStock ownedStock) {
        repository.save(ownedStock);
    }

    public Iterable<OwnedStock> getAllOwnedStock(){return repository.findAll();}
}
