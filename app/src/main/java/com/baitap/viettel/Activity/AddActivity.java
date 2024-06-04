package com.baitap.viettel.Activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.baitap.viettel.DataBaseHelper;
import com.baitap.viettel.R;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;

public class AddActivity extends AppCompatActivity {

    DataBaseHelper dBmain;
    SQLiteDatabase sqLiteDatabase;
    EditText name;
    Button upload, edit, display;
    ImageView image;
    MaterialCardView selectPhoto;

    Uri imagePath;
    Bitmap imageToStore;

    ProgressDialog dialog;
    int id=0;
    private static final int PICK_IMAGE_REQUEST = 99;

    //2
    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;
    String[] cameraPermission;
    String[] storagePermission;
    private String userEmail;
    private String email;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        dBmain = new DataBaseHelper(this);

        name = findViewById(R.id.itemName);
        image = findViewById(R.id.uploadImage);
        upload = findViewById(R.id.btnUpload);
        edit = findViewById(R.id.btnEdit);
        display = findViewById(R.id.btnDisplay);

        email = getIntent().getStringExtra("email");

        if (email == null) {
            Log.e("AddActivity", "Email không được truyền");
            finish();
            return;
        } else {
            Log.d("AddActivity", "User email: " + email);
        }



        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        editData();
        insertDataItem();
        imagePick();



    }

    private void editData() {
        if (getIntent().getBundleExtra("items") != null) {
            Bundle bundle = getIntent().getBundleExtra("items");
            assert bundle != null;
            id=bundle.getInt("idItem");
            Log.d("AddActivity", "Id: " + id);

            userEmail = bundle.getString("email");


            if (userEmail == null) {
                // Xử lý trường hợp lỗi, ví dụ, hiển thị thông báo và kết thúc activity
                Log.e("AddActivity1", "Email không được truyền");
                finish();
                return;
            } else {
                Log.d("AddActivity1", "User email: " + userEmail);
            }

            byte[]bytes=bundle.getByteArray("imageItem");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            image.setImageBitmap(bitmap);
            name.setText(bundle.getString("nameItem"));

            upload.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
        }
    }

    private void imagePick() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Image click detected");
                int avatar=0;
                if (avatar == 0) {
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                        pickFromGallery();
                    }
                } else if (avatar == 1) {
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST:
                if (grantResults.length > 0) {
                    boolean camera_accept = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storage_accept= grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if (camera_accept && storage_accept) {
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "Enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            break;
            case  STORAGE_REQUEST:
                if (grantResults.length > 0) {
                    boolean storage_accept = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (storage_accept) {
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.get().load(resultUri).into(image);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(TAG, "Crop error: " + error.getMessage());
            }
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(storagePermission,STORAGE_REQUEST);
        }
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

    }

    private void pickFromGallery() {
        CropImage.activity().start(this);
    }


    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(cameraPermission,CAMERA_REQUEST);
        }
    }

    private boolean checkCameraPermission() {
        boolean result=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED;
        boolean result2=ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED;
        return result && result2;
    }

    private void insertDataItem() {
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userEmail = getIntent().getStringExtra("email");

                if (userEmail == null) {
                    // Xử lý trường hợp lỗi, ví dụ, hiển thị thông báo và kết thúc activity
                    Log.e("AddActivity", "Email không được truyền");
                    finish();
                    return;
                } else {
                    Log.d("AddActivity", "User email: " + userEmail);
                }

                ContentValues cv = new ContentValues();
                cv.put("imageItem",ImageViewToByte(image));
                cv.put("nameItem",name.getText().toString());
                cv.put("email",userEmail);
                sqLiteDatabase=dBmain.getWritableDatabase();
                Log.d("AddActivity", "Adding item to database: " + name + ", Email: " + userEmail);
                Long recinsert = sqLiteDatabase.insert("items",null,cv);


                if (recinsert != -1) {
                    Toast.makeText(AddActivity.this, "Inserted successfully", Toast.LENGTH_SHORT).show();
                    image.setImageResource(R.drawable.ic_photo);
                    name.setText("");
                }

            }

        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put("imageItem",ImageViewToByte(image));
                cv.put("nameItem",name.getText().toString());
                sqLiteDatabase=dBmain.getWritableDatabase();
                long recedit = sqLiteDatabase.update("items",cv,"idItem="+id,null);
                if(recedit !=-1){
                    Toast.makeText(AddActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                    image.setImageResource(R.drawable.ic_photo);
                    name.setText("");
                    edit.setVisibility(View.GONE);
                    upload.setVisibility(View.VISIBLE);
                }
            }
        });
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, DisplayActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });
    }

    private byte[] ImageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
        return stream.toByteArray();

    }

}