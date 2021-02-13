package com.jcr.GestionClients.ui.Sheet;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcr.GestionClients.Keyboard;
import com.jcr.GestionClients.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jcr.GestionClients.fabAnimate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.jcr.GestionClients.MainActivity.fab;

public class SheetFragment extends Fragment {

    SheetAdapter adapter;
    RecyclerView recyclerView;
    Context context;
    SheetModel sheetModel;
    static int sheetKey=-1;
    boolean deleteActivated=false;
    public String searchedString;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    SearchManager searchManager;

    FloatingActionButton fabAdd;
    FloatingActionButton fabDel;

    List<Sheet> listSheets;
    List<Sheet> filteredSheets;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        sheetModel = new ViewModelProvider(this).get(SheetModel.class);

        setHasOptionsMenu(true);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sheet, container, false);
        recyclerView = view.findViewById(R.id.id_recyclerView_Sheet);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabInit();

        UpdateSheet();
        UpdateFilteredSheets("");
        UpdateAdapter();
        Keyboard.hide(getActivity(),getView());
    }

    /*
     *    GESTION DE LA BARRE DE RECHERCHE ET DU MENU
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //affichage du contenu du menu
        inflater.inflate(R.menu.tool_menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchSheet(searchItem);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
     * Actions du menu d'action
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.delete:
                delSheet();
                return true;
            case R.id.search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * GESTION DU FLOATING BUTTON
     */
    private void fabInit(){
        fabAnimate.showAdd();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "test",Toast.LENGTH_SHORT).show();
                if (deleteActivated) {
                    delSheet();
                }else
                {
                    sheetKey = -1;
                    NavHostFragment.findNavController(SheetFragment.this)
                        .navigate(R.id.action_nav_sheet_to_nav_sheetAdd);
                }

            }
        });
    }

    /*
     * ACTION A L'APPUI DU BOUTON DE SUPPRESSION
     */
    public void delSheet(){
        if (deleteActivated){
            deleteActivated=false;
            sheetKey = -1;

            for (int i = 0 ; i<listSheets.size();i++) {
                if (listSheets.get(i).isSelected) {
                    sheetModel.delete(context, listSheets.get(i).getID());
                }
            }
            UpdateSheet();
            UpdateFilteredSheets("");
            UpdateAdapter();

            fabAnimate.changeToAdd();

        } else {
            deleteActivated=true;
            fabAnimate.changeToDel();
            UpdateAdapter();
        }
    }

    public void SearchSheet(MenuItem searchItem){
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            queryTextListener = new SearchView.OnQueryTextListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    UpdateFilteredSheets(newText);
                    UpdateAdapter();
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    searchedString = query;
                    return false;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
    }

    public void UpdateSheet() {
        this.listSheets = new ArrayList<>();
        this.listSheets = sheetModel.getSheets(context);
        Collections.sort(this.listSheets, Sheet.CompDate);
    }

    public void UpdateFilteredSheets (String searchedString) {
        this.filteredSheets = new ArrayList<>();
        if (!searchedString.equals("")) {
            for (int i = 0; i < listSheets.size(); i++) {
                if (listSheets.get(i).getName().toLowerCase().contains(searchedString.toLowerCase())) {
                    filteredSheets.add(listSheets.get(i));
                }
            }
        } else {
            filteredSheets = listSheets;
        }
    }

    public void UpdateAdapter(){
        adapter=new SheetAdapter(context, this.filteredSheets,deleteActivated);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }
}