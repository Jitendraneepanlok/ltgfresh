package com.ltg.ltgfresh.Pojo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OffersResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("offer")
    @Expose
    private List<OfferData> offer = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<OfferData> getOffer() {
        return offer;
    }

    public void setOffer(List<OfferData> offer) {
        this.offer = offer;
    }
}