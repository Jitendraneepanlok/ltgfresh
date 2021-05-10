package com.ltg.ltgfresh.Helper;

import com.ltg.ltgfresh.Pojo.Category;
import com.ltg.ltgfresh.Pojo.ShopResponse;
import com.ltg.ltgfresh.Pojo.SubCategory;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Genre extends ExpandableGroup<SubCategory> {

    private int iconResId;

    public Genre(String title, List<SubCategory> items, int iconResId) {
        super(title, items);
        this.iconResId = iconResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre)) return false;

        Genre genre = (Genre) o;

        return getIconResId() == genre.getIconResId();

    }

    @Override
    public int hashCode() {
        return getIconResId();
    }
}
