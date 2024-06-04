package com.baitap.viettel.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.baitap.viettel.DataBaseHelper;
import com.baitap.viettel.MailSender;
import com.baitap.viettel.R;
import com.fraggjkee.smsconfirmationview.SmsConfirmationView;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etUserName, etNewPass, etReNewPass;
    Button btnSubmit, btnSubmitVerification, btnSubmitPassword;
    ConstraintLayout clForgotPassword, clVerification, clSetNewPassword;
    SmsConfirmationView smsConfirmationView;
    TextView tvLogin1, tvLogin2, tvLogin3, tvUserNameNotCorrect, tvCodeNotCorrect,tvPasswordNotMatch;
    ImageView ivBack;

    private String generatedOTP;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        dbHelper = new DataBaseHelper(this);
        setControl();
        setEvent();


    }
    private void setEvent() {
        btnSubmitClick();
        tvLogin1Click();
    }


    private void btnSubmitClick() {
        btnSubmit.setOnClickListener(view -> {
            String email = etUserName.getText().toString();
            Boolean checkEmail = dbHelper.checkEmail(email);
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your username!", Toast.LENGTH_SHORT).show();
                etUserName.requestFocus();
                return;
            }
            if(checkEmail == false){
                Toast.makeText(this, "Email not in use!", Toast.LENGTH_SHORT).show();
                etUserName.requestFocus();
                return;
            }

            ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
            progressDialog.setMessage("Checking in...");
            progressDialog.setTitle("Forgot password");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

            // Tạo mã OTP
            generatedOTP = String.valueOf(new Random().nextInt(899999) + 100000);


            // Gửi OTP qua email
            new MailSender(email, "Password reset OTP", "Your OTP is: " + generatedOTP).execute();



            progressDialog.dismiss();
            clForgotPassword.setVisibility(View.GONE);
            clVerification.setVisibility(View.VISIBLE);
            smsConfirmationView.setOnChangeListener((code, isComplete) -> {
                if (isComplete) {
                    if (code.equals(generatedOTP)) {
                        clVerification.setVisibility(View.GONE);
                        clSetNewPassword.setVisibility(View.VISIBLE);
                        btnSubmitPassword.setOnClickListener(v -> {
                            String newPass = etNewPass.getText().toString();
                            String reNewPass = etReNewPass.getText().toString();
                            if (newPass.equals(reNewPass) && !newPass.isEmpty()) {
                                dbHelper.updatePassword(email, newPass);
                                Dialog dialog = new Dialog(ForgotPasswordActivity.this);
                                dialog.setContentView(R.layout.dialog_forgot_password_success);
                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setGravity(Gravity.BOTTOM);
                                dialog.setCancelable(false);
                                dialog.show();
                                Button btnLogin = dialog.findViewById(R.id.btnChangePassword);
                                btnLogin.setOnClickListener(v1 -> {
                                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                                });
                            } else {
                                tvPasswordNotMatch.setVisibility(View.VISIBLE);
                                Toast.makeText(ForgotPasswordActivity.this, "Password not matching!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        tvLogin3.setOnClickListener(v -> {
                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        });
                    } else {
                        tvCodeNotCorrect.setVisibility(View.VISIBLE);
                        Toast.makeText(ForgotPasswordActivity.this, "Your code is incorrect!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            tvLogin2.setOnClickListener(v -> {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            });
            ivBack.setOnClickListener(v -> {
                tvUserNameNotCorrect.setVisibility(View.GONE);
                etUserName.setText(null);
                clVerification.setVisibility(View.GONE);
                clForgotPassword.setVisibility(View.VISIBLE);
            });
        });

    }

    private void tvLogin1Click() {
        tvLogin1.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        });
    }

    private void setControl() {
        //Anhxa clForgotPassword
        etUserName = findViewById(R.id.etUserName);
        btnSubmit = findViewById(R.id.btnSubmit);
        clForgotPassword = findViewById(R.id.clForgotPassword);
        clVerification = findViewById(R.id.clVerification);
        clSetNewPassword = findViewById(R.id.clSetNewPassword);
        tvLogin1 = findViewById(R.id.tvLogin1);
        tvUserNameNotCorrect = findViewById(R.id.tvUserNameNotCorrect);
        //Anh xa clVerification
        btnSubmitVerification= findViewById(R.id.btnSubmitVerification);
        smsConfirmationView = findViewById(R.id.smsCodeView);
        tvLogin2 = findViewById(R.id.tvLogin2);
        ivBack = findViewById(R.id.ivBack);
        tvCodeNotCorrect = findViewById(R.id.tvCodeNotCorrect);
        //Anh xa clSetNewPassword
        etNewPass = findViewById(R.id.etNewPass);
        etReNewPass = findViewById(R.id.etReNewPass);
        btnSubmitPassword = findViewById(R.id.btnSubmitPassword);
        tvLogin3 = findViewById(R.id.tvLogin3);
        tvPasswordNotMatch = findViewById(R.id.tvPasswordNotMatch);
    }
}