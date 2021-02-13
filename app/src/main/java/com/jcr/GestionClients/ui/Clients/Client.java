package com.jcr.GestionClients.ui.Clients;

import android.app.Application;
import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Comparator;
import java.util.Date;

public class Client {

    String name;
    String phone;
    String address;
    String email;
    Date bDay;
    boolean isSelected;

    public Client(String name, String phone, String address, String email, Date bDay, boolean isSelected) {

        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.bDay = bDay;
        this.isSelected=isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getbDay() {
        return bDay;
    }

    public void setbDay(Date bDay) {
        this.bDay = bDay;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public boolean getSelected() {
        return isSelected ;
    }

    public int getID(Context context) {
        ClientModel clientModel = new ClientModel();
        return clientModel.getKey(context, this.name);
    }

    public void reset() {
        this.name = "";
        this.phone = "";
        this.address = "";
        this.email = "";
        this.bDay = null;
        this.isSelected = false;
    }

    public static Comparator<Client> CompName = new Comparator<Client>() {
        @Override
        public int compare(Client client1, Client client2) {
            return client1.getName().toLowerCase().compareTo(client2.getName().toLowerCase());
        }
    };

}
