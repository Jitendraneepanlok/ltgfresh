package com.ltg.ltgfresh.Pojo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleProductResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("products")
    @Expose
    private List<SingleProductData> products = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<SingleProductData> getProducts() {
        return products;
    }

    public void setProducts(List<SingleProductData> products) {
        this.products = products;
    }

}
