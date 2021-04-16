package com.ltg.ltgfresh.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ltg.ltgfresh.R;

public class SubCategoryFragment extends Fragment {
    private NavController navController;
    View view;
    RecyclerView recycer_sub_category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        view = inflater.inflate(R.layout.fragment_sub_category, container, false);
        intView();
        return view;
    }

    private void intView() {

        recycer_sub_category = (RecyclerView) view.findViewById(R.id.recycer_sub_category);
    }
}
