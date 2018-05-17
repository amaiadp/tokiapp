package com.example.amaia.grupo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by zihara on 24/04/18.
 */

public class Sitio {

    private int IDSitio;
    private String nombre;
    private String descripcion;
    private boolean privado;
    private double latitud;
    private double longitud;
    private String imagen;
    private Comentario[] comentarios;
    private String TAG;
    private int usuario;
    JSONObject json;
    JSONObject coms;

    public Sitio(String pjson) {
        try {
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(pjson);
            IDSitio = (int) Integer.valueOf((String)json.get("id"));
            nombre = (String) json.get("nombre");
            descripcion = (String) json.get("descripcion");
            privado = (boolean) Boolean.valueOf((String)json.get("privado"));
            latitud = (double) Double.valueOf((String)json.get("latitud"));
            longitud = (double) Double.valueOf((String)json.get("longitud"));
            TAG = (String)json.get("tag");
            usuario = (int) Integer.valueOf((String)json.get("user_id"));
            imagen = (String) json.get("imagen");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public double calcularDistancia(double lat, double lng){
        //TODO: Formula distancia
        return 0.0;
    }

    public int getID() {
        return IDSitio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isPublico() {
        return !privado;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

//    public String[] getImagenes() {
//        return imagenes;
//    }

    public Comentario[] getComentarios() {
        return comentarios;
    }

    public String getTAG() {
        return TAG;
    }

    public int getUsuario() {
        return usuario;
    }

    public Bitmap getPrimeraImagen(){
        String imagenString;
        if (imagen!=null) {
            imagenString = imagen;
            byte[] imagenByte = Base64.decode(imagenString, Base64.DEFAULT);
            Bitmap imagenBitmap = BitmapFactory.decodeByteArray(imagenByte, 0, imagenByte.length);
            return imagenBitmap;
        }
        return null;
    }

    public String getImagen() {
        return imagen;
    }

//    public void putComentarios(JSONArray coments){
//
//    }

}
