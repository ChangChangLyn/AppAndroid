package com.baitap.viettel.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baitap.viettel.DataBaseHelper;
import com.baitap.viettel.Model.UserModel;
import com.baitap.viettel.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView editImage;
    private EditText etName;
    private Button btnSave, btnBack;
    private DataBaseHelper dataBaseHelper;
    private Bitmap selectedBitmap = null;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dataBaseHelper = new DataBaseHelper(this);
        editImage = findViewById(R.id.editImage);
        etName = findViewById(R.id.etName);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBackEdit);

        userEmail = getIntent().getStringExtra("email");
        if (userEmail == null) {
            // Xử lý trường hợp lỗi, ví dụ, hiển thị thông báo và kết thúc activity
            Log.e("EditProfileActivity", "Email không được truyền");
            finish();
            return;
        } else {
            Log.d("EditProfileActivity", "User email: " + userEmail);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //dataBaseHelper.updateProfile(userEmail, name, selectedBitmap);

                SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", name);

                if (selectedBitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] imgBytes = byteArrayOutputStream.toByteArray();
                    contentValues.put("imguser", imgBytes);
                }

                try {
                    long result = db.update("users", contentValues, "email = ?", new String[]{userEmail});
                    if (result == -1) {
                        Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        Log.e("EditProfileActivity", "Failed to update profile");
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        Log.d("EditProfileActivity", "Profile updated successfully");
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.e("EditProfileActivity", "Error updating profile: " + e.getMessage());
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //C2
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageUser();
            }
        });
    }

    private void selectImageUser() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    editImage.setImageBitmap(selectedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Crop error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }



    /*private void saveProfile() {
        String name = etName.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        //dataBaseHelper.updateProfile(userEmail, name, selectedBitmap);

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);

        if (selectedBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] imgBytes = byteArrayOutputStream.toByteArray();
            contentValues.put("imguser", imgBytes);
        }

        try {
            long result = db.update("users", contentValues, "email = ?", new String[]{userEmail});
            if (result == -1) {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                Log.e("EditProfileActivity", "Failed to update profile");
            } else {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                Log.d("EditProfileActivity", "Profile updated successfully");
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        } catch (Exception e) {
            Log.e("EditProfileActivity", "Error updating profile: " + e.getMessage());
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}