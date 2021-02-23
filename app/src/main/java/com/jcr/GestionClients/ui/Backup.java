package com.jcr.GestionClients.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.jcr.GestionClients.R;

import static com.jcr.GestionClients.BuildConfig.APPLICATION_ID;

public class Backup extends Fragment {
    Context context;

    String dbName;
//    String dbName = "dbGestionClients";
    String packageName;
//    String packageName = "com.jcr.GestionClients";
    String backupDirectory;
//    String backupDirectory = "/GestionClients/";
    String TAG="Backup";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //creating a new folder for the database to be backuped to
        Log.i(TAG, "onCreateView: created");
        File direct = new File(Environment.getExternalStorageDirectory() + "/Exam Creator");
        Log.i(TAG, "onCreate: " + direct.toString());
        if (!direct.exists()) {
            if (direct.mkdir()) {
                //directory is created;
            }
        }
//        exportDB();
//        importDB();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_import_export, container, false);

        context = getContext();

        dbName = context.getString(R.string.DBname);
        packageName = APPLICATION_ID;
        backupDirectory = context.getString(R.string.BackupDir);

        Button btn_import= view.findViewById(R.id.id_btn_import);
        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importDB();
            }
        });

        Button btn_export = view.findViewById(R.id.id_btn_export);
        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDB();
            }
        });
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

        //importing database
    private void importDB() {
        // TODO Auto-generated method stub
        Log.i(TAG, "importDB: ");
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + packageName
                        + "//databases//" + dbName;
                String backupDBPath = backupDirectory+dbName;
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getActivity().getBaseContext(), backupDB.toString(),
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(getActivity().getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    //exporting database
    private void exportDB() {
        // TODO Auto-generated method stub
        Log.i(TAG, "exportDB ");
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                Log.i(TAG, "exportDB: Ecriture autorisée");

                String currentDBPath = "//data//" + packageName
                        + "//databases//" + dbName;
                String backupDBPath = backupDirectory+dbName;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getActivity().getBaseContext(), "Fichier créé dans " + backupDB.toString(),
                        Toast.LENGTH_LONG).show();

            }else{
                Log.i(TAG, "exportDB: Ecriture interdite");
            }
        } catch (Exception e) {

            Toast.makeText(getActivity().getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();

        }
    }

}
