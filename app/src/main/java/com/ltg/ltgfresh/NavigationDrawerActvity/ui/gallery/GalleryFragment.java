package com.ltg.ltgfresh.NavigationDrawerActvity.ui.gallery;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ltg.ltgfresh.Adapter.OffersAdapter;
import com.ltg.ltgfresh.Adapter.ShopAdapter;
import com.ltg.ltgfresh.NavigationDrawerActvity.ui.home.HomeFragment;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.OffersResponse;
import com.ltg.ltgfresh.Pojo.ShopResponse;
import com.ltg.ltgfresh.R;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;


public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    ProgressDialog pDialog;
    View root;
    OffersAdapter adapter;
    RecyclerView offers_recycler;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        root = inflater.inflate(R.layout.fragment_gallery, container, false);
       /* final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        initView();
        getOffers();
        return root;
    }

    private void initView() {
        offers_recycler = (RecyclerView) root.findViewById(R.id.offers_recycler);
       /* RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        offers_recycler.setLayoutManager(mLayoutManager);
        offers_recycler.addItemDecoration(new GalleryFragment.GridSpacingItemDecoration(2, 1, true));
        offers_recycler.setItemAnimator(new DefaultItemAnimator());*/

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        offers_recycler.setLayoutManager(verticalLayoutManager);
        offers_recycler.setItemAnimator(new DefaultItemAnimator());
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


    private void getOffers() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<OffersResponse> call = apiService.getoffers();
        try {
            call.enqueue(new Callback<OffersResponse>() {
                @Override
                public void onResponse(Call<OffersResponse> call, retrofit2.Response<OffersResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), String.valueOf(response.body().getStatus()), Toast.LENGTH_SHORT).show();
                        adapter = new OffersAdapter(getActivity(),response.body());
                        offers_recycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        pDialog.dismiss();

                    } else {
                        pDialog.cancel();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<OffersResponse>() {
                            }.getType();
                            OffersResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                            Log.e("errorResponse", String.valueOf(errorResponse.getStatus()));
                            Toast.makeText(getActivity(), String.valueOf(errorResponse.getStatus()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<OffersResponse> call, Throwable t) {
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

}