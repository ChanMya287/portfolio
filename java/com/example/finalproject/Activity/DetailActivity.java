package com.example.finalproject.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.finalproject.Adapter.CartAdapter;
import com.example.finalproject.Adapter.ColorAdapter;
import com.example.finalproject.Adapter.SizeAdapter;
import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.Helper.ManagementCart;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityDetailBinding;

import java.util.ArrayList;

public class DetailActivity extends BaseActivity {
    private ActivityDetailBinding binding;
    private int numberOrder=1;
    private ManagementCart managementCart;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imageView=binding.imageView;

        managementCart=new ManagementCart(this);
        getBundles();
        initSize();
        initColor();
    }


    private void initColor() {
        ArrayList<String> list = new ArrayList<>();
        list.add("#7b1113");
        list.add("#100c08");
        list.add("#faf0e6");

        binding.recyclerColor.setAdapter(new ColorAdapter(list));
        binding.recyclerColor.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        list.add("XXL");
        binding.recyclerSize.setAdapter(new SizeAdapter(list));
        binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    private void getBundles() {
        String title = getIntent().getStringExtra("title");
        double price = getIntent().getDoubleExtra("price", 0.0);
        double rating = getIntent().getDoubleExtra("rating", 0.0);
        int imageUrl = getIntent().getIntExtra("imageResourceId",0);
        String description = getIntent().getStringExtra("description");
        double oldprice = getIntent().getDoubleExtra("oldprice",0.0);
        binding.titleTxt.setText(title);
        binding.priceTxt.setText("$" + price);
        binding.ratingBar.setRating((float) rating);
        binding.ratingTxt.setText(rating + " Rating");
        binding.descriptionTxt.setText(description);
        Glide.with(this)
                .load(imageUrl)
                .into(binding.imageView);
        ItemsDomain object = new ItemsDomain(title,price,oldprice,rating,imageUrl,description);

        binding.AddtoCartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object!= null) {
                    ColorAdapter colorAdapter = (ColorAdapter) binding.recyclerColor.getAdapter();
                    String selectedColor = colorAdapter.getSelectedColor();
                    SizeAdapter sizeAdapter = (SizeAdapter) binding.recyclerSize.getAdapter();
                    String selectedSize = sizeAdapter.getSelectedSize();
                    object.setNumberinCart(numberOrder);
                    if (selectedSize.equals("") || selectedColor.equals("") ){
                        Toast.makeText(DetailActivity.this, "Please Select Both Size and color!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        object.setColor(selectedColor);
                        object.setSize(selectedSize);
                        managementCart.insertItem(object);
                    }
                }
                else {
                    Log.e("Error", "object is null");
                }
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}