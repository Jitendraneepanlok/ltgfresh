package com.ltg.ltgfresh.NavigationDrawerActvity.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ltg.ltgfresh.Adapter.ProductAdapter;
import com.ltg.ltgfresh.Adapter.SlidingImage_Adapter;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.BannerResponse;
import com.ltg.ltgfresh.Pojo.ProductData;
import com.ltg.ltgfresh.Pojo.ProductResponse;
import com.viewpagerindicator.CirclePageIndicator;
import com.ltg.ltgfresh.R;

import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment {
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.banner_images, R.drawable.banner_images, R.drawable.banner_images};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private ViewPager view_pager_ads;
    private HomeViewModel homeViewModel;
    View root;
    private ProgressDialog pDialog;
    private ProductAdapter productAdapter;
    private RecyclerView product_recycler;
    private CirclePageIndicator indicator;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
      /*  final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        FirstViewPager();
        getProductList();
        getBannerImages();
        initView();
        return root;
    }

    private void initView() {
        view_pager_ads = (ViewPager) root.findViewById(R.id.view_pager_ads);
        indicator = (CirclePageIndicator) root.findViewById(R.id.indicator);

        product_recycler = (RecyclerView) root.findViewById(R.id.product_recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        product_recycler.setLayoutManager(mLayoutManager);
        product_recycler.addItemDecoration(new GridSpacingItemDecoration(2, 1, true));
        product_recycler.setItemAnimator(new DefaultItemAnimator());
    }

    private void FirstViewPager() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

//        view_pager_ads.setAdapter(new SlidingImage_Adapter(getActivity(), ImagesArray));
//        indicator.setViewPager(view_pager_ads);
      /*  final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        NUM_PAGES = IMAGES.length;
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                view_pager_ads.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });*/
    }

    private void getBannerImages() {
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Please wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BannerResponse> call = apiService.getBanner();
        try {
            call.enqueue(new Callback<BannerResponse>() {
                @Override
                public void onResponse(Call<BannerResponse> call, retrofit2.Response<BannerResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), String.valueOf(response.body().getStatus()), Toast.LENGTH_SHORT).show();
                        Log.e("res",""+response.body().getStatus());

                        view_pager_ads.setAdapter(new SlidingImage_Adapter(getActivity(),response.body().getBanner()));
                        indicator.setViewPager(view_pager_ads);
                        final float density = getResources().getDisplayMetrics().density;
                        indicator.setRadius(5 * density);
                        NUM_PAGES = IMAGES.length;
                        // Auto start of viewpager
                        final Handler handler = new Handler();
                        final Runnable Update = new Runnable() {
                            public void run() {
                                if (currentPage == NUM_PAGES) {
                                    currentPage = 0;
                                }
                                view_pager_ads.setCurrentItem(currentPage++, true);
                            }
                        };
                        Timer swipeTimer = new Timer();
                        swipeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(Update);
                            }
                        }, 3000, 3000);

                        // Pager listener over indicator
                        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                            @Override
                            public void onPageSelected(int position) {
                                currentPage = position;

                            }

                            @Override
                            public void onPageScrolled(int pos, float arg1, int arg2) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int pos) {

                            }
                        });
//                        pDialog.dismiss();

                    } else {
                        Log.e("res",""+response.body().getStatus());
                      //  pDialog.dismiss();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<ProductResponse>() {
                            }.getType();
                            BannerResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                            Log.e("errorResponse", String.valueOf(errorResponse.getStatus()));
                            Toast.makeText(getActivity(), String.valueOf(errorResponse.getStatus()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<BannerResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                    Log.e("", "Failer" + t);
                   // pDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            Log.e("LoginFailer", "" + ex);
            Toast.makeText(getActivity(), "" + ex, Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }
    }

    private void getProductList() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductResponse> call = apiService.getProductList();
        try {
            call.enqueue(new Callback<ProductResponse>() {
                @Override
                public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), String.valueOf(response.body().getStatus()), Toast.LENGTH_SHORT).show();
                        productAdapter = new ProductAdapter(getActivity(), response.body());
                        product_recycler.setAdapter(productAdapter);
                        productAdapter.notifyDataSetChanged();
                        pDialog.dismiss();

                    } else {
                        pDialog.cancel();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<ProductResponse>() {
                            }.getType();
                            ProductResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                            Log.e("errorResponse", String.valueOf(errorResponse.getStatus()));
                            Toast.makeText(getActivity(), String.valueOf(errorResponse.getStatus()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductResponse> call, Throwable t) {
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