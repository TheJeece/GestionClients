package com.jcr.GestionClients.ui.Clients;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ClientImportModel extends ViewModel {

    private static final String TAG = "IMPORT";
    static final int IDs= 0,NAMES=1,PHONES=2,MAILS=3,ADDRESSES=4;


    public List<String> nameID,name, namesIDs, names, mails, addresses, phones;
    public List<List<String>> contactList;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /*
    ##########  METHODE DE RECUPERATION DES CONTACTS TELEPHONES  ##########
    contactList.get([IDs,Names,Phones,Mails,Addresses]).get(ContactIndex).get(ObjectNumber))
     */
    public List<List<String>> getContactList(Context context) {

        ProgressDialog nDialog;
        nDialog = new ProgressDialog(context);
        nDialog.setMessage("Loading..");
        nDialog.setTitle("Get Data");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();

        Log.i(TAG, "Récupération des contacts");
        Cursor cContact =context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null,null,
                ContactsContract.Contacts.DISPLAY_NAME+" ASC");
        Log.i(TAG, "taille du curseur cContact : " + cContact.getCount());

        namesIDs = new ArrayList();
        names = new ArrayList();
        phones = new ArrayList();
        mails = new ArrayList();
        addresses = new ArrayList();
        nameID = new ArrayList();
        name = new ArrayList();


        Log.i(TAG, "Récupération des contacts ...");

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

        nDialog.dismiss();
        return contactList;
    }


    public String getNameByID(Context context, int ID) {

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

    public List getPhonesByID(Context context, int ID) {
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
    public List getMailsByID(Context context, int ID) {

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
    public List getAddressesByID(Context context, int ID) {
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

    public List<Date> getbDayById(Context context, int ID) {
        List<Date> bDay = new ArrayList();
        Date date = null;
        int year;
        int month;
        int day;


        Uri uri = ContactsContract.Data.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Event.CONTACT_ID,
                ContactsContract.CommonDataKinds.Event.START_DATE
        };

        String where =
                ContactsContract.Data.MIMETYPE + "= ? AND " +
                ContactsContract.CommonDataKinds.Event.TYPE +
                    "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY + " AND " +
                ContactsContract.CommonDataKinds.Event.CONTACT_ID + " = " + ID;

        String[] selectionArgs = new String[] {
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE };

        Cursor cBday =context.getContentResolver().query(uri,projection,where,selectionArgs,null);



        cBday.moveToFirst();

        if (cBday.getCount()>0) {
            do {
                String [] dateParts = cBday.getString(cBday.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA1)).split("-");
                year = Integer.parseInt(dateParts[0]);
                month = Integer.parseInt(dateParts[1]);
                day = Integer.parseInt(dateParts[2]);


                try {
                    date = simpleDateFormat.parse(day + "/"+ month+"/"+year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                bDay.add(date);
            } while (cBday.moveToNext());
        }
        cBday.close();
        return bDay;
    }

    /*
    ##########  METHODE DE VERIFICATION D'AUTORISATION D'ACCESS AU REPERTOIRE  ##########
     */
    public boolean HasPermission(Context context, Activity activity) {

        int READ_CONTACTS=1;

        Log.i(TAG, "SET permissioncheck");
        int permissioncheck = ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_CONTACTS);

        Log.i(TAG, "CHECK permissioncheck");
        if (permissioncheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS);
            return false;
        }
    }


}
