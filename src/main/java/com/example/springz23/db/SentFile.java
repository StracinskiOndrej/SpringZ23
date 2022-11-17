package com.example.springz23.db;

import javax.persistence.*;

@Entity
public class SentFile {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String reciever;
    private String fileName;
    @Column(columnDefinition = "BLOB")
    private  byte[] privateKey;
    @Column(columnDefinition = "BLOB")
    private byte[] publicKey;

    public SentFile() {
    }

    public SentFile(String sender, String reciever, String fileName, byte[] privateKey, byte[] publicKey) {
        this.sender = sender;
        this.reciever = reciever;
        this.fileName = fileName;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
