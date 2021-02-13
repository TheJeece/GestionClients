package com.jcr.GestionClients.ui.Sheet;

import java.util.Comparator;

public class SheetDetail {
    String category, prestation;
    long price;
    boolean isSelected;

    public SheetDetail(String category, String prestation, long price, boolean isSelected) {
        this.category = category;
        this.prestation = prestation;
        this.price = price;
        this.isSelected = isSelected;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrestation() {
        return prestation;
    }

    public void setPrestation(String prestation) {
        this.prestation = prestation;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    /*
     * Comparator pour le tri des détails par catégorie
     */
    public static Comparator<SheetDetail>CompCategory = new Comparator<SheetDetail>() {
        @Override
        public int compare(SheetDetail Detail1, SheetDetail Detail2) {
            return Detail1.getCategory().compareTo(Detail2.getCategory());
        }
    };
    /*
     * Comparator pour le tri des détails par Prestation
     */
    public static Comparator<SheetDetail>CompPrestation = new Comparator<SheetDetail>() {
        @Override
        public int compare(SheetDetail Detail1, SheetDetail Detail2) {
            return Detail1.getPrestation().compareTo(Detail2.getPrestation());
        }
    };

}
