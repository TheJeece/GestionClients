package com.jcr.GestionClients;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.jcr.GestionClients.ui.Prestations.CategoriesAdapter;
import com.jcr.GestionClients.ui.Prestations.Category;
import com.jcr.GestionClients.ui.Prestations.Prestation;
import com.jcr.GestionClients.ui.Sheet.Sheet;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME = "dbGestionClients";

    private final String TAG = "DataBaseHandler";
    //Tables des contacts

    public static final String
            tableContacts = "Contacts",
            tableCategories = "Categories",
            tablePrestations = "Prestations",
            tableSheet = "Fiches",
            tableDetailSheet = "Fiches_detail";

    private static final String
            TABLE_CONTACTS = tableContacts,
            COLUMN_ID = "id", //0
            COLUMN_NAME = "Nom", //1
            COLUMN_PHONE = "Téléphone",//2
            COLUMN_MAIL = "Mail",//3
            COLUMN_ADDRESS = "Adresse",//4
            COLUMN_BDAY = "Anniversaire";//5

    public static final int
            columnContactName = 1,
            columnContactPhone = 2,
            columnContactMail = 3,
            columnContactAddress = 4,
            columnContactBday = 5;

    //Table des prestations
    private static final String
            TABLE_CAT = tableCategories,
            COLUMN_CAT = "Catégorie", //1
            COLUMN_CAT_IMAGE = "Image";

    public static final int
            columnCategory = 1,
            columnImage = 2;

    //Table des prix
    private static final String
            TABLE_PRESTA = tablePrestations,
            COLUMN_PRESTA_CAT_ID = "idCategorie", //1
            COLUMN_PRESTA = "Prestation", //2
            COLUMN_PRESTA_PRICE = "Prix"; //3

    public static final int
            columnPrestaCategory = 1,
            columnPrestation = 2,
            columnPrestaPrice = 3;

    //Table des fiches
    private static final String
            TABLE_SHEET = tableSheet,
            COLUMN_SHEET_DATE = "Date",                 //1
            COLUMN_SHEET_CLIENT_ID = "idClient",        //2
            COLUMN_SHEET_SPONSOR_ID = "Parrain",        //3
            COLUMN_SHEET_PRICE_DISCOUNT = "Prix_remise",//4
            COLUMN_SHEET_PAID = "Acquittee";            //5

    public static final int
            columnSheetDate = 1,
            columnSheetClientID = 2,
            columnSheetSponsorID = 3,
            columnSheetPrice = 4,
            columnSheetIsPaid = 5;

    //tables des fiches détail
    private static final String
            TABLE_SHEET_DETAIL = tableDetailSheet,
            COLUMN_DETAIL_SHEET_ID = "idFiche",            //1
            COLUMN_DETAIL_PRESTA_ID = "idPresta";          //2

    public static final int
            columnDetailSheetID = 1,
            columnDetailPrestaID = 2;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
    CREATION DES TABLES DE LA BDD
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        TABLE CONTACTS
         */
        String CREATE_TABLE_CONTACTS = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME + " TEXT UNIQUE," +
                COLUMN_PHONE + " TEXT," +
                COLUMN_MAIL + " TEXT," +
                COLUMN_ADDRESS + " TEXT," +
                COLUMN_BDAY + " DATE" +
                ")";
        db.execSQL(CREATE_TABLE_CONTACTS);

        /*
        TABLE CATEGORIES
         */
        String CREATE_TABLE_CATEGORIES = "CREATE TABLE IF NOT EXISTS " + TABLE_CAT +
                " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_CAT + " TEXT," +
                COLUMN_CAT_IMAGE + " BLOB" +
                ")";
        db.execSQL(CREATE_TABLE_CATEGORIES);


        /*
        TABLE PRESTATIONS
         */
        String CREATE_TABLE_PRESTATIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_PRESTA +
                " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY," + //0
                COLUMN_PRESTA_CAT_ID + " INTEGER," + //1
                COLUMN_PRESTA + " TEXT," + //2
                COLUMN_PRESTA_PRICE + " NUMERIC" + //3
                ")";
        db.execSQL(CREATE_TABLE_PRESTATIONS);

        /*
        TABLE FICHES
         */
        String CREATE_TABLE_SHEETS = "CREATE TABLE IF NOT EXISTS " + TABLE_SHEET +
                " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +       //0
                COLUMN_SHEET_DATE + " TEXT," +              //1
                COLUMN_SHEET_CLIENT_ID + " INTEGER," +      //2
                COLUMN_SHEET_SPONSOR_ID + " INTEGER," +     //3
                COLUMN_SHEET_PRICE_DISCOUNT + " NUMERIC," + //4
                COLUMN_SHEET_PAID + " INTEGER" +            //5
                ")";
        db.execSQL(CREATE_TABLE_SHEETS);

        /*
        TABLE DETAILS
         */
        String CREATE_TABLE_SHEET_DETAIL = "CREATE TABLE IF NOT EXISTS " + TABLE_SHEET_DETAIL +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_DETAIL_SHEET_ID + " INTEGER," +
                COLUMN_DETAIL_PRESTA_ID + " INTEGER" +
        ")";
        db.execSQL(CREATE_TABLE_SHEET_DETAIL);

    }

    /*
    Mise à jour de la db en cas de modification de la valeur DATABASE_VERSION
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Suppression de la base de donnée existante
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESTA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHEET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHEET_DETAIL);
        // Création de la base de donnée avec la nouvelle version
        onCreate(db);
    }

    /*
    AJOUT DE NOUVELLES VALEURS DANS LA BDD CLIENTS
     */
    public void insertClientData(String name, String phone, String mail, String address, String bday){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);//column name, column value
        values.put(COLUMN_PHONE,phone);
        values.put(COLUMN_MAIL, mail);
        values.put(COLUMN_ADDRESS,address);
        values.put(COLUMN_BDAY,bday);

        // Inserting Row
        db.insert(TABLE_CONTACTS,null,values);
        db.close();
        // Closing database connection
    }

    /*
    AJOUT DE NOUVELLES VALEURS DANS LA BDD CATEGORY
     */
    public void insertCatData(String category, Bitmap image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        byte[] byteImg = null;

        if (image != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 0, stream);

            byteImg =stream.toByteArray();
        }
        values.put(COLUMN_CAT, category);
        values.put(COLUMN_CAT_IMAGE,byteImg);

        // Inserting Row
        db.insert(TABLE_CAT,null,values);
        db.close();
        // Closing database connection
    }
    /*
    AJOUT DE LA REFERENCE A L'IMAGE  CATEGORY
     */
    public void setCatData(int catID, String category, Bitmap image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        byte[] byteImg = null;

        if (image != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 0, stream);

            byteImg =stream.toByteArray();
        }
        values.put(COLUMN_ID,catID);
        values.put(COLUMN_CAT, category);
        values.put(COLUMN_CAT_IMAGE,byteImg);
        Log.i(TAG, "setCatData: image set " + byteImg );
        Log.i(TAG, "insertPrestaData: " + values);

        // Inserting Row
        db.update(TABLE_CAT,values,COLUMN_ID + " = " + catID,null);
        db.close();
        // Closing database connection
    }

    /*
    AJOUT DE NOUVELLES VALEURS DANS LA BDD PRESTATIONS
     */
    public void insertPrestaData(int catID, String prestation, long price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRESTA_CAT_ID, catID);
        values.put(COLUMN_PRESTA,prestation);
        values.put(COLUMN_PRESTA_PRICE, price);


        // Inserting Row
        db.insert(TABLE_PRESTA,null,values);
        db.close();
        // Closing database connection
    }

    /*
    SUPPRESSION DE  VALEURS AVEC LE NOM DE LA TABLE ET LA CLE
     */
    public void deleteData(String TABLENAME, int key) {
        String name = getDataFromKey(TABLENAME,key,1);
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLENAME,COLUMN_ID + " = " + key,null);
        db.close();
    }

    /*
    MISE A JOUR D'UNE VALEUR DE PRESTATION AVEC LA CLE CORRESPONDANTE
     */
    public void setPrestaData(int prestaKey, int catKey, String presta, long prix) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRESTA_CAT_ID,catKey);
        contentValues.put(COLUMN_PRESTA,presta);
        contentValues.put(COLUMN_PRESTA_PRICE,prix);

        db.update(TABLE_PRESTA,contentValues,COLUMN_ID + " = " + prestaKey,null);
        db.close();
    }

    /*
    MISE A JOUR D'UN CLIENT AVEC LA CLE CORRESPONDANTE
    //TODO supprimer l'appel du nom de la table. Ici Clients
     */
    public void setClientData(String TABLENAME, int columnIndex, int key, String strEdit){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        String COLUMN = "";
        if (columnIndex ==1) {
            COLUMN = COLUMN_NAME;
        } else if(columnIndex == 2) {
            COLUMN = COLUMN_PHONE;
        } else if(columnIndex == 3) {
            COLUMN = COLUMN_MAIL;
        } else if(columnIndex == 4) {
            COLUMN = COLUMN_ADDRESS;
        } else if(columnIndex == 5) {
            COLUMN = COLUMN_BDAY;
        }
        contentValues.put(COLUMN,strEdit);
        db.update(TABLENAME,contentValues,COLUMN_ID + " = " + key,null);
        db.close();
    }

    /*
    MISE A JOUR D'UNE FICHE AVEC LA CLE CORRESPONDANTE
    */
    public void setSheet(int sheetKey, Sheet sheet){
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");

        int clientID=getKey(TABLE_CONTACTS,sheet.getName());
        int sponsorID= getKey(TABLE_CONTACTS,sheet.getSponsor());

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SHEET_DATE,simpleDateFormat.format(sheet.getDate()));
        contentValues.put(COLUMN_SHEET_CLIENT_ID,clientID);
        contentValues.put(COLUMN_SHEET_SPONSOR_ID,sponsorID);
        contentValues.put(COLUMN_SHEET_PRICE_DISCOUNT,sheet.getPrice());
        contentValues.put(COLUMN_SHEET_PAID,sheet.isPaid());

        SQLiteDatabase db = this.getReadableDatabase();
        db.update(TABLE_SHEET,contentValues,COLUMN_ID + " = " + sheetKey,null);
        db.close();
    }

    /*
    Retourne la liste ciblée de la table
     */
    public List getListFromTable(String TABLENAME, int columnIndex){
        SQLiteDatabase db = this.getReadableDatabase();
        List list = new ArrayList();

        String selectQuery = "SELECT * FROM " + TABLENAME;
        Cursor cursor = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(columnIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    /*
    RECUPERE LA CLE DE LA LIGNE AVEC LE TEXTE DE LA PREMIERE COLONNE
     */
    public int getKey(String TABLENAME, String name) {
        int key = -1;
        String selectQuery = "SELECT * FROM " + TABLENAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do { if(cursor.getString(1).equals(name)) {
                key = cursor.getInt(0);
            }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return key;
    }

    /*
    RECUPERE LA DONNE CORRESPONDANTE A LA CLE AVEC LA COLONNE
     */
    public String getDataFromKey(String TABLENAME, int key, int columnIndex) {
        String ans ="";

        String selectQuery = "SELECT * FROM " + TABLENAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do { if(cursor.getInt(0)==key) {
                ans = cursor.getString(columnIndex);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return ans;
    }

    /*
    RECUPERE LA CLE DE LA PRESTATION CORRESPONDANTE A LA CATEGORIE
    (en cas de doublon dans 2 categories)
     */
    public int getPrestaKeyFromCatID(String TABLENAME, int MainID, String search) {

        int key = -1;

        String selectQuery = "SELECT * FROM " + TABLENAME +
                " WHERE " + COLUMN_PRESTA_CAT_ID + " = " + MainID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                if(cursor.getString(2).equals(search)) {
                key = cursor.getInt(0);
            }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return key;
    }

    /*
    RECUPERE LA LISTE DES PRESTATIONS CORRESPONDANTE A LA CATEGORIES
     */
    public List getPrestasFromCatID(int catID) {

        String selectQuery = "SELECT * FROM " + TABLE_PRESTA +
                " WHERE " + COLUMN_PRESTA_CAT_ID + " = " + catID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        List prestas = new ArrayList();

        if (cursor.moveToFirst()) {
            do {
                prestas.add(cursor.getString(2));
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return prestas;
    }
    /*
    RECUPERE LA LISTE DES CATEGORYES
     */
    public List<Category> getCategories() {
        String selectQuery = "SELECT * FROM " + TABLE_CAT ;
        SQLiteDatabase db = this.getReadableDatabase ();

        Cursor cursor = db.rawQuery(selectQuery,null);
        List<Category> categories = new ArrayList<>();
        if (cursor.moveToFirst() && cursor!=null) {
            do {
                int catID = cursor.getInt(0);
                String name = cursor.getString(columnCategory);
                byte[] byteImg = cursor.getBlob(columnImage);
                Bitmap image = null;
                if (byteImg != null) {
                    image = BitmapFactory.decodeByteArray(byteImg, 0, byteImg.length);
                    Log.i(TAG, "getCategory: image " + (byteImg == null));
                }

                Category category = new Category(catID, name, image, false);
                categories.add(category);
            }while(cursor.moveToNext());
        }
        Log.i(TAG, "getCategories: List set");
        cursor.close();
        db.close();
        return categories;
    }
    public Category getCategory(int catID) {
        String selectQuery = "SELECT * FROM " + TABLE_CAT +
                " WHERE " + COLUMN_ID + " = " + catID;
        SQLiteDatabase db = this.getReadableDatabase ();

        Cursor cursor = db.rawQuery(selectQuery,null);
        Category category=null;
        if (cursor.moveToFirst() && cursor!=null) {

            String name = cursor.getString(columnCategory);
            byte[] byteImg = cursor.getBlob(columnImage);
            Log.i(TAG, "getCategory: image "+ (byteImg == null));
            Bitmap image = null;
            if (byteImg != null) {
                image = BitmapFactory.decodeByteArray(byteImg, 0, byteImg.length);
                Log.i(TAG, "getCategory: image " + (byteImg == null));
            }
            category = new Category(catID, name, image, false);


        }
        Log.i(TAG, "getCategory: cat set");
        cursor.close();
        db.close();
        return category;
    }

    /*
    RECUPERE LA LISTE DES PRESTATIONS CORRESPONDANT A LA CATEGORIE SELECTIONEE
     */
    public List<Prestation> getPrestations(int catID) {

        SQLiteDatabase db = this.getReadableDatabase ();
        String selectQuery = "SELECT * FROM " + TABLE_PRESTA +
                " WHERE " + COLUMN_PRESTA_CAT_ID + " = " + catID;
        Cursor cursor = db.rawQuery(selectQuery,null);

        List<Prestation> prestations = new ArrayList<>();

        if (cursor.moveToFirst() && cursor!=null) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(columnPrestation);

                Long price = cursor.getLong(columnPrestaPrice);
                String selectQueryCat = "SELECT * FROM " + TABLE_CAT +
                        " WHERE " + COLUMN_ID + " = " + catID;
                Cursor cCategory = db.rawQuery(selectQueryCat,null);
                String category ="";
                if (cCategory != null && cCategory.moveToFirst()) {
                    category = cCategory.getString(columnCategory);
                }
                cCategory.close();
                Prestation prestation = new Prestation(id,category,name,price,false);
                prestations.add(prestation);

            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
    return prestations;
    }

    /*
    RECUPERE LA LISTE DES IDENTIFIANTS DE PRESTATIONS CONTENUS DANS UNE FICHE
     */
    public List<Integer> getPrestaIDsFromSheetID(int sheetID) {

        List<Integer> details = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SHEET_DETAIL +
                " WHERE " + COLUMN_DETAIL_SHEET_ID + " = " + sheetID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            do{

                details.add(cursor.getInt(columnDetailPrestaID));
            } while (cursor.moveToNext());
        }
    return details;
    }

    /*
    RECUPERE LA LISTE DES IDENTIFIANTS DE CHAQUE PRESTATION DONNEE EN ARGUMENT
     */
    public List getPrestaKeys(int catID,List listPresta) {

        String selectQuery = "SELECT * FROM " + TABLE_PRESTA +
                " WHERE " + COLUMN_PRESTA_CAT_ID + " = " + catID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        List keys = new ArrayList();

        if (cursor != null ) {
            for (int i=0;i<listPresta.size();i++) {
                cursor.moveToFirst();
                do {
                    String cPresta = cursor.getString(2);
                    if (listPresta.get(i).toString().equals(cPresta)) {
                        keys.add(cursor.getString(0));
                    }
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return keys;
    }

    /*
    RECUPERE LA LISTE DES PRIX CORRESPONDANT AUX PRESTATIONS EN ARGUEMENT
     */
    public List getPrices(List prestaIDs) {

        String selectQuery = "SELECT * FROM " + TABLE_PRESTA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        List prices = new ArrayList();

        if (cursor!=null) {
            for (int i=0;i<prestaIDs.size();i++) {
                cursor.moveToFirst();
                do {
                    if (Integer.parseInt(prestaIDs.get(i).toString()) == cursor.getInt(0)){
                        prices.add(cursor.getString(3));
                    }

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return prices;
    }

    /*
    CREE UNE NOUVELLE FICHE ET RENVOIE SON IDENTIFIANT
     */
    public int insertSheet(int ClientID, int SponsorID, String Date, long DiscountPrice, Boolean Paid) {

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SHEET_CLIENT_ID,ClientID);
        contentValues.put(COLUMN_SHEET_SPONSOR_ID,SponsorID);
        contentValues.put(COLUMN_SHEET_DATE, String.valueOf(Date));
        contentValues.put(COLUMN_SHEET_PRICE_DISCOUNT,DiscountPrice);
        contentValues.put(COLUMN_SHEET_PAID,Paid);

        db.insert(TABLE_SHEET,null,contentValues);

        //Récupération de la dernière clé créeepour la renvoyer
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SHEET,null);
        cursor.moveToLast();
        int sheetKey=-1;
        if (cursor!=null) {
            sheetKey=cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return sheetKey;
    }

    /*
    CREE UNE NOUVELLE PRESTATION A UNE FICHE DETAIL
     */
    public void insertSheetDetail(int sheetKey, int prestaKey) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DETAIL_SHEET_ID,sheetKey);
        contentValues.put(COLUMN_DETAIL_PRESTA_ID,prestaKey);
        SQLiteDatabase db = this.getReadableDatabase();
        db.insert(TABLE_SHEET_DETAIL,null,contentValues);
    }

    /*
    RECUPERE LA LISTE DES DETAILS D'UNE FICHE
     */
    public List<Integer> getSheetPrestas(int sheetKey) {
        List<Integer> prestaKeys = new ArrayList();
        String selectQuery = "SELECT * FROM " + TABLE_SHEET_DETAIL +
                " WHERE " + COLUMN_DETAIL_SHEET_ID + " = " + sheetKey ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor!=null) {
            cursor.moveToFirst();
            do {
                prestaKeys.add(cursor.getInt(2));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return prestaKeys;

    }

    /*
    VERIFIE QU'UNE FICHE A DES PRESTATIONS
     */
    public boolean sheetHasDetails(int sheetKey) {
        String selectQuery = "SELECT * FROM " + TABLE_SHEET_DETAIL +
                " WHERE " + COLUMN_DETAIL_SHEET_ID + " = " + sheetKey ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        cursor.close();
        db.close();
        return cursor!=null;
    }

    /*
    SUPPRESSION DES DETAILS D'UNE FICHE
     */
    public void delDetails(int sheetKey) {
        String selectQuery = "SELECT * FROM " + TABLE_SHEET_DETAIL +
                " WHERE " + COLUMN_DETAIL_SHEET_ID + " = " + sheetKey ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor !=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                deleteData(TABLE_SHEET_DETAIL, cursor.getInt(0));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

    }
}