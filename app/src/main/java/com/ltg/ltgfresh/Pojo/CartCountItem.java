package com.ltg.ltgfresh.Pojo;

import java.util.ArrayList;
import java.util.List;

public class CartCountItem {

    int Cartcount;
    ArrayList<String> product_Id;

    public ArrayList<String> getProduct_Id() {
        return product_Id;
    }

    public void setProduct_Id(ArrayList<String> product_Id) {
        this.product_Id = product_Id;
    }

    public int getCartcount() {
        return Cartcount;
    }

    public void setCartcount(int cartcount) {
        Cartcount = cartcount;
    }

}
