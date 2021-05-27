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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ltg.ltgfresh.Adapter.SearchAdapter;
import com.ltg.ltgfresh.Adapter.WishListAdapter;
import com.ltg.ltgfresh.Helper.FragmentCommunication;
import com.ltg.ltgfresh.Helper.UpdateInterface;
import com.ltg.ltgfresh.NavigationDrawerActvity.ui.home.HomeFragment;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.SearchResponse;
import com.ltg.ltgfresh.Pojo.WishListResponse;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.SharedPrefrences.SessionManager;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;

public class WishListFragment extends Fragment{
    View view;
    RecyclerView recycler_wishlist;
    WishListAdapter wishListAdapter;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;
    String User_Id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        sessionManager = new SessionManager(getActivity());
        User_Id = sessionManager.getUserData(SessionManager.ID);

        initView();
        getWishList();

        return view;
    }


    private void initView() {
        recycler_wishlist = (RecyclerView) view.findViewById(R.id.recycler_wishlist);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recycler_wishlist.setLayoutManager(mLayoutManager);
        recycler_wishlist.addItemDecoration(new WishListFragment.GridSpacingItemDecoration(2, 1, true));
        recycler_wishlist.setItemAnimator(new DefaultItemAnimator());

    }

    private void getWishList() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<WishListResponse> call = apiService.getWishList(User_Id);
        try {
            call.enqueue(new Callback<WishListResponse>() {
                @Override
                public void onResponse(Call<WishListResponse> call, retrofit2.Response<WishListResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() ==true) {
                            Toast.makeText(getActivity(), String.valueOf(response.body().getStatus()), Toast.LENGTH_SHORT).show();
                            Log.e("Response", "" + String.valueOf(response.body().getStatus()));
                            wishListAdapter = new WishListAdapter(getActivity(), response.body());
                            recycler_wishlist.setAdapter(wishListAdapter);
                            wishListAdapter.notifyDataSetChanged();
                            pDialog.dismiss();
                        }else {
                            Toast.makeText(getActivity(), String.valueOf(response.body().getStatus()), Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();

                        }
                    } else {
                        Toast.makeText(getActivity(), String.valueOf(response.body().getStatus()), Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();                    }
                }

                @Override
                public void onFailure(Call<WishListResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                    Log.e("msg", "Failer" + t);
                    pDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            Log.e("LoginFailer", "" + ex);
            Toast.makeText(getActivity(), "" + ex, Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }

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
