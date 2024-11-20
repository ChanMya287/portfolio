package com.example.finalproject.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityCommissionBinding;
import com.example.finalproject.sqlite.DBHelper;

import java.io.IOException;
import java.util.ArrayList;

public class CommissionActivity extends AppCompatActivity {
    public ActivityCommissionBinding binding;
    DBHelper db;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    private static final int PICK_IMAGE =1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new DBHelper(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Select Picture"),PICK_IMAGE);
            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.clientName.getText().toString().trim().isEmpty()
                        && !binding.bust.getText().toString().trim().isEmpty()
                        && !binding.waist.getText().toString().trim().isEmpty()
                        && !binding.shoulders.getText().toString().trim().isEmpty()
                        && !binding.arms.getText().toString().trim().isEmpty()
                        && !binding.detail.getText().toString().trim().isEmpty()
                        && !binding.gmail.getText().toString().trim().isEmpty()
                        && !binding.address.getText().toString().trim().isEmpty()
                        && imageToStore != null){
                    db.insertdata(
                binding.clientName.getText().toString(),
                binding.bust.getText().toString(),
                binding.waist.getText().toString(),
                binding.shoulders.getText().toString(),
                binding.arms.getText().toString(),
                binding.detail.getText().toString(),
                        binding.gmail.getText().toString(),
                        binding.address.getText().toString(),
                        imageToStore);
                    Toast.makeText(CommissionActivity.this, "Commission submission successful!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CommissionActivity.this,MainActivity.class));
                } else {
                    Toast.makeText(CommissionActivity.this, "Please fill the required fields!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE && resultCode==RESULT_OK && data.getData()!=null)
        {
            imageFilePath = data.getData();
            try {
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            binding.imageUpload.setImageBitmap(imageToStore);
        }
    }

}
