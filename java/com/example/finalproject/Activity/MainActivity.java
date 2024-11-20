package com.example.finalproject.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.finalproject.Adapter.CategoryAdapter;
import com.example.finalproject.Adapter.PopularAdapter;
import com.example.finalproject.Adapter.SliderAdapter;
import com.example.finalproject.Domain.CategoryDomain;
import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.Domain.SliderItems;
import com.example.finalproject.Helper.ManagementCart;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityMainBinding;
import com.example.finalproject.sqlite.DBHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class
MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DBHelper dbHelper;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.dbHelper = new DBHelper(this);
        Intent intent = getIntent();
        if (intent.getBooleanExtra("show_rating_dialog", false)) {
           displaydialog();
        }

        initBanner();
        initCategory();
        initPopular();
        bottomNavigation();
        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = prefs.getString("username", "");
    }
    private void showConfirmDialog(View v, String menuItemText){
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = prefs.getString("username", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm " +menuItemText);
        builder.setMessage("Are you sure you want to " + menuItemText + " " + name +"?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (menuItemText.equals("Log Out")) {
                    logoutUser();
                } else if (menuItemText.equals("Delete")) {
                    deleteUser();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
    }
    public void showPopup(View view){
        PopupMenu popupMenu=new PopupMenu(this,view);
        popupMenu.inflate(R.menu.menu_options);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                String menuItemText = menuItem.getTitle().toString();
                if (id == R.id.logOut){
                    showConfirmDialog(view, menuItemText);
                }
                else if (id== R.id.delete) {
                    showConfirmDialog(view,menuItemText);
                }
                return false;
            }
        });
    }
    private void deleteUser() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = prefs.getString("username", "");
        String password = prefs.getString("password", "");
        int userId = getUserId(name, password);
        if (userId != -1) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                db.delete("user", "id=?", new String[]{String.valueOf(userId)});
                db.delete("cart","userid=?",new String[]{String.valueOf(userId)});
                Toast.makeText(MainActivity.this, "Your Account is Deleted!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,IntroActivity.class));
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }
    private void logoutUser() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, IntroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "You have logged out successfully", Toast.LENGTH_SHORT).show();
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

    private void bottomNavigation() {
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CartActivity.class));
            }
        });
        binding.commission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CommissionActivity.class));
            }
        });
    }
    public void displaydialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rating_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        TextView textView = new TextView(this);
        textView.setTextColor(Color.WHITE);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Rating submitted!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.border_rating);
        dialog.show();
    }

    private  void initPopular(){
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();

        String[] popularImageNames = getResources().getStringArray(R.array.popular_image_names);
        String[] popularItems=getResources().getStringArray(R.array.popular_item_names);
        String[] description = getResources().getStringArray(R.array.descriptions);
        int[] popularReviews = {16,21,12,18,22,34,64,13,43,24};
        double[] popularPrices={30,47,38,67,40,54,67,77,54,36};
        double[] popularRatings={4.3,4.6,3.8,5,4,4,2.5,3,1,4.1};
        double[] popularOldPrices={35,55,44,74,46,56,70,80,66,43};
        if (popularImageNames.length != popularItems.length) {
            throw new RuntimeException("popularImageNames and popularItems arrays must have the same length");
        }
        for (int i = 0; i < popularImageNames.length; i++)  {

            int imageResourceId = getResources().getIdentifier(popularImageNames[i], "drawable", getPackageName());
            if (imageResourceId != 0) {
                ItemsDomain itemsdomain = new ItemsDomain(imageResourceId, popularItems[i],popularReviews[i],description[i],popularPrices[i],popularRatings[i],popularOldPrices[i]);
                items.add(itemsdomain);
            } else {
                Log.w("initPopular", "Image resource not found: " + popularImageNames[i]);
            }
        }
        if (!items.isEmpty()){
            binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager
                    (MainActivity.this,2));
            binding.recyclerViewPopular.setAdapter(new PopularAdapter(items));
            binding.recyclerViewPopular.setNestedScrollingEnabled(true);
            binding.progressBarPopular.setVisibility(View.GONE);
        }
        else {
            binding.progressBarPopular.setVisibility(View.GONE);
        }
        binding.exploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("items", items);
                startActivity(intent);
            }
        });
    }

    private  void initCategory(){
        binding.progressBarOfficial.setVisibility(View.VISIBLE);
        ArrayList<CategoryDomain> items = new ArrayList<>();

        String[] categoryImageNames = getResources().getStringArray(R.array.category_image_names);
        String[] categoryItems= {"Shirt","Pants","Hat","Zara","Gucci"};

        for (int i = 0; i < categoryImageNames.length; i++)  {
            int imageResourceId = getResources().getIdentifier(categoryImageNames[i], "drawable", getPackageName());
            if (imageResourceId != 0) {
                Drawable categoryImage = getResources().getDrawable(imageResourceId);
                CategoryDomain categoryDomain = new CategoryDomain(categoryImage, categoryItems[i]);
                items.add(categoryDomain);
            } else {
                Log.w("initPopular", "Image resource not found: " + categoryImageNames[i]);
            }
        }
        if (!items.isEmpty()){
            binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager
                    (MainActivity.this, LinearLayoutManager.HORIZONTAL,false));
            binding.recyclerViewOfficial.setAdapter(new CategoryAdapter(items));
            binding.recyclerViewOfficial.setNestedScrollingEnabled(true);
        }
        binding.progressBarOfficial.setVisibility(View.GONE);
    }

    private void initBanner() {
        binding.progressBarBanner.setVisibility(View.VISIBLE);

        ArrayList<SliderItems> items = new ArrayList<>();

        String[] bannerImageNames = getResources().getStringArray(R.array.banner_image_names);

        for (String imageName : bannerImageNames) {
            int imageResourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (imageResourceId != 0) {
                Drawable bannerImage = getResources().getDrawable(imageResourceId);
                SliderItems sliderItem = new SliderItems(bannerImage);
                items.add(sliderItem);
            } else {
                Log.w("initBanner", "Image resource not found: " + imageName);
            }
        }

        binding.progressBarBanner.setVisibility(View.GONE);

        banners(items);
    }
    private void banners(ArrayList<SliderItems> items){
        binding.viewPagerSlider.setAdapter(new SliderAdapter(items, binding.viewPagerSlider));
        binding.viewPagerSlider.setClipToPadding(false);
        binding.viewPagerSlider.setClipChildren(false);
        binding.viewPagerSlider.setOffscreenPageLimit(3);
        binding.viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewPagerSlider.setPageTransformer(compositePageTransformer);
    }

}