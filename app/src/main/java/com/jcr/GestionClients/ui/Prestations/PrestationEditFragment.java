package com.jcr.GestionClients.ui.Prestations;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.jcr.GestionClients.Keyboard;
import com.jcr.GestionClients.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.jcr.GestionClients.fabAnimate;

import java.util.List;

import static com.jcr.GestionClients.MainActivity.fab;
import static com.jcr.GestionClients.ui.Prestations.PrestationsFragment.PrestaKey;


public class PrestationEditFragment extends Fragment {

    final String TAG = "PrestationEditFragment";

    List                    listCat;

    AutoCompleteTextView    tvCategories;
    TextInputEditText       etPresta;
    TextInputEditText       etPrice;
    String                  category;
    String                  prestation;
    long                    price;

    private PrestationsModel prestationsModel;
    ArrayAdapter<String>    catAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: PrestationEditFragment");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        prestationsModel = new ViewModelProvider(this).get(PrestationsModel.class);
        View view = inflater.inflate(R.layout.fragment_list_prestation_edit, container, false);

        listCat = prestationsModel.getCats(getContext());
        tvCategories =  view.findViewById(R.id.id_actv_list_presta_cat);
        etPresta =      view.findViewById(R.id.id_et_list_presta);
        etPrice =       view.findViewById(R.id.id_et_list_presta_price);

        //Gestion du clic sur un espace vide
        hideKeyboardOnClick(view);

        setAdapter();
        setCategories();
        tvCategories.setAdapter(catAdapter);
        setPrestations();
        setPrice();

        //fabADD
        fabInit();

        Keyboard.hide(getActivity(),view);
        return view;

    }

    private void fabInit() {
        if (PrestaKey ==-1) {
            fabAnimate.showAdd();
        }else {
            fabAnimate.showEdit();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAnimate.validate();
                if (PrestaKey == -1) {
                    addPresta();
                } else {
                    editPresta();
                }
            }
        });
    }

    private void setAdapter(){
        catAdapter = new ArrayAdapter(
                getContext(),
                R.layout.list_item_textfield,
                listCat
        );
    }

    private void setPrice(){
        etPrice.setText(String.valueOf(prestationsModel.getPrice(getContext(), PrestaKey)));
    }

    private void setPrestations(){
        etPresta.setText(prestationsModel.getPresta(getContext(), PrestaKey));
    }

    private void setCategories(){
        tvCategories.setText(prestationsModel.getCat(getContext(), CategoriesFragment.CatKey));
        tvCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = tvCategories.getText().toString();
                Snackbar.make(getView(), "Sélectionné " + category, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void addPresta () {
        category = tvCategories.getText().toString();
        prestation = etPresta.getText().toString();
        price = Long.parseLong(etPrice.getText().toString());
        prestationsModel.addPresta(getContext(), prestationsModel.getCatKey(getContext(), category), prestation, price);
        PrestaKey=-1;
        hideKeyboard();
        NavHostFragment.findNavController(PrestationEditFragment.this).popBackStack();
    }

    public void editPresta () {
        category = tvCategories.getText().toString();
        prestation = etPresta.getText().toString();
        price = Long.parseLong(etPrice.getText().toString());
        prestationsModel.editPresta(getContext(),PrestaKey, prestationsModel.getCatKey(getContext(), category), prestation, price);
        PrestaKey=-1;
        NavHostFragment.findNavController(PrestationEditFragment.this).popBackStack();
    }

    private void hideKeyboardOnClick(View view){
        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        hideKeyboard();
                    }
                }
        );
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

}