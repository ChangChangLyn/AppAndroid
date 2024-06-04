package com.baitap.viettel.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.baitap.viettel.Activity.AddActivity;
import com.baitap.viettel.Activity.DisplayActivity;
import com.baitap.viettel.DataBaseHelper;
import com.baitap.viettel.Model.ItemModel;
import com.baitap.viettel.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.viewHolder> {
    Context context;
    ArrayList<ItemModel>modelArrayList;
    SQLiteDatabase sqLiteDatabase;
    int singledata;

    public ItemAdapter(Context context, ArrayList<ItemModel> modelArrayList, SQLiteDatabase sqLiteDatabase, int singledata) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.singledata = singledata;
    }

    @NonNull
    @Override
    public ItemAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.singledata,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.viewHolder holder, int position) {
        final ItemModel model = modelArrayList.get(position);
        byte[]image = model.getImage();
        Bitmap bitmap= BitmapFactory.decodeByteArray(image,0,image.length);
        holder.imageavatar.setImageBitmap(bitmap);
        holder.txtname.setText(model.getNameimg());

        holder.flowmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.flowmenu);
                popupMenu.inflate(R.menu.flow_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if(itemId == R.id.edit_menu){
                            Bundle bundle=new Bundle();
                            String userEmail = model.getEmail();
                            Log.d("ItemAdapter", "User email: " + userEmail);
                            bundle.putInt("idItem",model.getId());
                            bundle.putByteArray("imageItem",model.getImage());
                            bundle.putString("nameItem", model.getNameimg());
                            bundle.putString("email", model.getEmail());
                            Intent intent= new Intent(context, AddActivity.class);
                            intent.putExtra("items",bundle);
                            intent.putExtra("email",userEmail);
                            context.startActivity(intent);
                            return true;
                        }else if(itemId == R.id.delete_menu){

                            DataBaseHelper dBmain = new DataBaseHelper(context);
                            sqLiteDatabase = dBmain.getReadableDatabase();
                            long recdelete=sqLiteDatabase.delete("items","idItem="+model.getId(),null);
                            if (recdelete != -1) {
                                Toast.makeText(context, "Data deleted", Toast.LENGTH_SHORT).show();
                                modelArrayList.remove(position);
                                notifyDataSetChanged();
                            }
                            return true;
                        }else{
                            return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imageavatar;
        TextView txtname;
        ImageButton flowmenu;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageavatar=itemView.findViewById(R.id.viewavatar);
            txtname=itemView.findViewById(R.id.txt_name);
            flowmenu=itemView.findViewById(R.id.flowmenu);
        }
    }
}
