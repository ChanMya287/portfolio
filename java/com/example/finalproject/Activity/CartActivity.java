package com.example.finalproject.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalproject.Adapter.CartAdapter;
import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.Helper.ChangeNumberItemsListener;
import com.example.finalproject.Helper.ManagementCart;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityCartBinding;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private double tax;
    private ManagementCart managementCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        managementCart = new ManagementCart(this);

        initCartList();
        calculatorCart();
        setVariable();
    }
    private void initCartList() {
        readlist();
        int imageResourceId=0;
        binding.cartView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.cartView.setAdapter(new CartAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                calculatorCart();
                readlist();
            }
        },imageResourceId));
    }
    private void readlist(){
        if(managementCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
    }
    private void setVariable() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.address.getText().toString().equals("")){
                    Toast.makeText(CartActivity.this, "Please Enter Your Address!", Toast.LENGTH_SHORT).show();
                }
                else {
                    showConfirmDialog(v);
                }
            }
        });
    }
    private void showConfirmDialog(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Purchase");
        builder.setMessage("Are you sure you want to purchase the items");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (ItemsDomain item : managementCart.getListCart()) {
                    managementCart.insertorder(item, binding.address.getText().toString());
                }
                managementCart.deleteAll();
                Toast.makeText(CartActivity.this, "Purchase Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CartActivity.this,MainActivity.class);
                intent.putExtra("show_rating_dialog", true);
                startActivity(intent);
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

    private void calculatorCart() {
        double percentTax = 0.2;
        double delivery = 10;
        tax = Math.round((managementCart.getTotalFee()+percentTax * 100.0))/100.0;

        double total = Math.round((managementCart.getTotalFee()+ tax + delivery)*100.0)/100.0;
        double itemTotal = Math.round((managementCart.getTotalFee() * 100.0))/100.0;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$"+tax);
        binding.deliveryTxt.setText("$"+delivery);
        binding.totalTxt.setText("$" + total);
    }
}