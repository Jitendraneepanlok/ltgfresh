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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ltg.ltgfresh.Helper.FragmentCommunication;
import com.ltg.ltgfresh.Helper.Utility;
import com.ltg.ltgfresh.NavigationDrawerActvity.ui.home.HomeFragment;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.CartItemRemoveResponse;
import com.ltg.ltgfresh.Pojo.CartListData;
import com.ltg.ltgfresh.Pojo.CartListResponse;
import com.ltg.ltgfresh.Pojo.Category;
import com.ltg.ltgfresh.Pojo.SearchResponse;
import com.ltg.ltgfresh.Pojo.ShopResponse;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.SharedPrefrences.SessionManager;

import java.io.Serializable;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {

    private Context mContext;
    private CartListResponse cartListResponse;
    LayoutInflater inflter;
    private FragmentCommunication mCommunicator;
    private String CartID, User_Id;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;

    public CartListAdapter(Context applicationContext, CartListResponse cartListResponse, FragmentCommunication communication) {
        this.mContext = applicationContext;
        this.cartListResponse = cartListResponse;
        this.mCommunicator = communication;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView tv_name, tv_price, tv_remove;
        public AppCompatImageView img_category, img_delete;
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
            img_delete = (AppCompatImageView) view.findViewById(R.id.img_delete);
            tv_remove = (AppCompatTextView) view.findViewById(R.id.tv_remove);

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
        CartID = cartListData.getId();
        Glide.with(mContext).load(cartListData.getThumbnail()).into(holder.img_category);
        if (cartListData.getRate().get(0).getQuantity() != null && cartListData.getRate().get(0).getUnitIn() != null && cartListData.getRate().get(0).getPrice() != null) {
            holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + "" + cartListData.getRate().get(0).getPrice());
//            holder.txt_quantity.setText(cartListData.getRate().get(0).getQuantity() + "" + cartListData.getRate().get(0).getUnitIn());
        }

        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartListData.getUserEnterQuantity() <= Integer.parseInt(cartListData.getRate().get(0).getQuantity()))
                    cartListData.setUserEnterQuantity(cartListData.getUserEnterQuantity() + 1);
                holder.txt_quantity.setText(cartListData.getUserEnterQuantity().toString());
                cartListData.setTotalprice(cartListData.getTotalprice() + Integer.parseInt(cartListData.getRate().get(0).getPrice()));
                Utility.grandtotal = Utility.grandtotal + Integer.parseInt(cartListData.getRate().get(0).getPrice());
                mCommunicator.respond(0, Utility.grandtotal);
            }
        });

        holder.imgSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartListData.getUserEnterQuantity() > 1) {
                    cartListData.setUserEnterQuantity(cartListData.getUserEnterQuantity() - 1);
                    holder.txt_quantity.setText(cartListData.getUserEnterQuantity().toString());
                    cartListData.setTotalprice(cartListData.getTotalprice() - Integer.parseInt(cartListData.getRate().get(0).getPrice()));
                    Utility.grandtotal = Utility.grandtotal - Integer.parseInt(cartListData.getRate().get(0).getPrice());

                    mCommunicator.respond(0, Utility.grandtotal);
                }
            }
        });

        holder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallRemoveProductApi();
                int newPosition = holder.getAdapterPosition();
                Log.d("thien.van","on Click onBindViewHolder");
                cartListResponse.getCartlist().remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition,cartListResponse.getCartlist().size());
            }
        });

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallRemoveProductApi();
                int newPosition = holder.getAdapterPosition();
                Log.d("thien.van","on Click onBindViewHolder");
                cartListResponse.getCartlist().remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition,cartListResponse.getCartlist().size());
            }
        });
    }

    private void CallRemoveProductApi() {
        sessionManager = new SessionManager(mContext);
        User_Id = sessionManager.getUserData(SessionManager.ID);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CartItemRemoveResponse> call = apiService.getRemoveCartProducts(User_Id, CartID);
        try {
            call.enqueue(new Callback<CartItemRemoveResponse>() {
                @Override
                public void onResponse(Call<CartItemRemoveResponse> call, retrofit2.Response<CartItemRemoveResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                        pDialog.dismiss();

                    } else {
                        pDialog.cancel();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<CartItemRemoveResponse>() {
                            }.getType();
                            CartItemRemoveResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                            Log.e("errorResponse", String.valueOf(errorResponse.getStatus()));
                            Toast.makeText(mContext, String.valueOf(errorResponse.getStatus()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("Failer_Exception", "" + e);

                        }
                    }
                }

                @Override
                public void onFailure(Call<CartItemRemoveResponse> call, Throwable t) {
                    Toast.makeText(mContext, "" + t, Toast.LENGTH_SHORT).show();
                    Log.e("Failer", "" + t);
                    pDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            Log.e("LoginFailer", "" + ex);
            Toast.makeText(mContext, "" + ex, Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }

    }


}

