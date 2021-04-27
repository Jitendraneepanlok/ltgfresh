package com.ltg.ltgfresh.NavigationDrawerActvity.ui.organicproduct;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrganicProductModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OrganicProductModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Organic fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
