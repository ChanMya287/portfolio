package com.example.finalproject.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityLoginBinding;
import com.example.finalproject.databinding.ActivitySignupBinding;
import com.example.finalproject.sqlite.DBHelper;

public class SignupActivity extends AppCompatActivity {
    public ActivitySignupBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        DBHelper db = new DBHelper(this);

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.edtuser.getText().toString().trim();
                String password = binding.edtpassword.getText().toString().trim();

                if (name.equals("")|| password.equals("")){
                    Toast.makeText(SignupActivity.this, "Please fill the required fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    Cursor cursor = db.selectData(name,password);
                    if (cursor.getCount()>0){
                        Toast.makeText(SignupActivity.this, "User is already existed.Please choose different name.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        db.insertUser(name,password);
                        Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, IntroActivity.class));
                    }
                }
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,IntroActivity.class));
            }
        });

        binding.passwordToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edtpassword.getInputType() == InputType.TYPE_CLASS_TEXT) {
                    binding.edtpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.passwordToggleButton.setImageResource(R.drawable.eye);
                } else {
                    binding.edtpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    binding.passwordToggleButton.setImageResource(R.drawable.eye_slash);
                }
            }
        });
    }

}
