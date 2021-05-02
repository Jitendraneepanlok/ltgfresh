package com.ltg.ltgfresh.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ltg.ltgfresh.Pojo.CartListData;
import com.ltg.ltgfresh.Pojo.CartListResponse;
import com.ltg.ltgfresh.Pojo.Category;
import com.ltg.ltgfresh.Pojo.ShopResponse;
import com.ltg.ltgfresh.R;

import java.io.Serializable;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {

    private Context mContext;
    private CartListResponse cartListResponse;
    LayoutInflater inflter;

    public CartListAdapter(Context applicationContext, CartListResponse cartListResponse) {
        this.mContext = applicationContext;
        this.cartListResponse = cartListResponse;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView tv_name,tv_price;
        public AppCompatImageView img_category;
        public CardView card_category;
        TextView txt_quantity;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (AppCompatTextView) view.findViewById(R.id.tv_name);
            img_category = (AppCompatImageView) view.findViewById(R.id.img_category);
            card_category = (CardView) view.findViewById(R.id.card_category);
            tv_price = (AppCompatTextView)view.findViewById(R.id.tv_price);
            txt_quantity = (TextView)view.findViewById(R.id.txt_quantity);

        }
    }

    @Override
    public int getItemCount() {
        return cartListResponse.getCartlist().size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public CartListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_data_layout, parent, false);
        return new CartListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CartListAdapter.MyViewHolder holder, int position) {
        CartListData cartListData = cartListResponse.getCartlist().get(position);
        holder.tv_name.setText(cartListData.getProductName());
        Glide.with(mContext).load(cartListData.getProductImage()).into(holder.img_category);
        holder.tv_price.setText(mContext.getResources().getString(R.string.Rs));
        holder.txt_quantity.setText(cartListData.getQty());

    }
}

