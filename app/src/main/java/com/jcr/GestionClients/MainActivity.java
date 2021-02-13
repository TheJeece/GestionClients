package com.jcr.GestionClients;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //scrolling
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
//        toolBarLayout.setTitle(getTitle());
        //scrollin ***


        //Ouverture du layout principal
        DrawerLayout drawer = findViewById(R.id.main_layout);
        Log.i("MainActivity", "drawer null  " + String.valueOf(drawer == null));
        //ouverture du menu latéral
        NavigationView navigationView = findViewById(R.id.nav_view);
        Log.i("MainActivity", "navigationView null  " + String.valueOf(navigationView == null));

        //Construction du menu latéral
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_Sheet,
                R.id.nav_Prestations,
                R.id.nav_Clients,
                R.id.nav_import_export) // rajouter d'autres menus si besoin
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        fab=findViewById(R.id.fab);


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}