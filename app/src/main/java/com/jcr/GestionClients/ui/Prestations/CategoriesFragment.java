package com.jcr.GestionClients.ui.Prestations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcr.GestionClients.Keyboard;
import com.jcr.GestionClients.R;
import com.jcr.GestionClients.fabAnimate;

import java.util.ArrayList;
import java.util.List;

import static com.jcr.GestionClients.MainActivity.fab;
import static com.jcr.GestionClients.MainActivity.toolBarLayout;

import static com.jcr.GestionClients.MainActivity.CLIENT_ID;
import static com.jcr.GestionClients.MainActivity.CAT_ID;
import static com.jcr.GestionClients.MainActivity.SHEET_ID;


public class CategoriesFragment extends Fragment {

//    List categories = new ArrayList<>();
    private PrestationsModel prestationsModel;
    private ListView listview;
    private CheckBox check;
    Category category;
    List<Category> categories;
    private RecyclerView recyclerView;
    Context context;
    CategoriesAdapter adapter;
    boolean deleteActivated;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_categories, container, false);
        recyclerView = view.findViewById(R.id.id_rv_cat);
        context=getContext();

        prestationsModel = new ViewModelProvider(this).get(PrestationsModel.class);
        CAT_ID =-1;



        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolBarLayout.setTitle(getString(R.string.menu_Presta));
        UpdateCategories();
        UpdateAdapter();
        fabInit();
        Keyboard.hide(getActivity(),getView());
    }

    private void fabInit (){
        fabAnimate.showAdd();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAnimate.validate();
                if (deleteActivated) {
                    delCat();
                }else {
                    NavHostFragment
                        .findNavController(CategoriesFragment.this)
                        .navigate(R.id.action_nav_Prestations_to_nav_EditCat);
                }
            }}
        );
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    public void delCat() {
        //suppression des categories selectionnées
        for (int i=0;i<categories.size();i++) {
            if (categories.get(i).isSelected) {
                Log.i("suppression", "Categorie " + categories.get(i).getName());
                List<Prestation> prestations = prestationsModel.getPrestations(context, categories.get(i).getId());
                Log.i("suppression", "Nombre de prestations : " + prestations.size() );
                for (int j=0;j<prestations.size();j++){
                    Log.i("suppression", "suppression de " + prestations.get(j).getName());
                    prestationsModel.deletePresta(context,prestations.get(j).getId());
                }
                prestationsModel.deleteCat(context,categories.get(i).getId());
            }
        }
        UpdateCategories();
        UpdateAdapter();
    }

//    public void addCat() {
//        String newCat = DialogPrompt("Nom de la nouvelle catégorie");
//    }

    /*
    BOITE DE DIALOGUE D'IMPORTATION DES PARAMETRES PAR DEFAUT
    ---- A SUPPRIMER ---
     */
    public void importDefault(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Valeurs par défaut");
        alertDialogBuilder
                .setMessage("Accepter supprimera toutes les prestations et les remplacera par les valeurs par défaut")
                .setCancelable(true)
                .setPositiveButton("Accepter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setDefaultPrestas();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        UpdateCategories();
        UpdateAdapter();
    }



    /*
     * METHODE VIEWHOLDER
     */
    public void UpdateCategories() {
        Toast.makeText(context, "Categories updated", Toast.LENGTH_SHORT).show();
        this.categories = new ArrayList<>();
        this.categories =prestationsModel.getCategories(context);
    }

    public void UpdateAdapter(){
        adapter=new CategoriesAdapter(context, this.categories,deleteActivated);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //affichage du contenu du menu
        inflater.inflate(R.menu.tool_menu_categories, menu);

    }

    //Actions du menu d'action
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.DelCat:
                //Modification de l'apparence de l'icone
                deleteActivated = !deleteActivated;
                if (deleteActivated) {
                    item.setIcon(R.drawable.ic_delete);
                    fabAnimate.showDelete();
                } else {
                    item.setIcon(R.drawable.ic_edit);
                    fabAnimate.showEdit();
                }
                delCat();
                return true;
            case R.id.ImportDefaultCatPresta:
                importDefault();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void setDefaultPrestas(){
        prestationsModel.DefaultCats(getContext());
    }




}