package com.ltg.ltgfresh.NavigationDrawerActvity.ui.offers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OffersModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OffersModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Offers fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
