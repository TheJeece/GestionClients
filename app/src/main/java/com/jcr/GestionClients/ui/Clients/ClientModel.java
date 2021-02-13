package com.jcr.GestionClients.ui.Clients;


import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.jcr.GestionClients.DatabaseHandler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.lang.String;
import java.text.SimpleDateFormat;

public class ClientModel extends ViewModel {
    private static final String tableName = "Contacts";
    DatabaseHandler dbh;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String TAG = "ClientModel";

    public void Add(Context context, String name, String phone, String mail, String address, String bday) {
        dbh = new DatabaseHandler(context);
        dbh.insertClientData(name, phone, mail, address, bday);
    }
    public int getKey(Context context, String name) {
        dbh = new DatabaseHandler(context);
        return dbh.getKey(tableName,name);
    }

    public String getName(Context context,int key){
        dbh = new DatabaseHandler(context);
        return dbh.getDataFromKey(tableName,key,1);
    }
    public void setName(Context context, int key, String newData) {
        dbh = new DatabaseHandler(context);
        dbh.setClientData(tableName,1,key,newData);
    }
    public List getNames(Context context){
        dbh = new DatabaseHandler(context);
        return dbh.getListFromTable(tableName, 1);
    }

    public String getPhone(Context context,int key){
        dbh = new DatabaseHandler(context);
        return dbh.getDataFromKey(tableName,key,2);
    }
    public void setPhone(Context context, int key, String newData) {
        dbh = new DatabaseHandler(context);
        dbh.setClientData(tableName,2,key,newData);
    }
    public List getPhones(Context context, List names){
        dbh = new DatabaseHandler(context);
        List phones = new ArrayList();
        for(int i=0;i<names.size();i++) {
            String name = names.get(i).toString();
            String phone = getPhone(context,getKey(context,name));
            phones.add(phone);
        }
        return phones;
    }

    public String getMail(Context context,int key) {
        dbh = new DatabaseHandler(context);
        return dbh.getDataFromKey(tableName,key,3);
    }
    public void setMail(Context context, int key, String newData) {
        dbh = new DatabaseHandler(context);
        dbh.setClientData(tableName,3,key,newData);
    }

    public String getAddress(Context context,int key) {
        dbh = new DatabaseHandler(context);
        return dbh.getDataFromKey(tableName,key,4);
    }
    public void setAddress(Context context, int key, String newData) {
        dbh = new DatabaseHandler(context);
        dbh.setClientData(tableName,4,key,newData);
    }

