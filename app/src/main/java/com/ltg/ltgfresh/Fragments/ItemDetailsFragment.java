package com.ltg.ltgfresh.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ltg.ltgfresh.Adapter.ProductAdapter;
import com.ltg.ltgfresh.Helper.FragmentCommunication;
import com.ltg.ltgfresh.Helper.Utility;
import com.ltg.ltgfresh.NavigationDrawerActvity.MainActivity;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.AddToCartFromHomeResponse;
import com.ltg.ltgfresh.Pojo.ProductResponse;
import com.ltg.ltgfresh.Pojo.SingleProductData;
import com.ltg.ltgfresh.Pojo.SingleProductResponse;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.SharedPrefrences.SessionManager;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;

public class ItemDetailsFragment extends Fragment implements FragmentCommunication {
    private NavController navController;
    AppCompatButton btn_addtocart, btn_buynow;
    String Product_ID;
    private ProgressDialog pDialog;
    AppCompatTextView tv_title_name, tv_price, tv_description, tv_benefite, tv_disclimer, tv_review, tv_unit;
    AppCompatImageView img_product, img_cart, img_profile;
    TextView txt_quantity;
    View view;
    private SessionManager sessionManager;
    String Product_id, Quantity;
    ImageView imgSub, imgAdd;
    private FragmentCommunication mCommunicator;
    Toolbar toolbar;
    int quantity = 1;
    String Price;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_detail, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        toolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);

        img_cart = (AppCompatImageView) toolbar.findViewById(R.id.img_cart);
        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_cartViewFragment);
            }
        });

        img_profile = (AppCompatImageView) toolbar.findViewById(R.id.img_profile);
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_profileFragment);
            }
        });
        // Here get product ID from Home page Selected product item from list
        Product_ID = getArguments().getString("Product_ID");

        initView();
        getSingleProductDetails();
        return view;
    }

    private void initView() {
        tv_unit = (AppCompatTextView) view.findViewById(R.id.tv_unit);
        txt_quantity = (TextView) view.findViewById(R.id.txt_quantity);
        tv_title_name = (AppCompatTextView) view.findViewById(R.id.tv_title_name);
        tv_price = (AppCompatTextView) view.findViewById(R.id.tv_price);
        tv_description = (AppCompatTextView) view.findViewById(R.id.tv_description);
        tv_benefite = (AppCompatTextView) view.findViewById(R.id.tv_benefite);
        tv_disclimer = (AppCompatTextView) view.findViewById(R.id.tv_disclimer);
        tv_review = (AppCompatTextView) view.findViewById(R.id.tv_review);
        img_product = (AppCompatImageView) view.findViewById(R.id.img_product);
        btn_addtocart = (AppCompatButton) view.findViewById(R.id.btn_addtocart);
        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_addtocart.setTextColor(getResources().getColor(R.color.white));
                btn_addtocart.setBackgroundColor(getResources().getColor(R.color.purple_500));

                btn_buynow.setTextColor(getResources().getColor(R.color.black));
                btn_buynow.setBackgroundColor(getResources().getColor(R.color.white));
                CallAddToCartApi();
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

                navController.navigate(R.id.action_cartViewFragment_to_paymentFragment);
            }
        });

        imgAdd = (ImageView) view.findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity >= 1) {
                    quantity += 1;
                    txt_quantity.setText(String.valueOf(quantity));
                    int TotalPrice = Integer.parseInt(Price);
                    int GrandTotal = TotalPrice * quantity;
                    tv_price.setText(String.valueOf(GrandTotal));
                }
            }
        });

        imgSub = (ImageView) view.findViewById(R.id.imgSub);
        imgSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity >= 2) {
                    quantity -= 1;
                    txt_quantity.setText(String.valueOf(quantity));
                    int TotalPrice = Integer.parseInt(Price);
                    int GrandTotal = TotalPrice * quantity;
                    tv_price.setText(String.valueOf(GrandTotal));
                }
            }
        });
    }

    private void CallAddToCartApi() {
        sessionManager = new SessionManager(getActivity());
        String Id = sessionManager.getUserData(SessionManager.ID);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AddToCartFromHomeResponse> call = apiService.addToCart(Product_ID, Id, String.valueOf(quantity));
        try {
            call.enqueue(new Callback<AddToCartFromHomeResponse>() {
                @Override
                public void onResponse(Call<AddToCartFromHomeResponse> call, retrofit2.Response<AddToCartFromHomeResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e("cart_Response", "" + response.body().toString());
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();


                        pDialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        pDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<AddToCartFromHomeResponse> call, Throwable t) {
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

                        tv_unit.setText(response.body().getProducts().get(0).getRate().get(0).getUnit() + "" + response.body().getProducts().get(0).getRate().get(0).getUnitIn());
                        Product_id = response.body().getProducts().get(0).getRate().get(0).getProductId();
                        Quantity = response.body().getProducts().get(0).getRate().get(0).getQuantity();
                        tv_title_name.setText(Html.fromHtml(response.body().getProducts().get(0).getName()));
                        Price = response.body().getProducts().get(0).getRate().get(0).getPrice();
                        tv_price.setText(Price);
                        tv_description.setText(Html.fromHtml(response.body().getProducts().get(0).getDescription()));
                        tv_benefite.setText(Html.fromHtml(response.body().getProducts().get(0).getBenefits()));
                        tv_disclimer.setText(Html.fromHtml(response.body().getProducts().get(0).getDisclaimer()));
                        tv_review.setText(Html.fromHtml(response.body().getProducts().get(0).getReviews()));
                        Picasso.with(getContext()).load(response.body().getProducts().get(0).getThumbnail()).into(img_product);
                        txt_quantity.setText(Quantity);
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

    @Override
    public void respond(int position, Integer Price) {

    }
}
