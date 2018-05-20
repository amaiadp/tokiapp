package com.example.amaia.grupo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MostrarSitioPrivado extends DrawerActivity implements DBRemote.BaseDatosRespueta{

    private EditText nombre,descripcion;
    private EditText comentario;
    private ImageView imagen;
    private TextView TAG, tv_ubic;
    private int sitioID,comID;
    private String name,descr,tag;
    private Double lat,lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mostrar_sitio_privado);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_mostrar_sitio_privado, null, false);
        drawer.addView(contentView, 0);

        nombre = findViewById(R.id.etnombreprivado);
        descripcion = findViewById(R.id.etdescripcionprivado);
        TAG = findViewById(R.id.etTAGprivado);
        imagen = findViewById(R.id.imagenPrivado);
        comentario = findViewById(R.id.etcomentariosprivado);
        tv_ubic = findViewById(R.id.tv_ubiDato);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sitioID = extras.getInt("id");
            name = extras.getString("nombre");
            descr= extras.getString("descripcion");
            tag = extras.getString("tag");
            lat = extras.getDouble("latitud");
            lng = extras.getDouble("longitud");
            cargarSitio(extras);
            Button borrar_sitio = findViewById(R.id.borrar_privado);
            borrar_sitio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
               //TODO: Borrar de la DB
                    DBRemote db = new DBRemote(MostrarSitioPrivado.this,MostrarSitioPrivado.this,"borrarSitio","sitiosMod","sitioID="+sitioID);
                    db.execute();
                }
            });

            DBRemote db = new DBRemote(this,this, "seleccionarPrimeraImagen", "sitios", "sitioID=" + this.sitioID);
            db.execute();


            Button editar_sitio = findViewById(R.id.editar_privado);
            editar_sitio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO:Editar en la DB
                    String nom,desc;
                    nom = nombre.getText().toString().trim();
                    desc = descripcion.getText().toString().trim();
                    if(nom.equals("")||desc.equals("")){
                        Toast.makeText(MostrarSitioPrivado.this,R.string.noRelleno, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        HashMap<String, String> detalles = new HashMap<>();
                        detalles.put("comentID", String.valueOf(comID));
                        detalles.put("item_comentario", comentario.getText().toString().trim());
                        detalles.put("sitioID", String.valueOf(sitioID));
                        detalles.put("nombre", nom);
                        detalles.put("descripcion", desc);

                        DBRemote db = new DBRemote(MostrarSitioPrivado.this, MostrarSitioPrivado.this, "editarSitio", "sitiosMod", hashMapToUrl(detalles));
                        db.execute();
                    }
                }
            });


            DBRemote db2 = new DBRemote(MostrarSitioPrivado.this,MostrarSitioPrivado.this,"conseguirComentarios","comentarios","sitioID="+sitioID);
            db2.execute();


            Button mostrar_en_mapa = findViewById(R.id.btnmostrarMapaPrivado);
            mostrar_en_mapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MostrarSitioPrivado.this, MapsActivity.class);
                    intent.putExtra("nombre",name);
                    intent.putExtra("descripcion",descr);
                    intent.putExtra("latitud",lat);
                    intent.putExtra("longitud",lng);
                    startActivity(intent);

                }
            });

            Button mostrar_imagenes = findViewById(R.id.btnmostrarImagenesPrivado);
            mostrar_imagenes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent imgns = new Intent(MostrarSitioPrivado.this, MostrarImagenes.class);
                    imgns.putExtra("sitioID",sitioID);
                    imgns.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(imgns);
                }
            });
        }
    }



    private void cargarSitio(Bundle extras){

        nombre.setText(name);
        descripcion.setText(descr);
        TAG.setText(tag);
        DecimalFormat formatter = new DecimalFormat("#0.00");
        tv_ubic.setText("Latitud: " + formatter.format(lat) + ", Longitud: " + formatter.format(lng));

//        String img = extras.getString("imagen");
//        if(img!=null) {
//            byte[] imagenByte = Base64.decode(img, Base64.DEFAULT);
//            Bitmap imagenBitmap = BitmapFactory.decodeByteArray(imagenByte, 0, imagenByte.length);
//            imagen.setImageBitmap(imagenBitmap);
//        }

    }


    @Override
    public void responderDB(String resultados, String id) {
        JSONParser parser = new JSONParser();
        switch (id){
            case "seleccionarPrimeraImagen":
                byte[] imagenByte = Base64.decode(resultados, Base64.DEFAULT);
                Bitmap imagenBitmap = Sitio.decodeSampledBitmapFromResource(imagenByte,100,100);
                imagen.setImageBitmap(imagenBitmap);
            break;
            case "borrarSitio":
                if(Boolean.parseBoolean(resultados)){
                    Toast.makeText(MostrarSitioPrivado.this,R.string.sitioBorrado, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MostrarSitioPrivado.this,ListaMisSitios.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(MostrarSitioPrivado.this,R.string.error, Toast.LENGTH_SHORT).show();
                }
                break;

            case "editarSitio":
                if(Boolean.parseBoolean(resultados)){
                    Toast.makeText(MostrarSitioPrivado.this,R.string.sitioEditado, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MostrarSitioPrivado.this,ListaMisSitios.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(MostrarSitioPrivado.this,R.string.error, Toast.LENGTH_SHORT).show();
                }
                break;

            case "conseguirComentarios":
                try {
                    JSONArray array = (JSONArray) parser.parse(resultados);
                    Comentario com = new Comentario(((JSONObject)array.get(0)).toJSONString());
                    comentario.setText(com.getTexto());
                    Log.w("APP","comID (antes): "+comID);
                    Log.w("APP","comID (item_comentario): "+com.getId());
                    comID = com.getId();
                    Log.w("APP","comID (despues): "+comID);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private String hashMapToUrl(HashMap<String, String> params) {
        try {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()){
                if(first) first = false;
                else result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return result.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
