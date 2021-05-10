package com.ltg.ltgfresh.Pojo;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressLint("ParcelCreator")
public class SubCategory implements Parcelable {

//    @SerializedName("id")
//    @Expose
//    private String id;
//    @SerializedName("category_type")
//    @Expose
//    private String categoryType;
//    @SerializedName("category")
//    @Expose
//    private String category;
//    @SerializedName("slug")
//    @Expose
//    private String slug;
    @SerializedName("name")
    @Expose
    private String name;
//    @SerializedName("image")
//    @Expose
//    private String image;
//    @SerializedName("status")
//    @Expose
//    private String status;
//    @SerializedName("created_at")
//    @Expose
//    private String createdAt;
//    @SerializedName("updated_at")
//    @Expose
//    private String updatedAt;
//    @SerializedName("categories_name")
//    @Expose
//    private String categoriesName;

    public SubCategory(Parcel in) {
//     this.categoriesName  = in.readString();
     this.name=in.readString();
    }



//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getCategoryType() {
//        return categoryType;
//    }
//
//    public void setCategoryType(String categoryType) {
//        this.categoryType = categoryType;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public String getSlug() {
//        return slug;
//    }
//
//    public void setSlug(String slug) {
//        this.slug = slug;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(String createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public String getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(String updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//
//    public String getCategoriesName() {
//        return categoriesName;
//    }
//
//    public void setCategoriesName(String categoriesName) {
//        this.categoriesName = categoriesName;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

       // parcel.writeString(categoriesName);
        parcel.writeString(name);
    }
    public static final Creator<SubCategory> CREATOR = new Creator<SubCategory>() {
        @Override
        public SubCategory createFromParcel(Parcel in) {
            return new SubCategory(in);
        }

        @Override
        public SubCategory[] newArray(int size) {
            return new SubCategory[size];
        }
    };
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubCategory)) return false;

        SubCategory artist = (SubCategory) o;

//        if (isFavorite() != artist.isFavorite()) return false;
        return getName() != null ? getName().equals(artist.getName()) : artist.getName() == null;
    }
    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
//        result = 31 * result + (isFavorite() ? 1 : 0);
        return result;
    }
}

