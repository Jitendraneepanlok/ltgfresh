package com.ltg.ltgfresh.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ltg.ltgfresh.Helper.FragmentCommunication;
import com.ltg.ltgfresh.Helper.UpdateInterface;
import com.ltg.ltgfresh.Helper.Utility;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.AddToCartFromHomeResponse;
import com.ltg.ltgfresh.Pojo.SearchProduct;
import com.ltg.ltgfresh.Pojo.SearchResponse;
import com.ltg.ltgfresh.Pojo.WishListData;
import com.ltg.ltgfresh.Pojo.WishListResponse;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.SharedPrefrences.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewHolder> {

    private Context mContext;
    private WishListResponse wishListResponse;
    LayoutInflater inflter;

    public WishListAdapter(Context applicationContext, WishListResponse wishListResponse) {
        this.mContext = applicationContext;
        this.wishListResponse = wishListResponse;
        inflter = (LayoutInflater.from(applicationContext));

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, text_price, btn_addtocart, text_minus, text_quantity, text_plus;
        public ImageView thumbnail;
        public RelativeLayout rl_increase_quantity;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            text_price = (TextView) view.findViewById(R.id.text_price);
            text_minus = (TextView) view.findViewById(R.id.text_minus);
            text_quantity = (TextView) view.findViewById(R.id.text_quantity);
            text_plus = (TextView) view.findViewById(R.id.text_plus);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            btn_addtocart = (TextView) view.findViewById(R.id.btn_addtocart);
            rl_increase_quantity = (RelativeLayout) view.findViewById(R.id.rl_increase_quantity);

        }
    }

    @Override
    public int getItemCount() {
        return wishListResponse.getWishlist().size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public WishListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_card, parent, false);
        return new WishListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WishListAdapter.MyViewHolder holder, int position) {
        WishListData wishListData = wishListResponse.getWishlist().get(position);
        holder.title.setText(wishListData.getProductName());
        Glide.with(mContext)
                .load(wishListData.getProductImage())
                .into(holder.thumbnail);
    }
}