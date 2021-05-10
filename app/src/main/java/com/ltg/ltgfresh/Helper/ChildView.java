package com.ltg.ltgfresh.Helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ltg.ltgfresh.Pojo.ShopResponse;
import com.ltg.ltgfresh.R;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

@Layout(R.layout.child_layout)
public class ChildView {
    private static String TAG ="ChildView";
    private NavController navController;

    @View(R.id.child_name)
    TextView textViewChild;

    private Context mContext;
    private String subcatname;

    public ChildView(Context mContext,String subcatname) {
        this.mContext = mContext;
        this.subcatname = subcatname;
    }

    @Resolve
    private void onResolve(){
        textViewChild.setText(subcatname);
        textViewChild.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_fragment);
                navController.navigate(R.id.action_slideshowFragment_to_subCategoryFragment);
            }
        });
    }
}
