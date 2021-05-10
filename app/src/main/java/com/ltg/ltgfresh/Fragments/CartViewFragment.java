package com.ltg.ltgfresh.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ltg.ltgfresh.Activity.LoginActivity;
import com.ltg.ltgfresh.Adapter.CartListAdapter;
import com.ltg.ltgfresh.Adapter.ShopAdapter;
import com.ltg.ltgfresh.Helper.FragmentCommunication;
import com.ltg.ltgfresh.NavigationDrawerActvity.MainActivity;
import com.ltg.ltgfresh.Network.ApiClient;
import com.ltg.ltgfresh.Network.ApiInterface;
import com.ltg.ltgfresh.Pojo.CartListResponse;
import com.ltg.ltgfresh.Pojo.LogoutResponse;
import com.ltg.ltgfresh.R;
import com.ltg.ltgfresh.SharedPrefrences.SessionManager;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;

public class CartViewFragment extends Fragment implements FragmentCommunication {

    View view;
    private NavController navController;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;
    String User_ID;
    AppCompatTextView tv_text;
    AppCompatImageView img_back, img_cart, img_profile;
    RecyclerView cart_recycler;
    CartListAdapter adapter;
    Toolbar toolbar;
    AppCompatButton btn_addprice,btn_place_order;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart_view, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        sessionManager = new SessionManager(getActivity());
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

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
        initView();
        cartViewDetails();

        return view;
    }

    private void initView() {

        btn_place_order = (AppCompatButton)view.findViewById(R.id.btn_place_order);
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_cartViewFragment_to_paymentFragment);
            }
        });

        btn_addprice = (AppCompatButton) view.findViewById(R.id.btn_addprice);
        tv_text = (AppCompatTextView) view.findViewById(R.id.tv_text);
        tv_text.setText(getResources().getString(R.string.cart_view));

        img_back = (AppCompatImageView) view.findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        cart_recycler = (RecyclerView) view.findViewById(R.id.cart_recycler);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        cart_recycler.setLayoutManager(verticalLayoutManager);
        cart_recycler.setItemAnimator(new DefaultItemAnimator());
    }

    private void cartViewDetails() {
        User_ID = sessionManager.getUserData(SessionManager.ID);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CartListResponse> call = apiService.getcartList(User_ID);
        try {
            call.enqueue(new Callback<CartListResponse>() {
                @Override
                public void onResponse(Call<CartListResponse> call, retrofit2.Response<CartListResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), String.valueOf(response.body().getStatus()), Toast.LENGTH_SHORT).show();
                        adapter = new CartListAdapter(getActivity(), response.body(), CartViewFragment.this);
                        cart_recycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        pDialog.dismiss();

                    } else {
                        pDialog.cancel();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<CartListResponse>() {
                            }.getType();
                            CartListResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                            Log.e("errorResponse", String.valueOf(errorResponse.getStatus()));
                            Toast.makeText(getActivity(), String.valueOf(errorResponse.getStatus()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("Exception", "" + e);

                        }
                    }
                }

                @Override
                public void onFailure(Call<CartListResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                    Log.e("Failermsg", "" + t);
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
    public void respond(int position, Integer totalPrice) {

        Log.e("total_price", "" + totalPrice);
        btn_addprice.setText(getResources().getString(R.string.Rs) + "" + String.valueOf(totalPrice));
    }
}
