package com.example.springz23.db;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserAccount {
    @Id
    private String username;
    private byte[] salt;
    private byte[] saltedHash;

    private Integer i = 1;

    private String name;
    private String lastName;
    private Double money;

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserAccount(String username, byte[] salt, byte[] saltedHash) {
        this.username = username;
        this.salt = salt;
        this.saltedHash = saltedHash;
    }

    public UserAccount(String username, byte[] salt, byte[] saltedHash, String name, String lastName) {
        this.username = username;
        this.salt = salt;
        this.saltedHash = saltedHash;
        this.name = name;
        this.lastName = lastName;
        this.money = 0.0;
    }

    public UserAccount(String username, byte[] salt) {

    }

    public UserAccount() {
    }


    public String getUsername() {
        return username;
    }
    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
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
