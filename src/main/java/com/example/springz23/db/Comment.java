package com.example.springz23.db;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String stockId;

    private String content;

    public Comment() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment( String userId, String stockId, String content) {
        this.userId = userId;
        this.stockId = stockId;
        this.content = content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }



}