    public Date getBday (Context context,int key) {
        dbh = new DatabaseHandler(context);
        Date bDay=null;
        try {
            bDay = simpleDateFormat.parse(dbh.getDataFromKey(tableName,key,5));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return bDay;
    }
    public void setBday(Context context, int key, String newData) {
        dbh = new DatabaseHandler(context);
        dbh.setClientData(tableName,5,key,newData);
    }

    public boolean clientExists(Context context, String name) {
        List names = getNames(context);
        for (int i=0;i<names.size();i++) {
            if (name.equalsIgnoreCase(names.get(i).toString())) {
                return true;
            }
        }
        return false;
    }
    public void delete(Context context, int key) {
        dbh = new DatabaseHandler(context);
        dbh.deleteData(tableName,key);
    }

    public Client getClient(Context context, int ID) {

        dbh = new DatabaseHandler(context);
        String name = dbh.getDataFromKey(DatabaseHandler.tableContacts,ID,DatabaseHandler.columnContactName);
        String phone = dbh.getDataFromKey(DatabaseHandler.tableContacts,ID,DatabaseHandler.columnContactPhone);
        String mail = dbh.getDataFromKey(DatabaseHandler.tableContacts,ID,DatabaseHandler.columnContactMail);
        String address = dbh.getDataFromKey(DatabaseHandler.tableContacts,ID,DatabaseHandler.columnContactAddress);
        Date bDay = null;
        try {
            bDay = simpleDateFormat.parse(dbh.getDataFromKey(DatabaseHandler.tableContacts,ID,DatabaseHandler.columnContactBday));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Client(name,phone,address,mail,bDay,false);
    }

    public List<Client> getClients (Context context) {
        dbh = new DatabaseHandler(context);
        List<Client> clients = new ArrayList<Client>();
        List idList = dbh.getListFromTable(DatabaseHandler.tableContacts,0);
        Log.i(TAG, "getClients: "+idList);
        int ID = 0;
        for (int i =0; i<idList.size();i++) {
            ID = Integer.parseInt(idList.get(i).toString());
            Log.i(TAG, "getClients: ID = "+ID);

            clients.add(getClient(context,ID));
        }
        return clients;
    }

    public List<List<String>> importContactList(Context context) {

        Log.i(TAG, "Récupération des contacts");
        Cursor cContact =context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null,null,
                ContactsContract.Contacts.DISPLAY_NAME+" ASC");
        Log.i(TAG, "taille du curseur cContact : " + cContact.getCount());

        List namesIDs= new ArrayList();
        List names = new ArrayList();
        List nameID= new ArrayList();
        List name = new ArrayList();

        if (cContact.getCount()>0) {
            cContact.moveToFirst();
            do {
                //NAMES
                int namesIDsSize = namesIDs.size();
                nameID.add(cContact.getString(cContact.getColumnIndex(ContactsContract.Contacts._ID)));
                name.add(cContact.getString(cContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
//                Log.i(TAG, "ID : " + nameID.get(nameID.size() - 1) + " - Nom : " + name.get(name.size() - 1));

                if (namesIDsSize == 0 || !namesIDs.get(namesIDsSize - 1).equals(nameID.get(nameID.size() - 1))) {
                    namesIDs.add(nameID.get(nameID.size() - 1));
                    names.add(name.get(name.size() - 1));
                }

            } while (cContact.moveToNext());
        }
        cContact.close();

        List<List<String>> contactList = new ArrayList<>();
        contactList.add(namesIDs);
        contactList.add(names);

        return contactList;
    }

    public String importNameByID(Context context, int ID) {

        String name = "";
        StringBuffer whereClauseBuf = new StringBuffer();
        whereClauseBuf.append(ContactsContract.Contacts._ID);
        whereClauseBuf.append("=");
        whereClauseBuf.append(ID);

        Cursor cName = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null,
                whereClauseBuf.toString(),null,
                ContactsContract.Contacts.DISPLAY_NAME+" ASC");

        cName.moveToFirst();
        if (cName.getCount()>0) {
            do {
                name = cName.getString(cName.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            } while (cName.moveToNext());
        }
        cName.close();
        return name;
    }

    public List importPhonesByID(Context context, int ID) {
        List phones = new ArrayList();

        StringBuffer whereClauseBuf = new StringBuffer();
        whereClauseBuf.append(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        whereClauseBuf.append("=");
        whereClauseBuf.append(ID);

        Cursor cPhone =context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                whereClauseBuf.toString(),null,
                ContactsContract.Contacts.DISPLAY_NAME+" ASC");

        cPhone.moveToFirst();
        if (cPhone.getCount()>0) {
            do {
                phones.add(cPhone.getString(cPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            } while (cPhone.moveToNext());
        }
        cPhone.close();
        return phones;
    }
    public List importMailsByID(Context context, int ID) {

        List mails = new ArrayList();
        StringBuffer whereClauseBuf = new StringBuffer();
        whereClauseBuf.append(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
        whereClauseBuf.append("=");
        whereClauseBuf.append(ID);

        Cursor cMail =context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                whereClauseBuf.toString(),null,
                ContactsContract.Contacts.DISPLAY_NAME+" ASC");

        cMail.moveToFirst();
        if (cMail.getCount()>0) {
            do {
                mails.add(cMail.getString(cMail.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)));
            } while (cMail.moveToNext());
        }
        cMail.close();
        return mails;
    }
    public List importAddressesByID(Context context, int ID) {
        List addresses = new ArrayList();
        StringBuffer whereClauseBuf = new StringBuffer();
        whereClauseBuf.append(ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID);
        whereClauseBuf.append("=");
        whereClauseBuf.append(ID);

        Cursor cAddresses =context.getContentResolver().query(
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null,
                whereClauseBuf.toString(),null,
                ContactsContract.Contacts.DISPLAY_NAME+" ASC");

        cAddresses.moveToFirst();
        if (cAddresses.getCount()>0) {
            do {
                addresses.add(cAddresses.getString(cAddresses.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA1)));
            } while (cAddresses.moveToNext());
        }
        cAddresses.close();
        return addresses;
    }


}