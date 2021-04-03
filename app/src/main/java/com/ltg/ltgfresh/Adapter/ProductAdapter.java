package com.ltg.ltgfresh.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ltg.ltgfresh.Pojo.ProductData;
import com.ltg.ltgfresh.Pojo.ProductResponse;
import com.ltg.ltgfresh.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context mContext;
    private ProductResponse sellingSoldDataResponse;
    LayoutInflater inflter;
    private NavController navController;

    public ProductAdapter(Context applicationContext, ProductResponse sellingSoldDataResponse) {
        this.mContext = applicationContext;
        this.sellingSoldDataResponse = sellingSoldDataResponse;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

        }
    }

    @Override
    public int getItemCount() {
        return sellingSoldDataResponse.getProducts().size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card, parent, false);
        return new ProductAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductAdapter.MyViewHolder holder, int position) {
        ProductData superCatModel = sellingSoldDataResponse.getProducts().get(position);
        holder.title.setText(superCatModel.getName());
        // here pass the product id with product item position
        Bundle bundle = new Bundle();
        bundle.putString("Product_ID", sellingSoldDataResponse.getProducts().get(position).getId());
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_fragment);
                navController.navigate(R.id.action_homeFragment_to_itemDetailFragment, bundle);
            }
        });
    }
}