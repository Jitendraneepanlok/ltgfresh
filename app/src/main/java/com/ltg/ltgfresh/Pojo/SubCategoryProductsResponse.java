package com.ltg.ltgfresh.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategoryProductsResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("products")
    @Expose
    private List<SubCategoryProductsResponseProduct> products = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<SubCategoryProductsResponseProduct> getProducts() {
        return products;
    }

    public void setProducts(List<SubCategoryProductsResponseProduct> products) {
        this.products = products;
    }
}
