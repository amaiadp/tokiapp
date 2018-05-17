package com.example.amaia.grupo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ListaExplorar extends DrawerActivity implements AdapterView.OnItemClickListener,DBRemote.BaseDatosRespueta{

    ListView sitiosLV;
    AutoCompleteTextView buscador;
    Sitio[] sitios;
    Button mostrarEnMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.lista_explorar);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.lista_explorar, null, false);
        drawer.addView(contentView, 0);

        sitiosLV = findViewById(R.id.lista_explorar);
        mostrarEnMapa = findViewById(R.id.mostrarEnMapa);

        mostrarEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBRemote db = new DBRemote(ListaExplorar.this,"seleccionarPublicosMAPS","sitios", "");
                db.execute();
            }
        });

        DBRemote db = new DBRemote(this,"seleccionarPublicos","sitios", "");
        db.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Sitio sitio = (Sitio) sitiosLV.getAdapter().getItem(i);
        Intent intent = new Intent(this, TabsPublico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //mandar sitio
        intent.putExtra("id",sitio.getID());
        intent.putExtra("nombre",sitio.getNombre());
        intent.putExtra("descripcion",sitio.getDescripcion());
        intent.putExtra("latitud",sitio.getLatitud());
        intent.putExtra("longitud",sitio.getLongitud());
        intent.putExtra("tag",sitio.getTAG());
        intent.putExtra("imagen", sitio.getImagen());
        startActivity(intent);
    }

    public void responderDB(String info, String id){
        if(info!=null) {
                switch (id){
                    case "seleccionarPublicos":
                        try {
                            JSONParser parser = new JSONParser();
                            JSONArray array = (JSONArray) parser.parse(info);
                            sitios = new Sitio[array.size()];
                            JSONObject sitio;
                            for (int i = 0; i < array.size(); i++) {
                                sitio = (JSONObject) array.get(i);
                                sitios[i] = new Sitio(sitio.toJSONString());

                            }
                            sitiosLV.setItemsCanFocus(true);
                            ExplorarListAdaptador eladap = new
                                    ExplorarListAdaptador(this, sitios);

                            sitiosLV.setAdapter(eladap);

                            sitiosLV.setOnItemClickListener(this);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "seleccionarPublicosMAPS":
                        Log.i("ListaExplorar", "seleccionarPublicosMAPS");
                        Intent iS = new Intent(ListaExplorar.this, MapsActivity.class);
                        iS.putExtra("listaSitios", info);
                        startActivity(iS);
                        break;
                }

        }


    }

}
