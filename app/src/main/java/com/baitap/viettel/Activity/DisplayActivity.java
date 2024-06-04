package com.baitap.viettel.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baitap.viettel.Adapter.ItemAdapter;
import com.baitap.viettel.DataBaseHelper;
import com.baitap.viettel.Model.ItemModel;
import com.baitap.viettel.R;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    DataBaseHelper dBmain;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    Button add, back;
    public String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        userEmail = getIntent().getStringExtra("email");

        if (userEmail == null) {
            // Xử lý trường hợp lỗi, ví dụ, hiển thị thông báo và kết thúc activity
            Log.e("DisplayActivity", "Email không được truyền");
            finish();
            return;
        } else {
            Log.d("DisplayActivity", "User email: " + userEmail);
        }

        dBmain = new DataBaseHelper(this);
        findId();
        displayData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        //onClick();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayActivity.this, AddActivity.class);
                intent.putExtra("email",userEmail);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayActivity.this, MainActivity.class);
                intent.putExtra("email",userEmail);
                startActivity(intent);
            }
        });


    }

    private void displayData() {
        sqLiteDatabase = dBmain.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from items where email= ?",new String[]{userEmail});
        ArrayList<ItemModel> models = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            byte[] avatar = cursor.getBlob(2);
            String email = cursor.getString(3);
            //models.add(new ItemModel(id,avatar,name,email));
            models.add(new ItemModel(id,avatar,name,email));
            Log.d("Database", "Item: " + name + ", Email: " + userEmail);
        }
        cursor.close();
        itemAdapter = new ItemAdapter(this,models,sqLiteDatabase,R.layout.singledata);
        recyclerView.setAdapter(itemAdapter);
    }
    /*private void onClick(){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayActivity.this, AddActivity.class);
                intent.putExtra("email",userEmail);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayActivity.this, MainActivity.class);
                intent.putExtra("email",userEmail);
                startActivity(intent);
            }
        });
    }*/

    private void findId() {
        recyclerView = findViewById(R.id.rv);
        add = findViewById(R.id.btnAdd);
        back = findViewById(R.id.btnBack);
    }
}