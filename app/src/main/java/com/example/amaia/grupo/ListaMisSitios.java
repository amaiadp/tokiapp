package com.example.amaia.grupo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ListaMisSitios extends DrawerActivity implements DBRemote.BaseDatosRespueta{

    Sitio[] sitios;
    RecyclerView missitiosRV;
    int usuerID;
    Button mostrarEnMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.lista_mis_sitios);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.lista_mis_sitios, null, false);
        drawer.addView(contentView, 0);

        missitiosRV = findViewById(R.id.lista_mis_sitios);

        usuerID = MainActivity.getUserId();
        DBRemote db = new DBRemote(this,this,"seleccionarPrivados","sitios","userID="+MainActivity.getUserId());
        db.execute();

        mostrarEnMapa = findViewById(R.id.mostrarEnMapa);
        mostrarEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBRemote db = new DBRemote(ListaMisSitios.this, ListaMisSitios.this,"seleccionarPrivadosMAPS","sitios", "userID="+MainActivity.getUserId());
                db.execute();
            }
        });
    }

    @Override
    public void responderDB(String resultados, String id) {
        if (resultados != null) {
            switch (id) {
                case "seleccionarPrivados":
                    JSONParser parser = new JSONParser();
                    JSONArray array = null;
                    try {
                        array = (JSONArray) parser.parse(resultados);
                        sitios = new Sitio[array.size()];
                        JSONObject sitio;
                        for (int i = 0; i < array.size(); i++) {
                            sitio = (JSONObject) array.get(i);
                            sitios[i] = new Sitio(sitio.toJSONString());

                        }
                        RecyclerAdapter eladap = new RecyclerAdapter(sitios);
                        missitiosRV.setAdapter(eladap);
                        GridLayoutManager elLayoutManager = new
                                GridLayoutManager(ListaMisSitios.this, 2, GridLayoutManager.VERTICAL, false);
                        missitiosRV.setLayoutManager(elLayoutManager);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "seleccionarPrivadosMAPS":
                    Log.i("ListaExplorar", "seleccionarPrivadosMAPS");
                    Intent iS = new Intent(ListaMisSitios.this, MapsActivity.class);
                    iS.putExtra("listaSitios", resultados);
                    startActivity(iS);
                    break;
            }

        }
    }
}
