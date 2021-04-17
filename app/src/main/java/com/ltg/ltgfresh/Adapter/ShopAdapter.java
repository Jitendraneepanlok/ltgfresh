package com.ltg.ltgfresh.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ltg.ltgfresh.Pojo.Category;
import com.ltg.ltgfresh.Pojo.ShopResponse;
import com.ltg.ltgfresh.Pojo.SubCategory;
import com.ltg.ltgfresh.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {

    private Context mContext;
    private ShopResponse shopResponse;
    LayoutInflater inflter;
    private NavController navController;

    public ShopAdapter(Context applicationContext, ShopResponse shopResponse) {
        this.mContext = applicationContext;
        this.shopResponse = shopResponse;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView tv_name;
        public AppCompatImageView img_category;
        public CardView card_category;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (AppCompatTextView) view.findViewById(R.id.tv_name);
            img_category = (AppCompatImageView) view.findViewById(R.id.img_category);
            card_category = (CardView) view.findViewById(R.id.card_category);

        }
    }

    @Override
    public int getItemCount() {
        return shopResponse.getCategory().size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public ShopAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);
        return new ShopAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ShopAdapter.MyViewHolder holder, int position) {
        Category category = shopResponse.getCategory().get(position);
        holder.tv_name.setText(category.getName());
        Glide.with(mContext).load(category.getImage()).into(holder.img_category);
        /*// here pass the product id with product item position
        Bundle bundle = new Bundle();
        bundle.putString("Product_ID", sellingSoldDataResponse.getProducts().get(position).getId());
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_fragment);
                navController.navigate(R.id.action_homeFragment_to_itemDetailFragment, bundle);
            }
        });*/


        holder.card_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("Sub_Category", (Serializable) category.getSubCategory());



                navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_fragment);
                navController.navigate(R.id.action_slideshowFragment_to_subCategoryFragment,bundle);
            }
        });
    }
}
