package com.baitap.viettel.Activity;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.baitap.viettel.DataBaseHelper;
import com.baitap.viettel.Model.UserModel;
import com.baitap.viettel.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;
    private TextView tvName, tvEmail, tvChangePassword;
    private ImageView uploadImage;
    private Button editProfile, logOut;

    private String userEmail;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        dataBaseHelper = new DataBaseHelper(this);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvChangePassword = findViewById(R.id.tvChangePassword);
        uploadImage = findViewById(R.id.uploadImage);

        editProfile = findViewById(R.id.btnEditProfile);
        logOut = findViewById(R.id.btnLogout);

        userEmail = getIntent().getStringExtra("email");
        if (userEmail == null) {
            // Xử lý trường hợp lỗi, ví dụ, hiển thị thông báo và kết thúc activity
            Log.e("MainActivity", "Email không được truyền");
            finish();
            return;
        } else {
            Log.d("MainActivity", "User email: " + userEmail);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                intent.putExtra("email",userEmail);
                startActivity(intent);
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                intent.putExtra("email", userEmail);
                startActivityForResult(intent, 1);
            }
        });

        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_change_password);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.show();

                // Anh xa dialog
                ConstraintLayout clChangePassword = dialog.findViewById(R.id.clChangePassword);
                ConstraintLayout clChangePasswordSuccess = dialog.findViewById(R.id.clChangePasswordSuccess);
                EditText etOldPassword = dialog.findViewById(R.id.etOldPassword);
                EditText etNewPassword = dialog.findViewById(R.id.etNewPassword);
                EditText etReNewPassword = dialog.findViewById(R.id.etReNewPassword);
                TextView tvErrorChangePassword = dialog.findViewById(R.id.tvErrorChangePassword);
                Button btnChangePassword = dialog.findViewById(R.id.btnChangePassword);
                Button btnOK = dialog.findViewById(R.id.btnOk);

                DataBaseHelper dbHelper = new DataBaseHelper(MainActivity.this);

                btnChangePassword.setOnClickListener(v1 -> {
                    String password = etOldPassword.getText().toString();
                    String newPassword = etNewPassword.getText().toString();
                    String reNewPassword = etReNewPassword.getText().toString();

                    UserModel user = dbHelper.getUserEmail(userEmail);

                    if(user != null && password.equals(user.getPassword())){
                        if(newPassword.equals(reNewPassword)){
                            dbHelper.updatePassword(user.getEmail(), newPassword);
                            clChangePassword.setVisibility(View.GONE);
                            clChangePasswordSuccess.setVisibility(View.VISIBLE);
                            user.setPassword(newPassword);
                            btnOK.setOnClickListener(v2 -> {
                                dialog.dismiss();
                            });
                        } else {
                            tvErrorChangePassword.setText("New password and confirm password do not match!");
                        }
                    } else {
                        tvErrorChangePassword.setText("Your password is not correct");
                    }
                });
            }
        });



        if (userEmail != null) {
            displayUserInfo(userEmail);
        }

    }
    private void displayUserInfo(String email) {
        Cursor cursor = dataBaseHelper.getUserData(email);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            byte[] imgBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("imguser"));

            tvName.setText(name);
            tvEmail.setText(userEmail);
            if (imgBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
                uploadImage.setImageBitmap(bitmap);
            }
            cursor.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            displayUserInfo(userEmail);
        }
    }


}