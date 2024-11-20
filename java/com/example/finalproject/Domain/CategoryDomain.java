package com.example.finalproject.Domain;

import android.graphics.drawable.Drawable;

public class CategoryDomain {
    private String title;
    private int id;
    private Drawable picUrl;

    public CategoryDomain(String title, int id, Drawable picUrl) {
        this.title = title;
        this.id = id;
        this.picUrl = picUrl;
    }

    public CategoryDomain() {
    }

    public CategoryDomain(Drawable categoryImage, String categoryItem) {
        this.picUrl=categoryImage;
        this.title=categoryItem;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Drawable getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(Drawable picUrl) {
        this.picUrl = picUrl;
    }
}
