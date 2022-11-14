package com.example.springz23.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserAccount {
    @Id
    private String username;
    private byte[] salt;
    private byte[] saltedHash;

    @Column(columnDefinition = "BLOB")
    private byte[] privateKey;
    @Column(columnDefinition = "BLOB")
    private byte[] publicKey;

    private Integer i = 1;

    private String name;
    private String lastName;
    private Double money;

    private String session;



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

    public UserAccount(String username, byte[] salt, byte[] saltedHash, String name, String lastName, byte[] pubKey, byte[] privKey) {
        this.username = username;
        this.salt = salt;
        this.saltedHash = saltedHash;
        this.name = name;
        this.lastName = lastName;
        this.money = 50.0;
        this.privateKey = privKey;
        this.publicKey = pubKey;
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

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }


    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }
}
