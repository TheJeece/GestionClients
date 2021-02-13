package com.jcr.GestionClients.ui.Prestations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jcr.GestionClients.ui.Sheet.Sheet;

import java.util.Comparator;
import java.util.List;

public class Category {

    int id;
    String name;
    Bitmap image;
    boolean isSelected;

    public Category(int id, String name, Bitmap image, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    /*
     * Comparator pour le tri des catégories
     */
    public static Comparator<Category> CompCategories = new Comparator<Category>() {
        @Override
        public int compare(Category category1, Category category2) {
            return category1.getName().toLowerCase().compareTo(category2.getName().toLowerCase());
        }
    };

}
