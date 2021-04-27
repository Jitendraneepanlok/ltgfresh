package com.ltg.ltgfresh.NavigationDrawerActvity.ui.slideshow;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ltg.ltgfresh.Adapter.ProductAdapter;
import com.ltg.ltgfresh.Adapter.ShopAdapter;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.ProductResponse;
import com.ltg.ltgfresh.Pojo.ShopResponse;
import com.ltg.ltgfresh.R;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;


public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    RecyclerView shop_recycler;
    ShopAdapter adapter;
    View root;
    ProgressDialog pDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        /*final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        initView();
        getShopList();
        getOrganicProduct();

        return root;
    }
    private void initView() {
        shop_recycler = (RecyclerView)root.findViewById(R.id.shop_recycler);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        shop_recycler.setLayoutManager(verticalLayoutManager);
        shop_recycler.setItemAnimator(new DefaultItemAnimator());
    }

    private void getShopList() {
        pDialog = new ProgressDialog(getActivity());
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
                        Toast.makeText(getActivity(), String.valueOf(response.body().getStatus()), Toast.LENGTH_SHORT).show();
                        adapter = new ShopAdapter(getActivity(),response.body());
                        shop_recycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        pDialog.dismiss();

                    } else {
                        pDialog.cancel();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<ShopResponse>() {
                            }.getType();
                            ShopResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                            Log.e("errorResponse", String.valueOf(errorResponse.getStatus()));
                            Toast.makeText(getActivity(), String.valueOf(errorResponse.getStatus()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<ShopResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                    Log.e("", "Failer" + t);
                    pDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            Log.e("LoginFailer", "" + ex);
            Toast.makeText(getActivity(), "" + ex, Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }

    }

    private void getOrganicProduct() {

    }
}