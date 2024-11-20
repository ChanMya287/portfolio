package com.example.finalproject.Helper;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;

import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.sqlite.DBHelper;

import java.util.ArrayList;

public class ManagementCart {

    private Context context;
    private DBHelper dbHelper;
    SharedPreferences prefs;
    public ManagementCart(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
        prefs = context.getSharedPreferences("user_prefs", MODE_PRIVATE);
    }
    private int getUserId(String name, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = { "id" };
        String selection = "name = ? AND password = ?";
        String[] selectionArgs = { name, password };
        Cursor cursor = db.query("user", projection, selection, selectionArgs, null, null, null);
        int userId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        return userId;
    }

    public void insertItem(ItemsDomain item) {
        String name = prefs.getString("username", null);
        String password = prefs.getString("password", null);
        int userId = getUserId(name,password);
        if (userId!= -1) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("title", item.getTitle());
            values.put("price", item.getPrice());
            values.put("number_in_cart", item.getNumberinCart());
            values.put("image", item.getUrl());
            values.put("userid", userId);
            values.put("size",item.getSize());
            values.put("color",item.getColor());
            db.insert("cart", null, values);
            db.close();
            Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Error Occurred, Please Try Again.", Toast.LENGTH_SHORT).show();
        }
    }
        public ArrayList<ItemsDomain> getListCart(){
            String name = prefs.getString("username", null);
            String password = prefs.getString("password", null);
            int userId = getUserId(name,password);
            if (userId!=-1) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query("cart", new String[]{"id", "title", "price", "number_in_cart", "image","size","color"}, "userid = ?", new String[]{String.valueOf(userId)}, null, null, null);
                ArrayList<ItemsDomain> listItem = new ArrayList<>();
                while (cursor.moveToNext()) {
                    ItemsDomain item = new ItemsDomain();
                    item.setId(cursor.getInt(0));
                    item.setTitle(cursor.getString(1));
                    item.setPrice(cursor.getDouble(2));
                    item.setNumberinCart(cursor.getInt(3));
                    item.setUrl(cursor.getInt(4));
                    item.setSize(cursor.getString(5));
                    item.setColor(cursor.getString(6));
                    listItem.add(item);
                }
                cursor.close();
                db.close();
                return listItem;
            }
            return null;
        }
    public void insertorder(ItemsDomain item, String address) {
        String name = prefs.getString("username", null);
        String password = prefs.getString("password", null);
        int userId = getUserId(name,password);
        if (userId!= -1) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("title", item.getTitle());
            values.put("price", item.getPrice());
            values.put("number_in_cart", item.getNumberinCart());
            values.put("image", item.getUrl());
            values.put("userid", userId);
            values.put("address",address);
            values.put("size" , item.getSize());
            values.put("color", item.getColor());
            db.insert("purchase", null, values);
            db.close();
        }else {
            Toast.makeText(context, "Error Occurred, Please Try Again.", Toast.LENGTH_SHORT).show();
        }
    }
    public ItemsDomain getItem(ArrayList<ItemsDomain> list, int position) {
        if (list!= null &&!list.isEmpty() && position < list.size()) {
            return list.get(position);
        } else {
            return null;
        }
    }
    public void minusItem(ArrayList<ItemsDomain> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listItem != null && position >= 0 && position < listItem.size()) {
            ItemsDomain item = listItem.get(position);
            if (item != null) {
                if (item.getNumberinCart() >= 1) {
                    item.setNumberinCart(item.getNumberinCart() - 1);
                    updateItem(item);
                } else {
                    deleteItem(listItem,position);
                }
                changeNumberItemsListener.changed();
            }
        }
        getListCart();
    }

    public void plusItem(ArrayList<ItemsDomain> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        ItemsDomain item = listItem.get(position);
        item.setNumberinCart(item.getNumberinCart() + 1);
        updateItem(item);
        changeNumberItemsListener.changed();
    }
    public void updateItem(ItemsDomain item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("price", item.getPrice());
        values.put("number_in_cart", item.getNumberinCart());
        values.put("image", item.getUrl());
        db.update("cart", values, "id =?", new String[]{String.valueOf(item.getId())});
        db.close();
    }
        public Double getTotalFee () {
            String name = prefs.getString("username", null);
            String password = prefs.getString("password", null);
            int userId = getUserId(name,password);
            if(userId!=-1) {
                getListCart();
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query("cart", new String[]{"price", "number_in_cart"}, "userid=?", new String[]{String.valueOf(userId)}, null, null, null);
                double fee = 0;
                while (cursor.moveToNext()) {
                    fee += cursor.getDouble(0) * cursor.getInt(1);
                }
                cursor.close();
                db.close();
                return fee;
            }
            return null;
        }

    public void deleteAll() {
        String name = prefs.getString("username", "");
        String password = prefs.getString("password", "");
        int userId = getUserId(name, password);
        if (userId != -1) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                db.delete("cart", "userid=?", new String[]{String.valueOf(userId)});
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
                db.close();
            }
            getListCart();
        }
    }
    public void deleteItem(ArrayList<ItemsDomain> listItemSelected, int position) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("cart","id=?",new String[]{String.valueOf(listItemSelected.get(position).getId())});
        db.delete("purchase","id=?",new String[]{String.valueOf(listItemSelected.get(position).getId())});
        db.close();
        getListCart();
        Toast.makeText(context, listItemSelected.get(position).getTitle() + " is removed successfully", Toast.LENGTH_SHORT).show();
    }
}

