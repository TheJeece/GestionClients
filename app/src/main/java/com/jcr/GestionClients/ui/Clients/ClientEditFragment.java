package com.jcr.GestionClients.ui.Clients;

import android.app.DatePickerDialog;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcr.GestionClients.Keyboard;
import com.jcr.GestionClients.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.jcr.GestionClients.fabAnimate;
import com.jcr.GestionClients.ui.Sheet.Sheet;
import com.jcr.GestionClients.ui.Sheet.SheetModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.jcr.GestionClients.MainActivity.fab;
import static com.jcr.GestionClients.MainActivity.CLIENT_ID;
import static com.jcr.GestionClients.MainActivity.SHEET_ID;

public class ClientEditFragment extends Fragment {
    private static final String TAG = "EDIT";
    private ClientModel clientModel;
    private ClientImportModel cImport;

    List                phone, mail, address, bDay;
    TextInputLayout     tilName,tilPhone,tilAddress,tilMail,tilBday;
    AutoCompleteTextView actvName, actvPhone, actvAddress, actvMail, actvBday;
    EditText            etNote;
    Client              client;
    int                 importedClientID;
    private int         Year,Month, Day;
    List<List<String>>  contactsList;
    List<String>        names;
    Context             context;
    SimpleDateFormat    simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    RecyclerView        recyclerView;
    TextView            textView;
    CardView            cardView;
    SheetModel          sheetModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        clientModel     = new ViewModelProvider(this).get(ClientModel.class);
        cImport         = new ViewModelProvider(this).get(ClientImportModel.class);

        View view = inflater.inflate(R.layout.fragment_client_edit, container, false);

        tilName         = view.findViewById(R.id.id_til_client_name);
        actvName        = view.findViewById(R.id.id_actv_client_name);
        actvPhone       = view.findViewById(R.id.id_actv_client_phone);
        actvMail        = view.findViewById(R.id.id_actv_client_mail);
        actvAddress     = view.findViewById(R.id.id_actv_client_address);
        actvBday        = view.findViewById(R.id.id_actv_client_bday);
        etNote          = view.findViewById(R.id.id_et_client_note);
        recyclerView    = view.findViewById(R.id.id_rv);
        cardView        = view.findViewById(R.id.id_cv_presta);
        textView        = view.findViewById(R.id.id_tv_presta);

        client = new Client("","","","",null,"",false);


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        //initialisation des boutons
        fabInit();

        sheetModel = new ViewModelProvider(this).get(SheetModel.class);

        //initialisation des champs
        init(view);

