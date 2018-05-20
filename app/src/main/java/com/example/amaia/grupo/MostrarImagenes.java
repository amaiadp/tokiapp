package com.example.amaia.grupo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;

public class MostrarImagenes extends DrawerActivity implements DBRemote.BaseDatosRespueta{

    private ArrayList<String> imagenes;
    private int sitioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mostrar_imagenes);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_mostrar_imagenes, null, false);
        drawer.addView(contentView, 0);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            if(extras.containsKey("action")){
                if(extras.get("action").equals("borrar")){
                    imagenes = AnadirSitioActivity.getImagenes();
                    ImagePagerAdapter eladap = new ImagePagerAdapter(this,imagenes);
                    ViewPager vp = findViewById(R.id.pager_imagenes);
                    vp.setAdapter(eladap);
                }
            }
            else {
                sitioID = extras.getInt("sitioID");

                DBRemote db = new DBRemote(this, "conseguirImagenes", "imagenes", "sitioID=" + sitioID);
                db.execute();
            }

        }

    }

    @Override
    public void responderDB(String resultados, String id) {
        JSONParser parser = new JSONParser();
        try {
            JSONArray array = (JSONArray)parser.parse(resultados);
            imagenes = new ArrayList<>();
            JSONObject json;
            for(int i = 0; i<array.size();i++) {
                json = (JSONObject)array.get(i);
                imagenes.add((String)json.get("imagen"));
            }
            ImagePagerAdapter eladap = new ImagePagerAdapter(this,imagenes);
            ViewPager vp = findViewById(R.id.pager_imagenes);
            vp.setAdapter(eladap);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
