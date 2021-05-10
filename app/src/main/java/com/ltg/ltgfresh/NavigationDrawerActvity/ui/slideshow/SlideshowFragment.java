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
import com.ltg.ltgfresh.Activity.CategoryActivity;
import com.ltg.ltgfresh.Adapter.NewShopAdapter;
import com.ltg.ltgfresh.Adapter.ProductAdapter;
import com.ltg.ltgfresh.Adapter.ShopAdapter;
import com.ltg.ltgfresh.Helper.ChildView;
import com.ltg.ltgfresh.Helper.Genre;
import com.ltg.ltgfresh.Helper.HeaderView;
import com.ltg.ltgfresh.Helper.MyRecyclerListener;
import com.ltg.ltgfresh.NavigationDrawerActvity.MainActivity;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.Category;
import com.ltg.ltgfresh.Pojo.ProductResponse;
import com.ltg.ltgfresh.Pojo.ShopResponse;
import com.ltg.ltgfresh.Pojo.SubCategory;
import com.ltg.ltgfresh.R;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;


public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    View root;
    private Map<String, List<ShopResponse>> categoryMap;
    private List<ShopResponse> shopList;
    private ExpandablePlaceHolderView expandablePlaceHolderView;
    ProgressDialog pDialog;
    String Category_No;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        Category_No = getArguments().getString("Category_No");
        Log.e("Category_No", "" +  MainActivity.value);

        getShopList();
        initView();

        return root;
    }

    private void initView() {
        shopList = new ArrayList<>();
        categoryMap = new HashMap<>();
        expandablePlaceHolderView = (ExpandablePlaceHolderView) root.findViewById(R.id.expandablePlaceHolder);
        expandablePlaceHolderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Toast.makeText(getActivity(), "Clixcked", view.getId()).show();
            }
        });
    }

    private void getShopList() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ShopResponse> call = apiService.getCategory(MainActivity.value);
        try {
            call.enqueue(new Callback<ShopResponse>() {
                @Override
                public void onResponse(Call<ShopResponse> call, retrofit2.Response<ShopResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), String.valueOf(response.body().getStatus()), Toast.LENGTH_SHORT).show();
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

    private void getHeaderAndChild(List<Category> category) {

        for (Category shopResponse : category) {
            expandablePlaceHolderView.addView(new HeaderView(getActivity(), shopResponse.getName(), shopResponse.getImage()));

            for (SubCategory movie : shopResponse.getSubCategory()) {
                expandablePlaceHolderView.addView(new ChildView(getActivity(), movie.getName()));
            }
        }
    }
}
