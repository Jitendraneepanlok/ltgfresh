package com.ltg.ltgfresh.Pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartItemRemoveResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Cartlist")
    @Expose
    private List<CartItemRemoveData> cartlist = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CartItemRemoveData> getCartlist() {
        return cartlist;
    }

    public void setCartlist(List<CartItemRemoveData> cartlist) {
        this.cartlist = cartlist;
    }
}
