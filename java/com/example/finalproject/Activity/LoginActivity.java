package com.example.finalproject.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityLoginBinding;
import com.example.finalproject.sqlite.DBHelper;

import java.sql.DatabaseMetaData;
import java.sql.Struct;

public class LoginActivity extends AppCompatActivity {
    public ActivityLoginBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        DBHelper db = new DBHelper(this);

        binding.btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(binding.edtuser.getText()).trim();
                String password = String.valueOf(binding.edtpassword.getText()).trim();
                Cursor cursor = db.selectData(name, password);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int userId = cursor.getInt(0);
                    loginUser(name, password);
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                } else if (name.equals("")|| password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please Enter Required fields", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,IntroActivity.class));
            }
        });
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                Toast.makeText(LoginActivity.this, "Create new account.", Toast.LENGTH_SHORT).show();
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
    private void loginUser(String name, String password) {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", name);
        editor.putString("password", password);
        editor.apply();
    }
}
