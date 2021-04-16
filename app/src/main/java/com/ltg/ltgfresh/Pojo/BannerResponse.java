package com.ltg.ltgfresh.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("banner")
    @Expose
    private List<BannerData> banner = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<BannerData> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerData> banner) {
        this.banner = banner;
    }
}
