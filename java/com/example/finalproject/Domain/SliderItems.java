package com.example.finalproject.Domain;

import android.graphics.drawable.Drawable;

public class SliderItems {

    private Drawable image;

    public SliderItems() {}

    public SliderItems(Drawable image) {
        this.image = image;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

}
