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

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ltg.ltgfresh.Helper.UpdateInterface;
import com.ltg.ltgfresh.Helper.Utility;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.AddToCartFromHomeResponse;
import com.ltg.ltgfresh.Pojo.CartCountItem;
import com.ltg.ltgfresh.Pojo.ProductData;
import com.ltg.ltgfresh.Pojo.ProductResponse;
import com.ltg.ltgfresh.Pojo.SearchProduct;
import com.ltg.ltgfresh.Pojo.SearchResponse;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.SharedPrefrences.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private final UpdateInterface listner;
    private Context mContext;
    private SearchResponse searchResponse;
    LayoutInflater inflter;
    private NavController navController;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;

    public SearchAdapter(Context applicationContext, SearchResponse searchResponse, UpdateInterface listner) {
        this.mContext = applicationContext;
        this.searchResponse = searchResponse;
        inflter = (LayoutInflater.from(applicationContext));
        this.listner = listner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price, btn_addtocart;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            btn_addtocart = (TextView) view.findViewById(R.id.btn_addtocart);

        }
    }

    @Override
    public int getItemCount() {
        return searchResponse.getProducts().size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card, parent, false);
        return new SearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SearchAdapter.MyViewHolder holder, int position) {
        SearchProduct searchProduct = searchResponse.getProducts().get(position);
        holder.title.setText(searchProduct.getName());
        if (searchProduct.getRate().size()>0){
            holder.price.setText(mContext.getResources().getString(R.string.Rs) + " " + searchProduct.getRate().get(0).getPrice());
        }
        Glide.with(mContext)
                .load(searchProduct.getThumbnail())
                .into(holder.thumbnail);
        // here pass the product id with product item position
        Bundle bundle = new Bundle();
        bundle.putString("Product_ID", searchResponse.getProducts().get(position).getId());

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_fragment);
                navController.navigate(R.id.action_homeFragment_to_itemDetailFragment, bundle);
            }
        });

        holder.btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartCountItem dcd = Utility.getCartCountItem();
                if (!dcd.getProduct_Id().contains(searchResponse.getProducts().get(position).getId())) {
                    ArrayList<String> list = dcd.getProduct_Id();
                    list.add(searchResponse.getProducts().get(position).getId());
                    dcd.setProduct_Id(list);
                }
                listner.recyclerviewOnUpdate(dcd.getProduct_Id().size());
                CallAddCartApi();
            }
        });
    }

    private void CallAddCartApi() {
        sessionManager = new SessionManager(mContext);
        String Id = sessionManager.getUserData(SessionManager.ID);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AddToCartFromHomeResponse> call = apiService.addToCart(searchResponse.getProducts().get(0).getRate().get(0).getProductId(), Id, searchResponse.getProducts().get(0).getRate().get(0).getQuantity());
        try {
            call.enqueue(new Callback<AddToCartFromHomeResponse>() {
                @Override
                public void onResponse(Call<AddToCartFromHomeResponse> call, retrofit2.Response<AddToCartFromHomeResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e("cart_Response", "" + response.body().toString());
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                        pDialog.dismiss();
                    } else {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<AddToCartFromHomeResponse> call, Throwable t) {
                    // Log error here since request failed
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
