package com.baitap.viettel.Model;

import android.graphics.Bitmap;
import android.widget.EditText;

public class ItemModel {
    private int id;
    private byte[] image;
    private String nameimg;
    private String email;

    public ItemModel(int id,byte[] image, String nameimg,String email) {
        this.id = id;
        this.image = image;
        this.nameimg = nameimg;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ItemModel(byte[] image, String nameimg,String email) {
        this.image = image;
        this.nameimg = nameimg;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getNameimg() {
        return nameimg;
    }

    public void setNameimg(String nameimg) {
        this.nameimg = nameimg;
    }

}