        //Gestion du clic sur un espace vide
        view.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // Hiding the keyboard
                    Keyboard.hide(getActivity(),view);
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(actvName.getWindowToken(), 0);
                }
            }
        );

        Keyboard.hide(getActivity(),getView());

        //initialisation des listeners sur les champs;
        listenersInit();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //affichage du contenu du menu
        inflater.inflate(R.menu.tool_menu_edit_client, menu);
        if (CLIENT_ID ==-1) {
            menu.findItem(R.id.id_delete).setVisible(false);
            menu.findItem(R.id.id_import).setVisible(false);
        } else {
            menu.findItem(R.id.id_delete).setVisible(true);
            menu.findItem(R.id.id_import).setVisible(false);
        }
    }

    //Actions du menu d'action
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.id_delete:
                delClient();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void init(View view){
        //si client sélectionné
        if (CLIENT_ID != -1) {
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle(getString(R.string.EditClient));
            client = new Client(clientModel.getName(context, CLIENT_ID),
                    clientModel.getPhone(getContext(), CLIENT_ID),
                    clientModel.getAddress(getContext(), CLIENT_ID),
                    clientModel.getMail(getContext(), CLIENT_ID),
                    clientModel.getBday(getContext(), CLIENT_ID),
                    clientModel.getNote(getContext(), CLIENT_ID),
                    false);
            actvName.setText(client.getName());
            setFields();
            textView.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);

            setPrestaAdapter();

        }   else {
            textView.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
        }

        //initialisation des contacts importés
        if (cImport.HasPermission(context)) {
            contactsList = cImport.getContactList(context);
            //initialisation des noms de l'adapter
            setNames();

            //initialisation des adapters
            updateNamesAdapter();
            updatePhonesAdapter();
            updateMailsAdapter();
            updateAddressesAdapter();
            updateBdaysAdapter();
        }else{

            List<String> nameIDnull     = new ArrayList<>();
            List<String> namenull       = new ArrayList<>();

            nameIDnull.add("");
            namenull.add("");

            contactsList = new ArrayList<>();
            contactsList.add(nameIDnull);
            contactsList.add(namenull);


        }

    }

    public void setPrestaAdapter() {
        List<Sheet> sheets = sheetModel.getSheets(context);
        List<Sheet> clientSheets = new ArrayList<>();

        if (sheets!=null) {
            for (int i = 0;i<sheets.size();i++) {
                if (sheets.get(i).getName().equals(client.getName())) {
                    clientSheets.add(sheets.get(i));
                }
            }
            RecyclerView.Adapter adapter=new ClientEditAdapter(context, clientSheets);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

        } else {
            textView.setText("Aucune prestation enregistrée");
            recyclerView.setVisibility(View.GONE);
        }

    }



    public void fabInit() {
        //fabADD
//        fabAdd = view.findViewById(R.id.fabAdd);
        if (CLIENT_ID ==-1) {
            fabAnimate.showAdd();
        }else {
            fabAnimate.hide();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAnimate.validate();
                if (CLIENT_ID !=-1) {
                    editClient(CLIENT_ID);
                    CLIENT_ID = -1;
                } else {
                    addClient();
                }

                    NavHostFragment.findNavController(ClientEditFragment.this).popBackStack();
            }
        });
    }

    public void listenersInit() {
        actvName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                tilName.setError(null);
                tilName.setHelperText(null);
                client.reset();
                client.setName(s.toString());
                resetFields();
                updateNamesAdapter();

                //Nom vide
                if (client.getName().isEmpty()) {
                    fabAnimate.hide();
                    tilName.setError(getString(R.string.EmptyField));
                    // tous champs identiques pour suppression

                    //si l'entrée est à modifier
                }else if (CLIENT_ID !=-1) {
                    fabAnimate.showEdit();

                // si l'entrée est n'est pas dans la bdd est est une partie d'un nom du répertorire
                } else if (!NameExists(client.getName()) && StringInContactsList(client.getName())){

                    if (NameInContactsList(client.getName())) {
                        tilName.setHelperText(getString(R.string.NameInContactList));
                        importedClientID = ContactListID(client.getName());

                        updatePhonesAdapter();
                        updateAddressesAdapter();
                        updateBdaysAdapter();
                        updateMailsAdapter();
                    } else {
                        resetFields();
                    }

                    fabAnimate.showAdd();

                //Si non vide et client sélectionné
                } else if (NameExists(s.toString())) {

                    //Vérifier que l'utilisateur veux changer le nom des clients
                    //Attentions aux fiches contenant ce nom, le nom du client des fiches sera également modifié
                    tilName.setHelperText(getString(R.string.NameExists));
                    CLIENT_ID = client.getID(context);
                    setFields();
                    fabAnimate.showEdit();

                }
            }
        });

        actvPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String phone = actvPhone.getText().toString();
                if (phone.length() == 10 ) {
                    actvPhone.setText(PhoneFormat(phone));
                }
            }
        });

        actvAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (CLIENT_ID !=-1 && !s.equals(client.getAddress())) {
                    fabAnimate.showEdit();
                }
            }
        });

        actvMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (CLIENT_ID !=-1 && !s.equals(client.getEmail())) {
                    fabAnimate.showEdit();
                }
            }
        });

        //Méthode d'implémentation du datepicker
        actvBday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final Calendar c = Calendar.getInstance();

                    if (actvBday.getText().toString().equals("")) {
                        Year = c.get(Calendar.YEAR);
                        Month = c.get(Calendar.MONTH);
                        Day = c.get(Calendar.DAY_OF_MONTH);
                    } else {
                        String [] dateParts = actvBday.getText().toString().split("/");
                        Day = Integer.parseInt(dateParts[0]);
                        Month = Integer.parseInt(dateParts[1])-1;
                        Year = Integer.parseInt(dateParts[2]);
                    }
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            actvBday.setText(dayOfMonth + "/" + (month +1) + "/" + year);
                        }
                    }, Year, Month, Day);
                    datePickerDialog.show();

                }
            }

        });
        actvBday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (CLIENT_ID !=-1 && !s.equals(client.getbDay())) {
                    fabAnimate.showEdit();
                }
            }
        });

        etNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (CLIENT_ID !=-1 && !s.equals(client.getNote())) {
                    fabAnimate.showEdit();
                }
            }
        });
    }

    public boolean StringInContactsList(String name) {
        //Réinitialisation des noms de l'adapter
        this.names = new ArrayList();

        //Vérification de la présence de la saisie dans la liste de contact
        for (int i=0;i<contactsList.get(1).size();i++) {
            if (contactsList.get(1).get(i).toLowerCase().contains(name.toLowerCase())) {
                this.names.add(contactsList.get(1).get(i));
            }
        }

        boolean ret = true;
        if (this.names.size() == 0) {
            ret = false;
        }
        return ret;
    }

    public boolean NameInContactsList(String name) {
        //Réinitialisation des noms de l'adapter

        //Vérification de la présence de la saisie dans la liste de contact
        boolean ret = false;
        for (int i=0;i<contactsList.get(1).size();i++) {
            if (contactsList.get(1).get(i).toLowerCase().equals(name.toLowerCase())) {
                ret = true;
            }
        }

        return ret;
    }
    public int ContactListID(String name) {
        int id = -1;
        for (int i=0;i<contactsList.get(0).size();i++){
            if (contactsList.get(1).get(i).toLowerCase().equals(name.toLowerCase())) {
                id = Integer.parseInt(contactsList.get(0).get(i));
            }
        }
        return id;
    }

    public boolean NameExists(String name) {
        List<Client> clients = clientModel.getClients(context);
        boolean ret = false;
        for (int i=0;i<clients.size();i++) {
            if (clients.get(i).name.toLowerCase().equals(name.toLowerCase())) {
                ret = true;
            }
        }
        return ret;
    }

    public void setNames () {
        names = contactsList.get(1);
    }

    public void updateNamesAdapter() {
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(context, R.layout.list_item_textfield, names);
        actvName.setAdapter(namesAdapter);
    }

    public void setFields () {
        client.setPhone(clientModel.getPhone(context, CLIENT_ID));
        actvPhone.setText(client.getPhone());

        client.setAddress(clientModel.getAddress(context, CLIENT_ID));
        actvAddress.setText(client.getAddress());

        client.setEmail(clientModel.getMail(context, CLIENT_ID));
        actvMail.setText(client.getEmail());

        client.setbDay(clientModel.getBday(context, CLIENT_ID));
        if (client.getbDay()==null) {
            actvBday.setText("");
        }else {
            actvBday.setText(simpleDateFormat.format(client.getbDay()));
        }

        client.setNote(clientModel.getNote(context, CLIENT_ID));
        etNote.setText(client.getNote());

    }

    public void resetFields () {
        actvPhone.setAdapter(null);
        actvPhone.setText("");

        actvAddress.setAdapter(null);
        actvAddress.setText("");

        actvMail.setAdapter(null);
        actvMail.setText("");

        actvBday.setAdapter(null);
        actvBday.setText("");
    }

    public void updatePhonesAdapter () {
        phone = new ArrayList();
        phone = phoneListFormat(cImport.getPhonesByID(getContext(), importedClientID));
        if (phone.size() >0 ) {
            actvPhone.setText(phone.get(0).toString());
        }
        ArrayAdapter<String> phoneAdapter = new ArrayAdapter(
                getContext(),
                R.layout.list_item_textfield,
                phone
        );
        actvPhone.setAdapter(phoneAdapter);
    }

    public void updateMailsAdapter () {
        mail = new ArrayList();
        mail = cImport.getMailsByID(getContext(), importedClientID);
        if (mail.size() >0 ) {
            actvMail.setText(mail.get(0).toString());
        }
        ArrayAdapter<String> mailAdapter = new ArrayAdapter(
                getContext(),
                R.layout.list_item_textfield,
                mail
        );
        actvMail.setAdapter(mailAdapter);
    }

    public void updateAddressesAdapter () {
        address = new ArrayList();
        address= cImport.getAddressesByID(getContext(), importedClientID);
        if (address.size() >0 ) {
            actvAddress.setText(address.get(0).toString());
        }
        ArrayAdapter<String> addressAdapter = new ArrayAdapter(
                getContext(),
                R.layout.list_item_textfield,
                address
        );
        actvAddress.setAdapter(addressAdapter);
    }

    public void updateBdaysAdapter () {
        bDay = new ArrayList<>();
        bDay = cImport.getbDayById(context,importedClientID);
        if (bDay.size() >0 ) {
            actvBday.setText(simpleDateFormat.format(bDay.get(0)));
        }
        ArrayAdapter<String> bDayAdapter = new ArrayAdapter<>(
                context,
                R.layout.list_item_textfield,
                bDay
        );
        actvBday.setAdapter(bDayAdapter);
    }

    public void addClient(){
        //Au clic, Récupération du contenu de la inputbox dans la variable label
        String name = actvName.getText().toString();
        String phone = actvPhone.getText().toString();
        String mail = actvMail.getText().toString();
        String address = actvAddress.getText().toString();
        String bday = actvBday.getText().toString();
        String note = etNote.getText().toString();

        // Hiding the keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(actvName.getWindowToken(), 0);

        //si non vide
        if (!name.isEmpty() && !clientModel.clientExists(getActivity(),name)) {
            //Ouverture de la base db
            clientModel.Add(getActivity().getApplicationContext(), name, phone, mail, address, bday, note);

            //Réinitialisation de l'imputbox
            actvName.setText("");
            actvPhone.setText("");
            actvMail.setText("");
            actvAddress.setText("");
            actvBday.setText("");

            //Confirmation
            CLIENT_ID = -1;

            Snackbar.make(getView(), "Ajouté", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_nav_AddClient_to_Clients);


            //si vide
        } else if (clientModel.clientExists(getActivity(),name)) {
            Snackbar.make(getView(), "Le nom " + name + " existe déjà", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Snackbar.make(getView(), "Entrer un Nom", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void editClient(int Key) {
        clientModel.setName(getContext(),Key,actvName.getText().toString());
        clientModel.setPhone(getContext(),Key, actvPhone.getText().toString());
        clientModel.setAddress(getContext(),Key, actvAddress.getText().toString());
        clientModel.setMail(getContext(),Key, actvMail.getText().toString());
        clientModel.setBday(getContext(),Key, actvBday.getText().toString());
        clientModel.setNote(getContext(),Key, etNote.getText().toString());
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_AddClient_to_Clients);
    }

    public void delClient () {
        Context context = getActivity().getApplicationContext();
        
        clientModel.delete(context, clientModel.getKey(context, actvName.getText().toString()));
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_AddClient_to_Clients);

    }

    public String PhoneFormat(String phone) {
        phone = phone.replace(" ","")
        .replace("0033","0")
        .replace("+33","0")
        .replace("-","")
        .replace("(","")
        .replace(")","");

        String fPhone =  phone.replaceFirst("(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d+)","$1 $2 $3 $4 $5");
    Log.i(TAG, "PhoneFormat: " + fPhone);
        return fPhone;
    }

    public List phoneListFormat(List phones) {
        List fphone =  new ArrayList();
        for (int i=0;i<phones.size();i++) {
            fphone.add(PhoneFormat(phones.get(i).toString()));
        }
        return fphone;
    }



}
