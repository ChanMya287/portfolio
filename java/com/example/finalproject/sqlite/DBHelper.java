package com.example.finalproject.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class DBHelper extends SQLiteOpenHelper {
    byte[] imageInBytes;
    private Object Context;
    Context context;

    public DBHelper(@Nullable Context context) {
        super(context, "fashionista.db", null, 1);

    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cart (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, price REAL, number_in_cart INTEGER, image TEXT,size TEXT, color TEXT, userid INTEGER, FOREIGN KEY (userid) REFERENCES user(id))");
        db.execSQL("CREATE TABLE commission(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,bust REAL,waist REAL, shoulder REAL, arm REAL,detail TEXT,email TEXT,address TEXT, image BLOB)");
        db.execSQL("CREATE TABLE user(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, password TEXT)");
        db.execSQL("CREATE TABLE purchase(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, price REAL, number_in_cart INTEGER, image TEXT, size TEXT, color TEXT, address TEXT, userid INTEGER, FOREIGN KEY (userid) REFERENCES user(id))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cart");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS commission");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS purchase");
        onCreate(db);
    }

    public boolean insertdata(String name, String bust, String waist, String shoulder, String arm, String detail,String gmail,String address, Bitmap image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("bust", bust);
        contentValues.put("waist", waist);
        contentValues.put("shoulder", shoulder);
        contentValues.put("arm", arm);
        contentValues.put("detail",detail);
        contentValues.put("email", gmail);
        contentValues.put("address",address);
        ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,objectByteArrayOutputStream);
        imageInBytes = objectByteArrayOutputStream.toByteArray();
        contentValues.put("image",imageInBytes);
        long id1 = db.insert("commission", null,contentValues);
        if (id1<=0){
            return false;
        }
        else {
            return true;
        }
    }
    public Cursor selectData(String name, String password){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE name=? AND password=?",new String[]{name, password});
        return cursor;
    }
    public boolean insertUser(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("password", password);
        long result = db.insert("user", null, contentValues);
        if (result == -1) return false;
        else return true;
    }
}
