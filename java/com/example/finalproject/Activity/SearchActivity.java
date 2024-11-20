package com.example.finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Adapter.PopularAdapter;
import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.Helper.ManagementCart;
import com.example.finalproject.databinding.ActivitySearchBinding;
import com.example.finalproject.databinding.ActivitySearchBinding;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    private ActivitySearchBinding binding;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    private ArrayList<ItemsDomain> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchView = binding.searchview;
        mAdapter = new PopularAdapter(items);
        layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(mAdapter);
        search();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mAdapter = new PopularAdapter(items);
                ((PopularAdapter)mAdapter).updateData(items);
                binding.recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                filterData(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                filterData(newText);
                return false;
            }
        });

        mAdapter = new PopularAdapter(items);
        binding.recyclerView.setAdapter(mAdapter);
    }
    private void search(){
        Intent intent = getIntent();
        ArrayList<ItemsDomain> tempList = (ArrayList<ItemsDomain>)intent.getSerializableExtra("items");

        if (!tempList.isEmpty()){
            items.clear();
            items.addAll(tempList);
            mAdapter.notifyDataSetChanged();
            binding.recyclerView.setLayoutManager(new GridLayoutManager
                    (SearchActivity.this,2));
            binding.recyclerView.setNestedScrollingEnabled(true);
            binding.emptyTxt.setVisibility(View.GONE);
        }
        else {
            binding.emptyTxt.setVisibility(View.VISIBLE);
        }
    }
    private void filterData(String query) {
        ArrayList<ItemsDomain> filteredItems = new ArrayList<>();
        for (ItemsDomain item : items) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredItems.add(item);
            }
        }
        mAdapter = new PopularAdapter(filteredItems);
        binding.recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
