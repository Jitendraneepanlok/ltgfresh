package com.ltg.ltgfresh.Fragments;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ltg.ltgfresh.Adapter.ProductAdapter;
import com.ltg.ltgfresh.Adapter.ShopAdapter;
import com.ltg.ltgfresh.Helper.UpdateInterface;
import com.ltg.ltgfresh.NavigationDrawerActvity.ui.home.HomeFragment;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.AddToCartFromHomeResponse;
import com.ltg.ltgfresh.Pojo.SubCategory;
import com.ltg.ltgfresh.Pojo.SubCategoryProductsResponse;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.SharedPrefrences.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SubCategoryFragment extends Fragment implements UpdateInterface {
    private NavController navController;
    View view;
    RecyclerView recycer_sub_category;
    ProgressDialog pDialog;
    Toolbar toolbar;
    AppCompatTextView tv_cart_count;
    AppCompatImageView img_cart,img_profile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        view = inflater.inflate(R.layout.fragment_sub_category, container, false);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        img_cart = (AppCompatImageView)toolbar.findViewById(R.id.img_cart);
        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_cartViewFragment);
            }
        });

        img_profile = (AppCompatImageView)toolbar.findViewById(R.id.img_profile);
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_profileFragment);
            }
        });
        tv_cart_count = (AppCompatTextView)toolbar.findViewById(R.id.tv_cart_count);
        intView();
        CallSubToCategoryApi();
        return view;
    }

    private void intView() {
        recycer_sub_category = (RecyclerView) view.findViewById(R.id.recycer_sub_category);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recycer_sub_category.setLayoutManager(mLayoutManager);
        recycer_sub_category.addItemDecoration(new SubCategoryFragment.GridSpacingItemDecoration(2, 1, true));
        recycer_sub_category.setItemAnimator(new DefaultItemAnimator());
    }

    private void CallSubToCategoryApi() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SubCategoryProductsResponse> call = apiService.GetSubCategoryProducts("cat","1");
        try {
            call.enqueue(new Callback<SubCategoryProductsResponse>() {
                @Override
                public void onResponse(Call<SubCategoryProductsResponse> call, retrofit2.Response<SubCategoryProductsResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e("cart_Response", "" + response.body().toString());
                        Toast.makeText(getActivity(), ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                        ShopAdapter shopAdapter = new ShopAdapter(getActivity(), response.body(),SubCategoryFragment.this::recyclerviewOnUpdate);
                        recycer_sub_category.setAdapter(shopAdapter);
                        shopAdapter.notifyDataSetChanged();
                        pDialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<SubCategoryProductsResponse> call, Throwable t) {
                    // Log error here since request failed
                    Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                    Log.e("Failer", "" + t);
                    pDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            Log.e("LoginFailer", "" + ex);
            Toast.makeText(getActivity(), "" + ex, Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }


    }

    @Override
    public void recyclerviewOnUpdate(int amount) {
        tv_cart_count.setText(String.valueOf(amount));
        Log.e("Count", "" + amount);

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}

