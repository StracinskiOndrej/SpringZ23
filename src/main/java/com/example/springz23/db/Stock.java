package com.example.springz23.db;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Stock {

    @Id
    private String id;

    private String name;

    private float currentPrice;

    private float currentTrend;
    private String description;

    public Stock(String id, String name, float currentPrice, float currentTrend, String description) {
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
        this.currentTrend = currentTrend;
        this.description = description;
    }

    public Stock() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public float getCurrentTrend() {
        return currentTrend;
    }

    public void setCurrentTrend(float currentTrend) {
        this.currentTrend = currentTrend;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }
}
