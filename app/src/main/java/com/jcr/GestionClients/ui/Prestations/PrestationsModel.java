package com.jcr.GestionClients.ui.Prestations;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

import com.jcr.GestionClients.DatabaseHandler;

import java.util.List;

public class PrestationsModel extends ViewModel {
    private static final String tableCatName = "Categories";
    private static final String tablePrestaName = "Prestations";

    int prestaKey;
    int catKey;
    String presta;
    long price;
    boolean isSelected;

    DatabaseHandler dbh;

    public Category getCategory(Context context, int catID) {
        dbh = new DatabaseHandler(context);
        return dbh.getCategory(catID);
    }

    public List<Category> getCategories(Context context){
        dbh = new DatabaseHandler(context);
        return dbh.getCategories();
    }
    public List<Prestation> getPrestations(Context context, int catID){
        dbh = new DatabaseHandler(context);
        return dbh.getPrestations(catID);
    }

    public void addCat(Context context, String category, Bitmap image) {
        dbh = new DatabaseHandler(context);
        dbh.insertCatData(category, image);
    }

    public void addPresta(Context context, int catID, String presta, long prix) {
        dbh = new DatabaseHandler(context);
        dbh.insertPrestaData(catID, presta, prix);
    }
    public void editPresta(Context context, int prestaKey, int catID, String presta, long prix) {
        dbh = new DatabaseHandler(context);
        dbh.setPrestaData(prestaKey,catID, presta, prix);
    }
    public void editCategory(Context context, Category category) {
        dbh = new DatabaseHandler(context);
        dbh.setCatData(category.getId(),category.getName(),category.getImage());
    }

    public List getCats(Context context) {
        dbh = new DatabaseHandler(context);
        return dbh.getListFromTable(tableCatName, 1);
    }

    public String getCat(Context context, int catKey) {
        dbh = new DatabaseHandler(context);
        return dbh.getDataFromKey(tableCatName,catKey,1);
    }

    public int getCatKey(Context context, String category) {
        dbh = new DatabaseHandler(context);
        return dbh.getKey(tableCatName,category);
    }

    public int getCatKeyFromPrestaKey(Context context, int prestaKey) {
        dbh = new DatabaseHandler(context);
        return Integer.parseInt(dbh.getDataFromKey(tablePrestaName,prestaKey,1));
    }

    public boolean isCatExists(Context context,String category) {
        dbh = new DatabaseHandler(context);
        List categories = getCats(context);
        boolean ret = false;
        for (int i=0;i<categories.size();i++) {
            if (categories.get(i).toString().equals(category)) {
                ret = true;
            }
        }
        return ret;
    }

    public List getPrestas(Context context, int catID) {
        dbh = new DatabaseHandler(context);
        return dbh.getPrestasFromCatID(catID);
    }

    public boolean isPrestaExists(Context context,int catID, String presta) {
        dbh = new DatabaseHandler(context);
        List prestations = getPrestas(context,catID);
        boolean ret = false;
        for (int i=0;i<prestations.size();i++) {
            if (prestations.get(i).toString().equals(presta)) {
                ret = true;
            }
        }
        return ret;
    }

    public List getPrices(Context context, List prestaIDs) {
        dbh = new DatabaseHandler(context);
        return dbh.getPrices(prestaIDs);
    }
    public int getPrestaKey(Context context, int catID, String prestation) {
        dbh = new DatabaseHandler(context);
        return dbh.getPrestaKeyFromCatID(tablePrestaName, catID, prestation);

    }

    public String getPresta(Context context, int prestaID) {
        dbh = new DatabaseHandler(context);
        return dbh.getDataFromKey(tablePrestaName,prestaID,2);
    }
    public long getPrice(Context context, int prestaID) {
        dbh = new DatabaseHandler(context);
        long price = 0;
        String sPrice = dbh.getDataFromKey(tablePrestaName,prestaID,3);

        if (!sPrice.equals("")) {
            price = Long.parseLong(sPrice);
        }
        return price;
    }

    public List getPrestaKeys(Context context,int CatKey, List listPresta) {
        dbh = new DatabaseHandler(context);
        return dbh.getPrestaKeys(CatKey,listPresta);
    }

    public void deleteCat(Context context, int key) {
        dbh = new DatabaseHandler(context);
        dbh.deleteData(tableCatName,key);
    }

    public void deletePresta (Context context, int key) {
        dbh = new DatabaseHandler(context);
        dbh.deleteData(tablePrestaName,key);
    }

