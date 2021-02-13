package com.jcr.GestionClients.ui.Sheet;

import com.jcr.GestionClients.Keyboard;
import com.jcr.GestionClients.fabAnimate;
import com.jcr.GestionClients.ui.Prestations.PrestationsModel;
import com.jcr.GestionClients.R;
import com.jcr.GestionClients.ui.Clients.ClientModel;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.jcr.GestionClients.MainActivity.fab;
import static com.jcr.GestionClients.ui.Sheet.SheetFragment.sheetKey;


public class SheetDetailFragment extends Fragment {

    Context context;
    private RecyclerView rvDetails;
    private TextInputLayout tilDate,tilName,tilSponsor,tilCategory,tilPrestation,tilPrice;
    private AutoCompleteTextView name,sponsor,category,prestation;
    private EditText date,calcPrice,price;
    private Switch paid;
    private ImageButton add,del;
    private ClientModel clientModel;
    private PrestationsModel prestationsModel;
    private SheetModel sheetModel;
    private Sheet sheet;
    public SheetDetail detail;
    private final String TAG = "PrestaSheetAddFragment";

    int Year,Month,Day;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    boolean deleteActivated = false;
    public List<SheetDetail> detailList = new ArrayList<>();

    SheetDetailAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_sheet_add, container, false);

        clientModel = new ViewModelProvider(this).get(ClientModel.class);
        prestationsModel = new ViewModelProvider(this).get(PrestationsModel.class);
        sheetModel = new ViewModelProvider(this).get(SheetModel.class);

        //a modifier lors de l'ouverture d'une fiche
        sheet = new Sheet(-1,null,"","",detailList,0,true,false);
