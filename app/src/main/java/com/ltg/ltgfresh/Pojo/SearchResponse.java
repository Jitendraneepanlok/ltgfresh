package com.ltg.ltgfresh.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("products")
    @Expose
    private List<SearchProduct> products = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<SearchProduct> getProducts() {
        return products;
    }

    public void setProducts(List<SearchProduct> products) {
        this.products = products;
    }
}
