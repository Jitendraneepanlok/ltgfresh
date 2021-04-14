package com.ltg.ltgfresh.Pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private UserProfileData data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public UserProfileData getData() {
        return data;
    }

    public void setData(UserProfileData data) {
        this.data = data;
    }
}
