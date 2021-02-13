package com.jcr.GestionClients.ui.Prestations;

public class Prestation {

    int id;
    String category, name;
    long price;
    boolean isSelected;

    public Prestation(int id, String category, String name, long price, boolean isSelected) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
