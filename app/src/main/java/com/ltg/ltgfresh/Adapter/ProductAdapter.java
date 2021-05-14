package com.ltg.ltgfresh.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.ltg.ltgfresh.Activity.LoginActivity;
import com.ltg.ltgfresh.Helper.FragmentCommunication;
import com.ltg.ltgfresh.Helper.UpdateInterface;
import com.ltg.ltgfresh.Helper.Utility;
import com.ltg.ltgfresh.NavigationDrawerActvity.MainActivity;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.AddToCartFromHomeResponse;
import com.ltg.ltgfresh.Pojo.CartCountItem;
import com.ltg.ltgfresh.Pojo.LoginResponse;
import com.ltg.ltgfresh.Pojo.ProductData;
import com.ltg.ltgfresh.Pojo.ProductResponse;
import com.ltg.ltgfresh.Pojo.Rate;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.SharedPrefrences.SessionManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private final UpdateInterface listner;
    private Context mContext;
    private ProductResponse sellingSoldDataResponse;
    LayoutInflater inflter;
    private NavController navController;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;
    private FragmentCommunication mCommunicator;
    private String QUANTITY;


    public ProductAdapter(Context applicationContext, ProductResponse sellingSoldDataResponse, UpdateInterface listner, FragmentCommunication communication) {
        this.mContext = applicationContext;
        this.mCommunicator = communication;
        this.sellingSoldDataResponse = sellingSoldDataResponse;
        inflter = (LayoutInflater.from(applicationContext));
        this.listner = listner;
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
        ProductData productData = sellingSoldDataResponse.getProducts().get(position);
        holder.title.setText(productData.getName());
        if (productData.getRate().size() > 0) {
            holder.text_price.setText(mContext.getResources().getString(R.string.Rs) + " " + productData.getRate().get(0).getPrice());
        }
        Glide.with(mContext)
                .load(productData.getThumbnail())
                .into(holder.thumbnail);
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

        holder.btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* CartCountItem dcd = Utility.getCartCountItem();
                if (!dcd.getProduct_Id().contains(sellingSoldDataResponse.getProducts().get(position).getId())) {
                    ArrayList<String> list = dcd.getProduct_Id();
                    list.add(sellingSoldDataResponse.getProducts().get(position).getId());
                    dcd.setProduct_Id(list);
                }
                listner.recyclerviewOnUpdate(dcd.getProduct_Id().size());
                CallAddCartApi();*/

                holder.rl_increase_quantity.setVisibility(View.VISIBLE);
                holder.btn_addtocart.setVisibility(View.INVISIBLE);
            }
        });

        holder.text_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productData.getUserEnterQuantity() > 1) {
                    productData.setUserEnterQuantity(productData.getUserEnterQuantity() - 1);
                    QUANTITY = productData.getUserEnterQuantity().toString();
                    holder.text_quantity.setText(QUANTITY/*productData.getUserEnterQuantity().toString()*/);
                    productData.setTotalprice(productData.getTotalprice() - Integer.parseInt(productData.getRate().get(0).getPrice()));
                    Utility.grandtotal = Utility.grandtotal - Integer.parseInt(productData.getRate().get(0).getPrice());

                    holder.text_price.setText(mContext.getResources().getString(R.string.Rs) + " " + productData.getUserEnterQuantity() * Integer.parseInt(productData.getRate().get(0).getPrice()));
                    mCommunicator.respond(0, Utility.grandtotal);
                    CallAddCartApi();
                }
            }

        });

        holder.text_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productData.getUserEnterQuantity() <= Integer.parseInt(productData.getRate().get(0).getQuantity()))
                    productData.setUserEnterQuantity(productData.getUserEnterQuantity() + 1);
                QUANTITY = productData.getUserEnterQuantity().toString();
                holder.text_quantity.setText(QUANTITY/*productData.getUserEnterQuantity().toString()*/);
                productData.setTotalprice(productData.getTotalprice() + Integer.parseInt(productData.getRate().get(0).getPrice()));
                Utility.grandtotal = Utility.grandtotal + Integer.parseInt(productData.getRate().get(0).getPrice());
                holder.text_price.setText(mContext.getResources().getString(R.string.Rs) + " " + productData.getUserEnterQuantity() * Integer.parseInt(productData.getRate().get(0).getPrice()));

                mCommunicator.respond(0, Utility.grandtotal);
                CallAddCartApi();
            }

        });
    }

    private void CallAddCartApi() {
        Log.e("QUANTITY", "" + QUANTITY);
        sessionManager = new SessionManager(mContext);
        String Id = sessionManager.getUserData(SessionManager.ID);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AddToCartFromHomeResponse> call = apiService.addToCart(sellingSoldDataResponse.getProducts().get(0).getRate().get(0).getProductId(), Id, QUANTITY);
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