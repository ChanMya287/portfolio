package com.example.finalproject.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalproject.Activity.DetailActivity;
import com.example.finalproject.Activity.SearchActivity;
import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.databinding.ViewholderPopularBinding;

import java.io.Serializable;
import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {
    ArrayList<ItemsDomain> items;
    Context context;

    public PopularAdapter(ArrayList<ItemsDomain> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public PopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        ViewholderPopularBinding binding = ViewholderPopularBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.ViewHolder holder, int position) {
        ItemsDomain object = items.get(position);
        holder.binding.titleTxt.setText(object.getTitle());
        holder.binding.reviewTxt.setText(""+object.getReview());
        holder.binding.priceTxt.setText("$" + object.getPrice());
        holder.binding.ratingTxt.setText("(" + object.getRating()+")");
        holder.binding.oldPriceTxt.setText("$"+object.getOldPrice());
        holder.binding.oldPriceTxt.setPaintFlags(holder.binding.oldPriceTxt.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.ratingBar2.setRating((float) object.getRating());
        RequestOptions options = new RequestOptions();
        options = options.transform(new CenterCrop());

        Glide.with(context)
                .load(object.getUrl())
                .apply(options)
                .into(holder.binding.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("title", items.get(position).getTitle());
                intent.putExtra("price", items.get(position).getPrice());
                intent.putExtra("rating", items.get(position).getRating());
                intent.putExtra("description", items.get(position).getDescription());
                intent.putExtra("imageResourceId", items.get(position).getUrl());
                intent.putExtra("oldprice",items.get(position).getOldPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(ArrayList<ItemsDomain> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderPopularBinding binding;
        public ViewHolder(@NonNull ViewholderPopularBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
