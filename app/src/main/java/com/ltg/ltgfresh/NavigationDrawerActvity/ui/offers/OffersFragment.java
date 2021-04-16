package com.ltg.ltgfresh.NavigationDrawerActvity.ui.offers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ltg.ltgfresh.R;

public class OffersFragment extends Fragment {

    View view;
    private OffersModel offersModel;
    RecyclerView offers_recycler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // offersModel = new ViewModelProvider(this).get(OffersModel.class);
        view = inflater.inflate(R.layout.fragment_offers,container,false);
        initView();
        getOffers();
        return view;
    }


    private void initView() {

        offers_recycler = (RecyclerView)view.findViewById(R.id.offers_recycler);

    }

    private void getOffers() {


    }

}
