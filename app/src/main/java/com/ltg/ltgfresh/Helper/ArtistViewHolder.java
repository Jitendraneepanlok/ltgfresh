package com.ltg.ltgfresh.Helper;

import android.view.View;
import android.widget.TextView;

import com.ltg.ltgfresh.Pojo.Category;
import com.ltg.ltgfresh.Pojo.ShopResponse;
import com.ltg.ltgfresh.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ArtistViewHolder extends ChildViewHolder {

    private TextView artistName;

    public ArtistViewHolder(View itemView) {
        super(itemView);
        artistName = itemView.findViewById(R.id.artist_name);
    }

    public void setArtistName(String name) {
        artistName.setText(name);
    }
}

