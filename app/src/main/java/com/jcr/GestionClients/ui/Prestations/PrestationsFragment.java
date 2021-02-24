package com.jcr.GestionClients.ui.Prestations;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcr.GestionClients.Keyboard;
import com.jcr.GestionClients.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jcr.GestionClients.fabAnimate;

import java.util.ArrayList;
import java.util.List;

import static com.jcr.GestionClients.MainActivity.fab;
import static com.jcr.GestionClients.MainActivity.toolBarLayout;
import static com.jcr.GestionClients.ui.Prestations.CategoriesFragment.CatKey;

public class PrestationsFragment extends Fragment {


    Boolean         deleteActivated=false;
    RecyclerView    recyclerView;
    PrestationsAdapter adapter;
    List<Prestation> prestations;

    String          TAG = "PrestationFragment";
    Context         context;
    public static int PrestaKey;
    private PrestationsModel prestationsModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_prestations, container, false);
        recyclerView = view.findViewById(R.id.id_rv_presta);
        prestationsModel = new ViewModelProvider(this).get(PrestationsModel.class);

        context = getContext();
        prestations = prestationsModel.getPrestations(context,CatKey);

        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolBarLayout.setTitle(prestationsModel.getCat(context, CatKey));

        UpdateAdapter();
        setFab();
        Keyboard.hide(getActivity(),getView());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //affichage du contenu du menu
        inflater.inflate(R.menu.tool_menu_edit, menu);
        menu.findItem(R.id.id_edit).setIcon(R.drawable.ic_delete);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //Actions du menu d'action
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
            switch (item.getItemId()) {
                case R.id.id_edit:
                    if (deleteActivated){
                        fabAnimate.changeToAdd();
                        deleteActivated = false;
                    } else {
                        fabAnimate.changeToDel();
                        deleteActivated = true;
                    }
                    UpdateAdapter();
//                    NavHostFragment.findNavController(PrestationsFragment.this)
//                            .navigate(R.id.action_nav_ListePrestations_to_nav_EditCategories);

                    return  true;
                default:
                    return super.onOptionsItemSelected(item);
            }
    }

    /*
     ** GESTION DU FLOATING BUTTON
     */
    private void setFab() {
        fabAnimate.showAdd();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAnimate.validate();
                if (!deleteActivated) {
                    PrestaKey = -1;
                    NavHostFragment
                            .findNavController(PrestationsFragment.this)
                            .navigate(R.id.action_nav_ListePrestations_to_nav_EditPresta);
                }else{
                    delPrestations();
                }
            }
        });
    }

    private void delPrestations(){
        for (int i=0;i<prestations.size();i++) {
            if (prestations.get(i).isSelected()){
                prestationsModel.deletePresta(context, prestations.get(i).getId());
                UpdateAdapter();
            }
        }
        prestations = prestationsModel.getPrestations(context,CatKey);
    }

    public void UpdateAdapter(){
        adapter=new PrestationsAdapter(context, this.prestations,deleteActivated);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}