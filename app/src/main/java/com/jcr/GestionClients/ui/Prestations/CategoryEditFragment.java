package com.jcr.GestionClients.ui.Prestations;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jcr.GestionClients.Keyboard;
import com.jcr.GestionClients.R;
import com.jcr.GestionClients.fabAnimate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static android.app.Activity.RESULT_OK;
import static com.jcr.GestionClients.MainActivity.fab;
import static com.jcr.GestionClients.ui.Prestations.CategoriesFragment.CatKey;

public class CategoryEditFragment extends Fragment {

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    View view;

    ImageView imCategory;
    TextInputLayout tilCategory;
    TextInputEditText etCategory;
    Button btnAdd;

    Context context;
    String imageName;
    Category category;
    Bitmap bitmapScaled;
    String TAG = "image";
    PrestationsModel prestationsModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_category_edit, container, false);
        tilCategory = view.findViewById(R.id.id_til_Category);
        etCategory = view.findViewById(R.id.id_et_Category);
        imCategory = view.findViewById(R.id.id_iv_Category);
        btnAdd = view.findViewById(R.id.id_btn_Category);
        prestationsModel = new ViewModelProvider(this).get(PrestationsModel.class);
        context = getContext();

        setContent();
        setCategoryListener();
        setBtnListener();
        setFab();
        Keyboard.hide(getActivity(),view);

        return view;
    }

    private void setCategoryListener(){
        etCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (CatKey == -1 && prestationsModel.isCatExists(context,s.toString())){
                    tilCategory.setError(getString(R.string.CategoryAlreadyExists));
                    fab.setVisibility(View.GONE);
                } else {
                    tilCategory.setError(null);
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /*
     * Définit le listener sur le fab et l'action associée
     */
    private void setFab() {
        if (CatKey == -1) {
            fabAnimate.showAdd();
        } else {
            fabAnimate.showEdit();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCat();
                NavHostFragment
                    .findNavController(CategoryEditFragment.this)
                    .popBackStack();
            }
        });
    }

    /*
     * Crée ou met à jour une entrée dans la base de donnée
     */
    private void saveCat (){
        if (CatKey == -1) {
            //Crée une nouvelle category
            prestationsModel.addCat(context,etCategory.getText().toString(),bitmapScaled);
        } else {
            category.setName(etCategory.getText().toString());
            category.setImage(bitmapScaled);
            prestationsModel.editCategory(context,category);
        }
    }

    /*
     * Définit le contenu de etCategory et imCategory si un client a été sélectionné
     */
    private void setContent(){
        if (CatKey != -1) {
            category = prestationsModel.getCategory(context, CatKey);
            etCategory.setText(category.getName());
            imCategory.setImageBitmap(category.getImage());
        }
    }

    /*
     * Définit un listener sur le bouton
     */
    private void setBtnListener(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPicture();
            }
        });
    }

    /*
     * Lance la gallery pour le choix d'une image
     */
    private void EditPicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    /*
     * Récupère les données de la gallery
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
                    bitmapScaled = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
                    imCategory.setImageBitmap(bitmapScaled);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

}
