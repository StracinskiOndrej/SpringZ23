package com.example.springz23.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Comment {
    @Id
    private Long id;
    private Long userId;
    private String stockId;

    @Column( columnDefinition = "LONGTEXT")
    private String content;

    public Comment() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public Comment(Long id, Long userId, String stockId, String content) {
        this.id = id;
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