package com.ltg.ltgfresh.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ltg.ltgfresh.Adapter.NewShopAdapter;
import com.ltg.ltgfresh.Adapter.ShopAdapter;
import com.ltg.ltgfresh.Helper.ChildView;
import com.ltg.ltgfresh.Helper.HeaderView;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.Category;
import com.ltg.ltgfresh.Pojo.ShopResponse;
import com.ltg.ltgfresh.Pojo.SubCategory;
import com.ltg.ltgfresh.R;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class CategoryActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    private Map<String, List<ShopResponse>> categoryMap;
    private List<ShopResponse> shopList;
    private ExpandablePlaceHolderView expandablePlaceHolderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        shopList = new ArrayList<>();
        categoryMap = new HashMap<>();
        expandablePlaceHolderView = (ExpandablePlaceHolderView) findViewById(R.id.expandablePlaceHolder);
        expandablePlaceHolderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Toast.makeText(getApplicationContext(), "Clixcked", view.getId()).show();
            }
        });
     //   getShopList();
    }


/*
    private void getShopList() {
        pDialog = new ProgressDialog(CategoryActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ShopResponse> call = apiService.getCategory();
        try {
            call.enqueue(new Callback<ShopResponse>() {
                @Override
                public void onResponse(Call<ShopResponse> call, retrofit2.Response<ShopResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CategoryActivity.this, String.valueOf(response.body().getStatus()), Toast.LENGTH_SHORT).show();
//                        shopList = Collections.singletonList(response.body());
                        getHeaderAndChild(response.body().getCategory());
                        pDialog.dismiss();

                    } else {
                        pDialog.cancel();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<ShopResponse>() {
                            }.getType();
                            ShopResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                            Log.e("errorResponse", String.valueOf(errorResponse.getStatus()));
                            Toast.makeText(CategoryActivity.this, String.valueOf(errorResponse.getStatus()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<ShopResponse> call, Throwable t) {
                    Toast.makeText(CategoryActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                    Log.e("", "Failer" + t);
                    pDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            Log.e("LoginFailer", "" + ex);
            Toast.makeText(CategoryActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }
    }
*/

    private void getHeaderAndChild(List<Category> category){

        for (Category shopResponse :category){
            expandablePlaceHolderView.addView(new HeaderView(this,shopResponse.getName(),shopResponse.getImage()));

            for (SubCategory movie : shopResponse.getSubCategory()){
                expandablePlaceHolderView.addView(new ChildView(this, movie.getName()));
            }
        }
    }
}