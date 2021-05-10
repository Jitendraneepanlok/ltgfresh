package com.ltg.ltgfresh.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ltg.ltgfresh.Helper.FragmentCommunication;
import com.ltg.ltgfresh.Helper.Utility;
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
    private FragmentCommunication mCommunicator;


    public CartListAdapter(Context applicationContext, CartListResponse cartListResponse,FragmentCommunication communication) {
        this.mContext = applicationContext;
        this.cartListResponse = cartListResponse;
        this.mCommunicator= communication;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView tv_name, tv_price;
        public AppCompatImageView img_category;
        public CardView card_category;
        public ImageView imgSub, imgAdd;
        TextView txt_quantity;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (AppCompatTextView) view.findViewById(R.id.tv_name);
            img_category = (AppCompatImageView) view.findViewById(R.id.img_category);
            card_category = (CardView) view.findViewById(R.id.card_category);
            tv_price = (AppCompatTextView) view.findViewById(R.id.tv_price);
            txt_quantity = (TextView) view.findViewById(R.id.txt_quantity);
            imgSub = (ImageView) view.findViewById(R.id.imgSub);
            imgAdd = (ImageView) view.findViewById(R.id.imgAdd);

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
        holder.tv_name.setText(cartListData.getName());
        Glide.with(mContext).load(cartListData.getThumbnail()).into(holder.img_category);
        if (cartListData.getRate().get(0).getQuantity() != null && cartListData.getRate().get(0).getUnitIn() != null && cartListData.getRate().get(0).getPrice() != null) {
            holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + "" + cartListData.getRate().get(0).getPrice());
            holder.txt_quantity.setText(cartListData.getRate().get(0).getQuantity() + "" + cartListData.getRate().get(0).getUnitIn());
        }

        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartListData.getUserEnterQuantity() <= Integer.parseInt(cartListData.getRate().get(0).getQuantity()))
                    cartListData.setUserEnterQuantity(cartListData.getUserEnterQuantity() + 1);
                    holder.txt_quantity.setText(cartListData.getUserEnterQuantity().toString());
                cartListData.setTotalprice(cartListData.getTotalprice()+Integer.parseInt(cartListData.getRate().get(0).getPrice()));
                Utility.grandtotal=Utility.grandtotal+Integer.parseInt(cartListData.getRate().get(0).getPrice());
                    mCommunicator.respond(0,Utility.grandtotal);
            }
        });

        holder.imgSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartListData.getUserEnterQuantity() > 1) {
                    cartListData.setUserEnterQuantity(cartListData.getUserEnterQuantity() - 1);
                    holder.txt_quantity.setText(cartListData.getUserEnterQuantity().toString());
                    cartListData.setTotalprice(cartListData.getTotalprice()-Integer.parseInt(cartListData.getRate().get(0).getPrice()));
                    Utility.grandtotal=Utility.grandtotal-Integer.parseInt(cartListData.getRate().get(0).getPrice());

                    mCommunicator.respond(0,Utility.grandtotal);
                }
            }
        });
    }
}

