package com.ltg.ltgfresh.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ltg.ltgfresh.Adapter.ProductAdapter;
import com.ltg.ltgfresh.NavigationDrawerActvity.MainActivity;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.ProductResponse;
import com.ltg.ltgfresh.Pojo.SingleProductResponse;
import com.ltg.ltgfresh.R;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;

public class ItemDetailsFragment extends Fragment {
    private NavController navController;
    AppCompatButton btn_addtocart, btn_buynow;
    String Product_ID;
    private ProgressDialog pDialog;
    AppCompatTextView tv_title_name, tv_price, tv_description, tv_benefite, tv_disclimer, tv_review;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_detail, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.item_details);

        // Here get product ID from Home page Selected product item from list
        Product_ID = getArguments().getString("Product_ID");
        Log.e("ID", "" + Product_ID);

        initView();
        getSingleProductDetails();
        return view;
    }

    private void initView() {

        tv_title_name = (AppCompatTextView) view.findViewById(R.id.tv_title_name);
        tv_price = (AppCompatTextView) view.findViewById(R.id.tv_price);
        tv_description = (AppCompatTextView) view.findViewById(R.id.tv_description);
        tv_benefite = (AppCompatTextView) view.findViewById(R.id.tv_benefite);
        tv_disclimer = (AppCompatTextView) view.findViewById(R.id.tv_disclimer);
        tv_review = (AppCompatTextView) view.findViewById(R.id.tv_review);

        btn_addtocart = (AppCompatButton) view.findViewById(R.id.btn_addtocart);
        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_addtocart.setTextColor(getResources().getColor(R.color.white));
                btn_addtocart.setBackgroundColor(getResources().getColor(R.color.purple_500));

                btn_buynow.setTextColor(getResources().getColor(R.color.black));
                btn_buynow.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        btn_buynow = (AppCompatButton) view.findViewById(R.id.btn_buynow);
        btn_buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_buynow.setTextColor(getResources().getColor(R.color.white));
                btn_buynow.setBackgroundColor(getResources().getColor(R.color.purple_500));

                btn_addtocart.setTextColor(getResources().getColor(R.color.black));
                btn_addtocart.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
    }

    private void getSingleProductDetails() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SingleProductResponse> call = apiService.getSingleProduct(Product_ID);
        try {
            call.enqueue(new Callback<SingleProductResponse>() {
                @Override
                public void onResponse(Call<SingleProductResponse> call, retrofit2.Response<SingleProductResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), String.valueOf(response.body().getStatus()), Toast.LENGTH_SHORT).show();

                        tv_title_name.setText(Html.fromHtml(response.body().getProducts().get(0).getName()));
                        tv_price.setText("100"/*response.body().getProducts().get(0).getPrice()*/);
                        tv_description.setText(Html.fromHtml(response.body().getProducts().get(0).getDescription()));
                        tv_benefite.setText(Html.fromHtml(response.body().getProducts().get(0).getBenefits()));
                        tv_disclimer.setText(Html.fromHtml(response.body().getProducts().get(0).getDisclaimer()));
                        tv_review.setText(Html.fromHtml(response.body().getProducts().get(0).getReviews()));
                        pDialog.dismiss();

                    } else {
                        pDialog.cancel();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<SingleProductResponse>() {
                            }.getType();
                            SingleProductResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                            Log.e("errorResponse", String.valueOf(errorResponse.getStatus()));
                            Toast.makeText(getActivity(), String.valueOf(errorResponse.getStatus()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<SingleProductResponse> call, Throwable t) {
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
