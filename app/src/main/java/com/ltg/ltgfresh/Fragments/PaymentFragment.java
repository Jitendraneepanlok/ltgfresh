package com.ltg.ltgfresh.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ltg.ltgfresh.R;

public class PaymentFragment extends Fragment {
    Toolbar toolbar;
    AppCompatImageView img_cart, img_profile;
    private NavController navController;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_payment,container,false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

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
        return view;

    }
}
