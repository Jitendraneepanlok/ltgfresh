package com.ltg.ltgfresh.Pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("category")
    @Expose
    private List<Category> category = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

}

