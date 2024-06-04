package com.baitap.viettel;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.baitap.viettel.Model.UserModel;

import java.io.ByteArrayOutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    Context context;
    public static String databaseName = "Viettel.db";


    public DataBaseHelper(@Nullable Context context) {
        super(context,databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT,email TEXT, password TEXT,name TEXT, imguser BLOB )");
        db.execSQL("CREATE TABLE items(idItem INTEGER PRIMARY KEY AUTOINCREMENT,nameItem TEXT, imageItem BLOB, email TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS items");
    }
    public Boolean insertData(String email, String password, String name){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("name", name);
        long result = MyDatabase.insert("users", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ?", new String[]{email});
        if(cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
    /*public void updateProfile(String userEmail,String name, Bitmap imguser){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);

        // Chuyển đổi hình ảnh Bitmap thành byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imguser.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        contentValues.put("imguser", imgBytes);

        try {
            long result = db.update("users", contentValues, "email = ?", new String[]{userEmail});
            if (result == -1) {
                Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show();
                Log.e("DataBaseHelper", "Failed to update profile");
            } else {
                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                Log.d("DataBaseHelper", "Profile updated successfully");
            }
        } catch (Exception e) {
            Log.e("DataBaseHelper", "Error updating profile: " + e.getMessage());
        }
    }*/

    public void updatePassword(String userEmail, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        db.update("users", values, "email = ?", new String[]{String.valueOf(userEmail)});
    }
    public UserModel getUserEmail(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"email", "password"}, "email =?",
                new String[]{String.valueOf(userEmail)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            cursor.close();
            return new UserModel(email, password);
        } else {
            return null;
        }
    }


    public Cursor getUserData(String email) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
    }
}
