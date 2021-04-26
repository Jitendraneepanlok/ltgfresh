package com.ltg.ltgfresh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ltg.ltgfresh.Pojo.OfferData;
import com.ltg.ltgfresh.Pojo.OffersResponse;
import com.ltg.ltgfresh.R;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder> {

    private Context mContext;
    private OffersResponse offersResponse;
    LayoutInflater inflter;
    private NavController navController;

    public OffersAdapter(Context applicationContext, OffersResponse offersResponse) {
        this.mContext = applicationContext;
        this.offersResponse = offersResponse;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, offer_percantage,max_price,descrption;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            offer_percantage = (TextView) view.findViewById(R.id.offer_percantage);
            max_price = (TextView)view.findViewById(R.id.max_price);
            descrption = (TextView)view.findViewById(R.id.descrption);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

        }
    }

    @Override
    public int getItemCount() {
        return offersResponse.getOffer().size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public OffersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_card, parent, false);
        return new OffersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OffersAdapter.MyViewHolder holder, int position) {
        OfferData offerData = offersResponse.getOffer().get(position);
        holder.title.setText(offerData.getName());
        holder.offer_percantage.setText(offerData.getOfferValue() + " % off");
//        holder.max_price.setText("\u20B9 "+offerData.getMaxCash());
        holder.descrption.setText(offerData.getDescription());
        Glide.with(mContext)
                .load(offerData.getImage())
                .placeholder(R.drawable.ic_disturb)
                .into(holder.thumbnail);
    }

}
