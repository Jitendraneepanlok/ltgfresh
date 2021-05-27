package com.ltg.ltgfresh.Pojo;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishListResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("Wishlist")
    @Expose
    private List<WishListData> wishlist = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<WishListData> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<WishListData> wishlist) {
        this.wishlist = wishlist;
    }
}
