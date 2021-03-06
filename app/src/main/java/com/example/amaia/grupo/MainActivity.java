package com.example.amaia.grupo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends DrawerActivity{

    private static int userId;


    private static String username;

    public static void setUserId(int userId) {
        MainActivity.userId = userId;
    }

    public static void setUsername(String username) {
        MainActivity.username = username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);


        Bundle extras = getIntent().getExtras(); //se necesita el id del usuario para poder saber cuales son sus sitios
        if (extras != null) {
            userId = extras.getInt("user_id");
            username = extras.getString("user_name");
        }


        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawer.addView(contentView, 0);


        CardView añadir = findViewById(R.id.main_añadir);
        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AnadirSitioActivity.class);
                i.putExtra("calling-activity","MainActivity");
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        CardView explorar = findViewById(R.id.main_explorar);
        explorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent exp = new Intent(MainActivity.this, ListaExplorar.class);
                exp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(exp);
            }
        });

        CardView misSitios = findViewById(R.id.main_missitios);
        misSitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent msit = new Intent(MainActivity.this, ListaMisSitios.class);
                msit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(msit);
            }
        });



    }

    public static int getUserId(){
        return userId;
    }


    public static String getUsername() {
        return username;
    }

}
