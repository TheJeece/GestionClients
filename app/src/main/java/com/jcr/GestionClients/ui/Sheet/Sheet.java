package com.jcr.GestionClients.ui.Sheet;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Sheet {

    int ID;
    Date date;
    String name, sponsor;
    long price;
    Boolean isPaid,isSelected;
    List<SheetDetail> sheetDetails;

    public Sheet(int ID, Date date, String name, String sponsor, List<SheetDetail> sheetDetails, long price, boolean isPaid, boolean isSelected) {
        this.ID = ID;
        this.date = date;
        this.name = name;
        this.sponsor = sponsor;
        this.sheetDetails = sheetDetails;
        this.price  =price;
        this.isPaid = isPaid;

        this.isSelected=isSelected;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public String getPresta() {
//        return presta;
//    }
//
//    public void setPresta(String presta) {
//        this.presta = presta;
//    }

    public List<SheetDetail> getSheetDetails() {
        return sheetDetails;
    }
    public void setSheetDetails(List<SheetDetail> sheetDetails){
        this.sheetDetails=sheetDetails;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public boolean isPaid() {
        return isPaid;}

    public void setPaid(boolean Paid) {
        isPaid=Paid;}

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /*
     * Comparator pour le tri des détails par date
     */
    public static Comparator<Sheet> CompDate = new Comparator<Sheet>() {
        @Override
        public int compare(Sheet Sheet1, Sheet Sheet2) {
            return Sheet2.getDate().compareTo(Sheet1.getDate());
        }
    };
}
