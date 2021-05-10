package com.ltg.ltgfresh.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CartListResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("Cartlist")
    @Expose
    private List<CartListData> cartlist = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<CartListData> getCartlist() {
        return cartlist;
    }

    public void setCartlist(List<CartListData> cartlist) {
        this.cartlist = cartlist;
    }
}
