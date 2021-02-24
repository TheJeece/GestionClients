package com.jcr.GestionClients.ui.Clients;

import android.app.AlertDialog;
import android.app.SearchManager;
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
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.core.app.NotificationCompatSideChannelService;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.String;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jcr.GestionClients.Keyboard;
import com.jcr.GestionClients.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jcr.GestionClients.fabAnimate;

import static android.view.View.VISIBLE;
import static com.jcr.GestionClients.MainActivity.fab;
import static com.jcr.GestionClients.MainActivity.toolBarLayout;


public class ClientsFragment extends Fragment {

    String TAG = "Clients";
    private ClientModel clientModel;
    private ClientImportModel cImport;
    ClientAdapter       adapter;
    RecyclerView        recyclerView;

    static int          ClientID =-1;
    Context             context;
    private SearchView  searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    SearchManager       searchManager;
    public String       searchedString;
    boolean             deleteActivated=false;
    List<Client>        clients;
    List<Client>        filteredClients;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Autorisation d'affichage du menu option (search, delete)
        setHasOptionsMenu(true);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // ClientModelgère la communication avec la database
        clientModel     = new ViewModelProvider(this).get(ClientModel.class);
        cImport         = new ViewModelProvider(this).get(ClientImportModel.class);
        //Création de la vue
        View view = inflater.inflate(R.layout.fragment_client, container, false);
        recyclerView = view.findViewById(R.id.RecyclerView);
        context=getContext();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolBarLayout.setTitle(getString(R.string.menu_Clients));

        //Mise à jour de la vue liste
        UpdateClients();
        UpdateAdapter();
        fabInit();
        Keyboard.hide(getActivity(),getView());
    }

    //Floatingbutton add
    private void fabInit() {
        fabAnimate.showAdd();
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                fabAnimate.validate();
                if (!deleteActivated) {
                    ClientID =-1;
                    if(cImport.HasPermission(context,view)){
                        NavHostFragment
                                .findNavController(ClientsFragment.this)
                                .navigate(R.id.action_nav_Clients_to_EditClient);
                    } else {
//                        cImport.requestPermission(getActivity());
                        ConfirmDialogBox();
                    }


                } else {
                    delClient();
                    UpdateClients();
                    UpdateAdapter();
                }


            }
        });
    }

    public void ConfirmDialogBox (){

        Log.i(TAG, "ConfirmDialogBox: ");
        AlertDialog alertDialog =new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Autorisation requise");
        alertDialog.setMessage("L'autorisation des lecture des contacts est requise pour importer des contacts du répertoire");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Autoriser", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context,  String.valueOf(which), Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                cImport.requestPermission(getActivity());
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, String.valueOf(which), Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Continuer sans autoriser", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context,  String.valueOf(which), Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                NavHostFragment
                        .findNavController(ClientsFragment.this)
                        .navigate(R.id.action_nav_Clients_to_EditClient);
            }
        });
        alertDialog.show();

    }

    /*
     *  MENU D'ACTION
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //affichage du contenu du menu
        inflater.inflate(R.menu.tool_menu_search, menu);

        //GESTION DE LA BARRE DE RECHERCHE
        MenuItem searchItem = menu.findItem(R.id.search);
        searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchContact(searchItem);

        super.onCreateOptionsMenu(menu, inflater);
    }

    //Actions du menu d'action
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.delete:
                if (!deleteActivated) {
                    fabAnimate.changeToDel();
                    deleteActivated = true;
                } else {
                    delClient();
                }
                UpdateAdapter();
                return true;
            case R.id.search:
                deleteActivated=false;
                fabAnimate.changeToAdd();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void delClient (){
        for (int i=0;i<clients.size();i++) {
//            Log.i(TAG, "delClient: " + clients.get(i).getName() + "  " + clients.get(i).isSelected);

            if (clients.get(i).isSelected) {
//                Log.i(TAG, "delClient: "+ clients.get(i).getName());
                clientModel.delete(context, clientModel.getKey(context, clients.get(i).getName()));
            }
        }
        deleteActivated = false;
        fabAnimate.changeToAdd();

        UpdateClients();
    }

    public void UpdateClients() {
        Log.i(TAG, "UpdateClients: ");
        this.clients = new ArrayList<>();
        this.clients = clientModel.getClients(context);
        Collections.sort(this.clients,Client.CompName);
        UpdateFilteredClients ("");

    }

    public void UpdateFilteredClients (String SearchedString) {
        this.filteredClients = new ArrayList<>();
        if (!SearchedString.isEmpty()) {
            for (int i=0;i<this.clients.size();i++) {
                if (this.clients.get(i).getName().toLowerCase().contains(SearchedString.toLowerCase())) {
                    this.filteredClients.add(clients.get(i));
                }
            }
        } else {
            this.filteredClients  = this.clients;
        }
        Collections.sort(this.filteredClients, Client.CompName);

    }

    public void UpdateAdapter() {
        Log.i(TAG, "Mise à jour de la vue clients");

        adapter=new ClientAdapter(context, this.filteredClients, deleteActivated);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void SearchContact(MenuItem searchItem){
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            queryTextListener = new SearchView.OnQueryTextListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onQueryTextChange(String newText) {
//                    Log.i("onQueryTextChange", newText);
                    UpdateFilteredClients(newText);
                    UpdateAdapter();
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
//                    Log.i("onQueryTextSubmit", query);
                    searchedString = query;
                    return false;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
    }

}