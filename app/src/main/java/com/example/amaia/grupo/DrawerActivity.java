package com.example.amaia.grupo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent exp = new Intent(DrawerActivity.this, MainActivity.class);
            exp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(exp);
        } else if (id == R.id.nav_public) {
            Intent exp = new Intent(DrawerActivity.this, ListaExplorar.class);
            exp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(exp);
        } else if (id == R.id.nav_privado) {
            Intent msit = new Intent(DrawerActivity.this, ListaMisSitios.class);
            msit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(msit);
        } else if (id == R.id.nav_anadir) {
            Intent i = new Intent(DrawerActivity.this, AnadirSitioActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }  else if (id == R.id.nav_logout) {
            //elimina el id (si existe) de las preferencias, para salir de la sesion (mantener sesion iniciada se desactiva). Y vuelve al login
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DrawerActivity.this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("user_id");
            editor.apply();
            Intent i2 = new Intent(DrawerActivity.this, LoginActivity.class);
            startActivity(i2);
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
