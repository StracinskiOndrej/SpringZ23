package com.example.springz23.db;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserAccount {
    @Id
    private String username;
    private byte[] salt;
    private byte[] saltedHash;

    public UserAccount(String username, byte[] salt, byte[] saltedHash) {
        this.username = username;
        this.salt = salt;
        this.saltedHash = saltedHash;
    }

    public UserAccount(String username, byte[] salt) {

    }

    public UserAccount() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getSaltedHash() {
        return saltedHash;
    }

    public void setSaltedHash(byte[] saltedHash) {
        this.saltedHash = saltedHash;
    }
}
