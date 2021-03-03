package com.jcr.GestionClients.ui.Sheet;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.jcr.GestionClients.DatabaseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SheetModel extends ViewModel {
    private static final String TAG = "PrestaSheetModel";
    DatabaseHandler dbh;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /*
    CREE UNE FICHE ET RENVOIE LA CLE CREE
     */
    public int CreateSheet(Context context, int ClientID, int SponsorID, Date date, long DiscountPrice, boolean Paid, String note){
        dbh = new DatabaseHandler(context);
        String sDate = simpleDateFormat.format(date);
        return dbh.insertSheet(ClientID,SponsorID,sDate,DiscountPrice,Paid,note);
    }

    /*
    MISE A JOUR DES DONNES DE LA FICHE
     */
    public void editSheet(Context context, int sheetKey, Sheet sheet) {
        dbh= new DatabaseHandler(context);
        dbh.setSheet(sheetKey,sheet);
    }

    /*
    RECUPERE LA LISTE DES PRESTATIONS DE LA FICHE
     */
    public List<Integer> getSheetPrestas(Context context, int sheetKey) {
        dbh = new DatabaseHandler(context);
        return dbh.getSheetPrestas(sheetKey);
    }

    public void delPresta(Context context, int detailID) {

    }

    public void addSheet(Context context, Sheet sheet) {
        dbh = new DatabaseHandler(context);
     }

     /*
     AJOUTE UN DETAIL AVEC LA CLE DE LA FICHE
     */
    public void insertDetail(Context context, int sheetKey, int prestaID) {
        dbh = new DatabaseHandler(context);
        dbh.insertSheetDetail(sheetKey,prestaID);
    }

    /*
    SUPPRESSION DE TOUTES LES PRESTATIONS DE LA FICHE
     */
    public void delDetails(Context context, int sheetKey) {
        dbh = new DatabaseHandler(context);
        if (dbh.sheetHasDetails(sheetKey)) {
            dbh.delDetails(sheetKey);
        }
    }

    public List getPrestas(Context context) {
        dbh = new DatabaseHandler(context);
        return dbh.getListFromTable(DatabaseHandler.tableSheet,0);
    }

    /*
    RETOURNE LA LISTE DES OBJETS PRESTATIONS LIES A UNE FIHCE
     */
    public List<SheetDetail> getDetails(Context context, int sheetID) {
        dbh = new DatabaseHandler(context);
        List<SheetDetail> details = new ArrayList<SheetDetail>();
        List<Integer> prestaIDs = dbh.getPrestaIDsFromSheetID(sheetID);
        for (int i=0; i<prestaIDs.size();i++) {
            int id = prestaIDs.get(i);
            String presta = dbh.getDataFromKey(DatabaseHandler.tablePrestations,id,DatabaseHandler.columnPrestation);
            long    price;
            int     catID;
            String  cat;
            if (!presta.equals("")) {
                price = Long.parseLong(dbh.getDataFromKey(DatabaseHandler.tablePrestations, id, DatabaseHandler.columnPrestaPrice));
                catID = Integer.parseInt(dbh.getDataFromKey(DatabaseHandler.tablePrestations, id, DatabaseHandler.columnPrestaCategory));
                cat = dbh.getDataFromKey(DatabaseHandler.tableCategories, catID, DatabaseHandler.columnCategory);
            } else {
                price = 0;
                catID = -1;
                cat = "Catégorie non repertoriée";
                presta = "Prestation non repertoriée";
            }
            SheetDetail sheetDetail = new SheetDetail(cat,presta,price,false);
            details.add(sheetDetail);
        }

        return details;
    }

    /*
    RETOURNE LA LISTE DES OBJETS FICHE
     */
    public List<Sheet> getSheets(Context context) {
        dbh = new DatabaseHandler(context);
        List idList = dbh.getListFromTable(DatabaseHandler.tableSheet,0);
        List<Sheet> sheetList = new ArrayList<>();
        Log.i(TAG, "getSheets: " + idList.size());
        if (idList.size()>0) {
            int id;

            for (int i = 0; i < idList.size(); i++) {
                id = Integer.parseInt(idList.get(i).toString());
                Sheet sheet = getSheet(context,id);
                sheetList.add(sheet);
            }
        }
    return sheetList;
    }

    /*
    RETOURNE L'OBJET FICHE LIE A L'ID
     */
    public Sheet getSheet(Context context, int id) {
        dbh = new DatabaseHandler(context);

        Date date = null;
        try {
            date = simpleDateFormat.parse(dbh.getDataFromKey(DatabaseHandler.tableSheet, id, DatabaseHandler.columnSheetDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int clientID = Integer.parseInt(dbh.getDataFromKey(DatabaseHandler.tableSheet, id, DatabaseHandler.columnSheetClientID));
        String client = dbh.getDataFromKey(DatabaseHandler.tableContacts, clientID, DatabaseHandler.columnContactName);

        int sponsorID = Integer.parseInt(dbh.getDataFromKey(DatabaseHandler.tableSheet, id, DatabaseHandler.columnSheetSponsorID));
        String sponsor = "";
        if (sponsorID != -1) {
            sponsor = dbh.getDataFromKey(DatabaseHandler.tableContacts, sponsorID, DatabaseHandler.columnContactName);
        }
//        String category = "";
//        String prestation = "";
        List<SheetDetail> sheetDetails = getDetails(context,id);

        long price = Long.parseLong(dbh.getDataFromKey(DatabaseHandler.tableSheet, id, DatabaseHandler.columnSheetPrice));
        Boolean isPaid = 1==Integer.parseInt(dbh.getDataFromKey(DatabaseHandler.tableSheet, id, DatabaseHandler.columnSheetIsPaid));
        String note = dbh.getDataFromKey(DatabaseHandler.tableSheet,id,DatabaseHandler.columnSheetNotes);

        Boolean isSelected = false;

        Sheet sheet = new Sheet(id, date, client, sponsor, sheetDetails, price, isPaid, note, isSelected);
        return sheet;
    }

    public void delete(Context context, int sheetKey) {
        dbh = new DatabaseHandler(context);
        dbh.deleteData(DatabaseHandler.tableSheet,sheetKey);
    }

    /*
     * Retourne la liste des fiches client
     */
    public List<Sheet> getSheetsOfClient(Context context, String name) {

        dbh = new DatabaseHandler(context);
        List idList = dbh.getListFromTable(DatabaseHandler.tableSheet,0);
        List<Sheet> sheetList = new ArrayList<>();

        if (idList.size()>0) {
            int id;

            for (int i = 0; i < idList.size(); i++) {
                id = Integer.parseInt(idList.get(i).toString());
                Sheet sheet = getSheet(context,id);
                if (sheet.getName().equals(name)) {
                    sheetList.add(sheet);
                }
            }
        }
        return sheetList;
    }

    public List<Sheet> getSponsoredOfClient (Context context, String name) {
        dbh =new DatabaseHandler(context);

        List idList = dbh.getListFromTable(DatabaseHandler.tableSheet,0);
        List<Sheet> sheetList = new ArrayList<>();

        if (idList.size()>0) {
            int id;

            for (int i = 0; i < idList.size(); i++) {
                id = Integer.parseInt(idList.get(i).toString());
                Sheet sheet = getSheet(context,id);
                if (sheet.getSponsor().equals(name)) {
                    sheetList.add(sheet);
                }
            }
        }
        return sheetList;

    }
}
