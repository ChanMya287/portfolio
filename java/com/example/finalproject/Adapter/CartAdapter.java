package com.example.finalproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalproject.Activity.CartActivity;
import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.Helper.ChangeNumberItemsListener;
import com.example.finalproject.Helper.ManagementCart;
import com.example.finalproject.databinding.ViewholderCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
    ArrayList<ItemsDomain> listItemSelected;
    ChangeNumberItemsListener changeNumberItemsListener;
    private ManagementCart managementCart;
    private int imageResourceId;
    private CartActivity cartActivity;
    public CartAdapter(ArrayList<ItemsDomain> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener,int imageResourceId) {
        this.listItemSelected = listItemSelected;
        this.changeNumberItemsListener = changeNumberItemsListener;
        managementCart = new ManagementCart(context);
        this.imageResourceId = imageResourceId;
    }
    @NonNull
    @Override
    public CartAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding= ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.Viewholder holder, int position) {
        int imageUrl = listItemSelected.get(position).getUrl();
        if (imageResourceId!= 0){
            imageUrl= imageResourceId;
        }
        holder.binding.titleTxt.setText(listItemSelected.get(position).getTitle());
            holder.binding.feeEachItem.setText("$" + listItemSelected.get(position).getPrice());
            holder.binding.totalEachItem.setText("$" + Math.round(listItemSelected.get(position).getNumberinCart() * listItemSelected.get(position).getPrice()));
            holder.binding.numberitemTxt.setText(String.valueOf(listItemSelected.get(position).getNumberinCart()));
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.binding.pic);

            holder.binding.plusCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    managementCart.plusItem(listItemSelected, position, new ChangeNumberItemsListener() {
                        @Override
                        public void changed() {
                            listItemSelected.set(position, managementCart.getItem(listItemSelected, position));
                            notifyItemChanged(position);
                            changeNumberItemsListener.changed();
                        }
                    });
                }
            });
            holder.binding.minusCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    managementCart.minusItem(listItemSelected, position, new ChangeNumberItemsListener() {
                                @Override
                                public void changed() {
                                    listItemSelected.set(position, managementCart.getItem(listItemSelected, position));
                                    notifyItemChanged(position);
                                    notifyDataSetChanged();
                                    changeNumberItemsListener.changed();
                                    managementCart.getListCart();
                                    if (listItemSelected.get(position).getNumberinCart() == 0){
                                        managementCart.deleteItem(listItemSelected,position);
                                        listItemSelected.remove(position);
                                        notifyItemRemoved(position);
                                        changeNumberItemsListener.changed();
                                    }
                                }
                            });
                        }
            });
            holder.binding.trashBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        managementCart.deleteItem(listItemSelected, position);
                        listItemSelected.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        changeNumberItemsListener.changed();
                        managementCart.getListCart();
                    }
            });
    }
    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;
        public Viewholder(@NonNull ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
