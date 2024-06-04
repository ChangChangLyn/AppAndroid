package com.baitap.viettel.Model;

import android.graphics.Bitmap;

import java.sql.Blob;

public class UserModel {
    private String id;
    private String name, email, password;
    private Blob image;

    public UserModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserModel(String id, String name, String email, Blob image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
    }
    public UserModel(String email,String name, Blob image) {
        this.email = email;
        this.name = name;
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
}