    public void DefaultCats(Context context) {
        dbh = new DatabaseHandler(context);
        //nettoyage des catégories
        List catKeys = dbh.getListFromTable(tableCatName, 0);
        for (int i=0;i<catKeys.size();i++) {
            deleteCat(context,Integer.parseInt(catKeys.get(i).toString()));
        }
        //nettoyage des prestations
        List prestaKeys = dbh.getListFromTable(tablePrestaName,0);
        for (int i=0;i<prestaKeys.size();i++) {
            deletePresta(context,Integer.parseInt(prestaKeys.get(i).toString()));
        }

        String[] categories= {
            "Soins des mains",
            "Soins des pieds",
            "Forfaits mains & pieds",
            "Epilations",
            "Forfaits épilations",
            "Soins du visage"
            };

        Bitmap[] image={
                null,
                null,
                null,
                null,
                null,
                null};

        for (int i=0;i<categories.length;i++) {
            addCat(context,categories[i],image[i]);
        }

        //soins mains
        int CAT=1;
        String[] prestations= {
                "Pose de vernis",
                "Pose de vernis french",
                "Pose de vernis semi-permanent",
                "Pose de vernis french semi-permanent",
                "Embellissement Mains",
                "Embellissement + vernis Mains",
                "Embellissement + vernis french Mains",
                "Embellissement + vernis semi-permanent Mains",
                "Embellissement + vernis french semi-permanent Mains"
        };

        long[] prix = {
                15,
                20,
                22,
                25,
                25,
                35,
                40,
                42,
                45,
        };

        for (int i=0;i<prestations.length;i++){
            addPresta(context,CAT,prestations[i],prix[i]);
        }

        //soins pieds
        CAT=2;
        prestations= new String[]{
                "Pose de vernis",
                "Pose de vernis french",
                "Pose de vernis semi-permanent",
                "Pose de vernis french semi-permanent",
                "Embellissement Pieds",
                "Embellissement + vernis Pieds",
                "Embellissement + vernis french Pieds",
                "Embellissement + vernis semi-permanent Pieds",
                "Embellissement + vernis french semi-permanent Pieds"
        };

        prix = new long[]{
                15,
                20,
                22,
                25,
                35,
                45,
                50,
                52,
                55
        };

        for (int i=0;i<prestations.length;i++){
            addPresta(context,CAT,prestations[i],prix[i]);
        }

        //Forfaits mains & pieds
        CAT=3;
        prestations= new String[]{
                "Embellissement mains et pieds + vernis",
                "Embellissement mains et pieds + vernis french",
                "Embellissement mains et pieds + vernis semi-permanent",
                "Embellissement mains et pieds + vernis french semi-permanent",
                "Forfait mariée (épilations, mains, pieds, maquillage ...)"
        };

        prix = new long[]{
                75,
                85,
                95,
                100,
                100
        };

        for (int i=0;i<prestations.length;i++){
            addPresta(context,CAT,prestations[i],prix[i]);
        }
        //Epilations
        CAT=4;
        prestations= new String[]{
                "Sourcils, lèvre, menton",
                "Aisselles",
                "Maillot classique",
                "Maillot échancré",
                "Maillot semi-intégral",
                "Maillot intégral",
                "Cuisses",
                "Demi-jambes",
                "Avant-bras",
                "Bras entier",
                "Épaules",
                "Dos",
                "Torse"
        };

        prix = new long[]{
                15,
                15,
                15,
                25,
                30,
                35,
                25,
                20,
                15,
                30,
                20,
                40,
                60
        };

        for (int i=0;i<prestations.length;i++){
            addPresta(context,CAT,prestations[i],prix[i]);
        }

        //Forfaits épilations
        CAT=5;
        prestations= new String[]{
                "Visage",
                "Jambes entières",
                "Épaules, dos et torse",
                "Demi-jambes + maillot ou aisselles",
                "Demi-jambes + maillot + aisselles",
                "Jambes entières + maillot ou aisselles",
                "Jambes entières + maillot + aisselles"
        };

        prix = new long[]{
                35,
                40,
                85,
                30,
                45,
                55,
                70
        };

        for (int i=0;i<prestations.length;i++){
            addPresta(context,CAT,prestations[i],prix[i]);
        }

        //Soins visage
        CAT=6;
        prestations= new String[]{
                "Soin du visage",
                "Teinture de cils",
                "Réhaussement de cils",
                "Teinture de sourcils",
                "Brow-lift",
                "Teinture de cils + réhaussement de cils",
                "Brow-lift + teinture des sourcils + épilation",
                "Make-Up Jour",
                "Make-Up Cocktail",
                "Cours d'auto-maquillage",
                "Mariée : Essai + jour J"
        };

        prix = new long[]{
                70,
                25,
                55,
                15,
                65,
                70,
                80,
                40,
                55,
                70,
                100
        };

        for (int i=0;i<prestations.length;i++){
            addPresta(context,CAT,prestations[i],prix[i]);
        }



    }


}