//        sheet = new ViewModelProvider(this).get(PrestaSheet.class);

        rvDetails = view.findViewById(R.id.id_rv_sheet_presta);
        tilDate = view.findViewById(R.id.id_til_sheet_date);
        date = view.findViewById(R.id.id_et_sheet_date);
        tilName = view.findViewById(R.id.id_til_sheet_name);
        name = view.findViewById(R.id.id_actv_sheet_name);
        tilSponsor = view.findViewById(R.id.id_til_sheet_sponsor);
        sponsor = view.findViewById(R.id.id_actv_sheet_sponsor);
        tilCategory = view.findViewById(R.id.id_til_sheet_cat);
        category = view.findViewById(R.id.id_actv_sheet_cat);
        tilPrestation = view.findViewById(R.id.id_til_sheet_presta);
        prestation = view.findViewById(R.id.id_actv_sheet_presta);
        calcPrice = view.findViewById(R.id.id_et_sheet_calc);
        tilPrice = view.findViewById(R.id.id_til_sheet_price);
        price = view.findViewById(R.id.id_et_sheet_price);
        add=view.findViewById(R.id.id_btn_sheet_add);
        del=view.findViewById(R.id.id_btn_sheet_del);
        paid=view.findViewById(R.id.id_switch_sheet_paid);

        //Remplissage de la feuille
        if (sheetKey !=-1) {
            try {
                setSheet();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //Mise en place des contenu de champs
        setDate();
        setName();
        setSponsor();
        setCategory();
        setSwitch();
        setPrice();

        //Mise à jour de l'encart
        updateDetails();


        //Bouton ajouter prestation
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPresta();
            }
        });

        //Bouton supprimer prestation
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delPresta();
            }
        });

        fabInit();

        Keyboard.hide(getActivity(),view);

        return view;
    }

    /*
    INITIALISATION DES FAB
     */

    private void fabInit(){

        if (sheetKey == -1) {
            fabAnimate.showAdd();
        } else {
            fabAnimate.showEdit();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                if (sheetIsValid()) {
                    if (sheetKey == -1) {
                        addSheet();
                        sheetKey = -1;
                    } else {
                        editsheet(sheetKey);
                        sheetKey = -1;
                    }

                    NavHostFragment
                            .findNavController(SheetDetailFragment.this)
                            .navigate(R.id.action_nav_SheetAdd_to_nav_Sheet);

                }
            }
        });
    }

    /*
    INITIALISATION DE LA FEUILLE
    récupère les données contenues dans la bdd et met à jour l'adapter
     */
    private void setSheet() throws ParseException {
        Log.i(TAG, "setSheet: Initialisation de la feuille");
        sheet = sheetModel.getSheet(context,sheetKey);
        date.setText(simpleDateFormat.format(sheet.getDate()));
        name.setText(sheet.getName());
        sponsor.setText(sheet.getSponsor());
        price.setText(String.valueOf(sheet.getPrice()));
        paid.setChecked(sheet.isPaid());
        detailList = sheetModel.getDetails(context,sheetKey);

        if (detailList !=null){
            updateDetails();
        }
    }

    /*
    INITIALISATION DU TEXT INPUT DATE
    avec Datepicker et vérification de format
     */
    public void setDate(){
        Log.i(TAG, "setDate : Initialisation de la date");

        //Méthode d'implémentation du datepicker
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final Calendar c = Calendar.getInstance();

                    if (date.getText().toString().equals("")) {
                        Year = c.get(Calendar.YEAR);
                        Month = c.get(Calendar.MONTH);
                        Day = c.get(Calendar.DAY_OF_MONTH);
                    } else {
                        String [] dateParts = date.getText().toString().split("/");
                        Day = Integer.parseInt(dateParts[0]);
                        Month = Integer.parseInt(dateParts[1])-1;
                        Year = Integer.parseInt(dateParts[2]);
                    }
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            date.setText(dayOfMonth + "/" + (month +1) + "/" + year);
                        }
                    }, Year, Month, Day);
                    datePickerDialog.show();
                }
            }
        });
        //vérification de la validité de la frappe et mise a jour de l'objet fiche
        checkDate();
    }
    private void checkDate() {
        if (date.getText().toString().equals("")) {
            tilDate.setError(getString(R.string.DateEmpty));
        }
        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String [] dateParts = date.getText().toString().split("/");
                if (dateParts.length != 3) {
                    tilDate.setError(getString(R.string.DateFormatIncorrect));
                } else {
                    if (isDateValid(dateParts)) {
                        if (tilDate.getError() != null) {
                            tilDate.setError(null);
                        }
                        try {
                            sheet.setDate(simpleDateFormat.parse(date.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        tilDate.setError(getString(R.string.DateFormatIncorrect));
                    }
                }
            }
        });
    }

    /*
    VERIFICATION DU FORMAT DATE
     */
    private boolean isDateValid(String[] dateParts) {
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        Log.i(TAG, "isDateValid: \nday : " + day + " \nmonth : "+month + "\nYear : "+Year);
        boolean dayOK = false,monthOK=false,yearOK=false;
        if (day>0 && day<=31 && month>0 && month <=12 && year >=1900) {
            yearOK=true;
            monthOK=true;
            if (month == 4 || month == 6 ||month == 9 ||month == 11) {
                if (day <=30) {
                    dayOK=true;
                }
            } else if (month ==2) {
                if (day > 28) {
                    int divYear = Year/4;
                    if (day == 29 && divYear == Year/4) {
                        dayOK=true;
                    }
                }
            } else {
                dayOK=true;
            }
        }
        return dayOK && monthOK && yearOK;
    }

    /*
    INITIALISATION DU TEXT INPUT NAME
    avec contenu dufichier client et mise a jour de l'objet sheet
     */
    public void setName(){
        Log.i(TAG, "setName: Initialisation du nom");
        ArrayAdapter<String> adapter = new ArrayAdapter(
                getContext(),
                R.layout.list_item_textfield,
                clientModel.getNames(getContext())
        );
        name.setAdapter(adapter);
        name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: " +name.getText().toString());
                sheet.setName(name.getText().toString());
                hideKeyboard();
                Snackbar.make(getView(), "Sélectionné " + sheet.getName(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //vérification de la validité de la frappe
        checkName();
    }
    private void checkName(){
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = name.getText().toString();
                if (!query.equals("") && !clientModel.clientExists(context, query)) {
                    tilName.setError(getString(R.string.NameDoesNotExists));
                } else if (query.equals("")) {
                    tilName.setError(getString(R.string.NameEmpty));
                } else {
                    if (tilName.getError()!=null){
                        tilName.setError(null);
                    }
                    sheet.setName(query);
                    Snackbar.make(getView(), "Nom Sélectionné " + sheet.getName(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /*
    INITIALISATION DU TEXT INPUT SPONSOR
    avec contenu du fichier client
     */
    public void setSponsor(){
        Log.i(TAG, "setSponsor: Initialisation du parrain");
        ArrayAdapter<String> adapter = new ArrayAdapter(
                getContext(),
                R.layout.list_item_textfield,
                clientModel.getNames(getContext())
        );
        sponsor.setAdapter(adapter);

        //vérification de la validité de la frappe
        checkSponsor();
    }
    private void checkSponsor(){
        sponsor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = sponsor.getText().toString();
                if (!query.equals("") && !clientModel.clientExists(context, query)) {
                    tilSponsor.setError(getString(R.string.NameDoesNotExists));
                } else if (query.equals("")) {
                    tilSponsor.setError(getString(R.string.NameEmpty));
                } else {
                    if (tilSponsor.getError() !=null) {
                        tilSponsor.setError(null);
                    }
                    sheet.setSponsor(query);
                    hideKeyboard();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /*
    INITIALISATION DUTEXT INPUT CATEGORY
    avec le contenu de la liste des prestations
     */
    public void setCategory () {
        Log.i(TAG, "setCategory: Initialisation des catégories");
        ArrayAdapter<String> adapter = new ArrayAdapter(
                getContext(),
                R.layout.list_item_textfield,
                prestationsModel.getCats(getContext())
        );
        category.setAdapter(adapter);

        //vérification de la validité de la frappe
        checkCategory();
    }
    private void checkCategory(){
        category.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = category.getText().toString();
                if (!query.equals("") && !prestationsModel.isCatExists(context, query)) {
                    tilCategory.setError(getString(R.string.CategoryDoesNotExists));
                } else if (query.equals("")) {
                    tilCategory.setError(getString(R.string.CategoryEmpty));
                } else {
                    if (tilCategory.getError() !=null) {
                        tilCategory.setError(null);
                    }
                    updatePrestations();
                    hideKeyboard();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /*
    INITIALISATION DU SWITCH
     */
    public void setSwitch(){
        Log.i(TAG, "setSwitch: initiatisation du switch");
        paid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sheet.setPaid(isChecked);
            }
        });
    }

    /*
    INITIALISATION DU TEXT INPUT PRIX REMISE
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setPrice(){
        Log.i(TAG, "setPrice: Initialisation du prix");

        price.setText(String.valueOf(sheet.getPrice()));
        checkPrice();
    }
    private void checkPrice() {
        if (!calcPrice.getText().toString().equals("0") && price.getText().toString().equals("0")) {
            tilPrice.setError(getString(R.string.PriceEmpty));
        } else {
            tilPrice.setError(null);
        }
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    sheet.setPrice(0);
                    tilPrice.setError(getString(R.string.PriceEmpty));
                } else {
                    sheet.setPrice(Long.parseLong(s.toString()));
                    if (tilPrice.getError()!=null) {
                        tilPrice.setError(null);
                    }
                }
            }
        });
    }

    /*
    INITIALISATION DU TEXT INPUT PRESTATION
    avec le contenu de la liste des prestations
     */
    public void updatePrestations() {

        if (category.getText().toString().equals("")) {
            tilCategory.setError(getString(R.string.PrestaCatInvalid));

        } else {
            int catID = prestationsModel.getCatKey(context, category.getText().toString());
            ArrayAdapter<String> adapter = new ArrayAdapter(
                    getContext(),
                    R.layout.list_item_textfield,
                    prestationsModel.getPrestas(getContext(), catID)
            );
            prestation.setAdapter(adapter);

            checkPresta(catID);

            //vérification de la validité de la frappe
            prestation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkPresta(catID);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    /*
    VERIFICATION DE L'ENTREE PRESTATIONS
     */
    public void checkPresta(int catID) {
        String query = prestation.getText().toString();
        if (!query.equals("") && !prestationsModel.isPrestaExists(context, catID,query)) {
            tilPrestation.setError(getString(R.string.PrestationDoesNotExists));
        } else if (query.equals("")) {
            tilPrestation.setError(getString(R.string.PrestationEmpty));
        } else {
            if (tilPrestation.getError() != null) {
                tilPrestation.setError(null);
            }
            hideKeyboard();
        }
    }

    /*
    AJOUT D'UNE PRESTATION DANS LA LISTE D'OBJET DetailList
    et mise à jour de l'encart
     */
    public void addPresta() {
        deleteActivated = false;
        if (!category.getText().toString().equals("") && !prestation.getText().toString().equals("")) {

            int catID = prestationsModel.getCatKey(context,category.getText().toString());
            int prestaID = prestationsModel.getPrestaKey(context,catID,prestation.getText().toString());

            detail = new SheetDetail(
                    category.getText().toString(),
                    prestation.getText().toString(),
                    prestationsModel.getPrice(context,prestaID),
                    false
            );

            detailList.add(detail);
            Collections.sort(detailList, SheetDetail.CompCategory);

            updateDetails();
        } else {
            tilCategory.setError(getString(R.string.CategoryEmpty));
            tilPrestation.setError(getString(R.string.PrestationEmpty));
        }
        price.setText(calcPrice.getText().toString());
    }

    /*
    SUPPRESSION D'UNE PRESTATION
     */
    public void delPresta () {
        if (deleteActivated) {

            for (int i=detailList.size()-1;i>=0;i--) {
                if (detailList.get(i).isSelected) {
                    detailList.remove(i);
                }
            }
            deleteActivated = false;
        } else {
            deleteActivated = true;
        }
        updateDetails();

    }

    /*
    MISE A JOUR DES PRESTATIONS
     */
    public void updateDetails(){
        adapter=new SheetDetailAdapter(context,detailList,deleteActivated);
        rvDetails.setAdapter(adapter);
        rvDetails.setLayoutManager(new LinearLayoutManager(context));
        updatePrice();
    }

    /*
    MISE A JOUR CALCUL DES PRIX
     */
    public void updatePrice(){
        long sum=0;
        for (int i=0;i<detailList.size();i++) {
            sum = sum + detailList.get(i).getPrice();
        }
        calcPrice.setText(String.valueOf(sum));
    }

    /*
    AJOUT DE LA FEUILLE DANS LA BDD
    création de la feuille
     */
    public void addSheet(){
        //Création et Récupération de la clé de la nouvelle feuille dans la bdd, hors details

        //TODO a modifier avec mise à jour de l'objet et appel de l'objet dans sheetModel
        sheetKey = sheetModel.CreateSheet(context,
                clientModel.getKey(context, sheet.getName()),
                clientModel.getKey(context, sheet.getSponsor()),
                sheet.getDate(),
                sheet.getPrice(),
                sheet.isPaid()
        );

        saveDetails(context,sheetKey);
    }

    /*
    MISE A JOUR DE LA TABLE DETAILS
    suppression des details existants et création de nouveaux details
     */
    public void saveDetails (Context context, int sheetKey){
        //Suppression des details existants de la bdd
        sheetModel.delDetails(context,sheetKey);

        //Ajout des details de la nouvelle feuille dans la bdd
        for (int i =0;i<detailList.size();i++) {
            int catID = prestationsModel.getCatKey(context, detailList.get(i).getCategory());
            int prestaID = prestationsModel.getPrestaKey(context, catID, detailList.get(i).getPrestation());
            sheetModel.insertDetail(context, sheetKey, prestaID);
        }
    }

    /*
    MISE A JOUR DE LA TABLE FICHE
     */
    public void editsheet(int sheetKey){

        //MIse à jour de la fiche
        sheetModel.editSheet(context,sheetKey,sheet);
        //Mise à jour des details
        saveDetails(context,sheetKey);

    }

    public boolean sheetIsValid() {

        checkDate();
        checkName();
        checkSponsor();
        checkPrice();


        return
                tilDate.getError()==null &&
                tilName.getError()==null &&
                tilSponsor.getError()==null &&
                tilPrice.getError()==null
                ;
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

}
