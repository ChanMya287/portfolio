package com.example.finalproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ViewholderSizeBinding;

import java.util.ArrayList;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {
    ArrayList<String> items;
    Context context;
    int selectPosition =-1;
    int lastSelectedPosition = -1;
    String selectedSize = "";
    public SizeAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SizeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        ViewholderSizeBinding binding = ViewholderSizeBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeAdapter.ViewHolder holder, int position) {
        holder.binding.sizeTxt.setText(items.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition=selectPosition;
                selectPosition=holder.getAdapterPosition();
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectPosition);
                selectedSize = items.get(selectPosition);
            }
        });
        if (selectPosition==holder.getAdapterPosition()){
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_selected);
            holder.binding.sizeTxt.setTextColor(context.getResources().getColor(R.color.purple));
        }else {
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_unselected);
            holder.binding.sizeTxt.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ViewholderSizeBinding binding;
        public ViewHolder(@NonNull ViewholderSizeBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
    public String getSelectedSize() {
        return selectedSize;
    }
}
