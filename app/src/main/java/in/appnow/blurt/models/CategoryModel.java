package in.appnow.blurt.models;

/**
 * Created by sonu on 14:19, 18/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class CategoryModel {
    private String categoryName;
    private int categoryIcon;

    public CategoryModel(String categoryName, int categoryIcon) {
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }
}
