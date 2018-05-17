package com.example.amaia.grupo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.simple.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnadirSitioActivity extends DrawerActivity implements DBRemote.BaseDatosRespueta{

    private Double latitud;
    private Double longitud;
    private EditText et_nombre;
    private EditText et_descripcion;
    private EditText et_comentario;
    private CheckBox cb_privado;
    int PLACE_PICKER_REQUEST = 1;
    private TextView tv_ubicacion;
    private ArrayList<Bitmap> imagenes = new ArrayList<>();
    int IMAGEN_CAMARA = 111;
    int IMAGEN_GALERIA = 222;
    private ImageButton imagenAnadir;
    private Spinner spinner;
    private String[] lista_tag = {"otro","restauracion","cultura","ocio"};


    private String hashMapToUrl(HashMap<String, String> params){
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));

                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_anadir_sitio);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_anadir_sitio, null, false);
        drawer.addView(contentView, 0);




        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion);

        et_comentario = (EditText) findViewById(R.id.et_comentario);

        cb_privado = (CheckBox) findViewById(R.id.cb_privado);
        tv_ubicacion = (TextView) findViewById(R.id.tv_ubiDato);
        spinner = (Spinner) findViewById(R.id.sp_tag);

        Button bt_anadir = (Button) findViewById(R.id.bt_anadir);
        bt_anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsArray = new JSONArray();
                for (int i = 0; imagenes.size()>i; i++ ) {
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    imagenes.get(i).compress(Bitmap.CompressFormat.JPEG, 50, output);
                    byte[] byteArray = output.toByteArray();
                    String imageStr = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    jsArray.add(i,imageStr);
                }





                HashMap<String,String> detail = new HashMap<>();
                detail.put("user_id", String.valueOf(MainActivity.getUserId()));
                detail.put("nombre", String.valueOf(et_nombre.getText()));
                detail.put("descripcion", String.valueOf(et_descripcion.getText()));
                detail.put("comentario", String.valueOf(et_comentario.getText()));
                detail.put("privado", String.valueOf(cb_privado.isChecked()));
                detail.put("latitud", String.valueOf(latitud));
                detail.put("longitud", String.valueOf(longitud));
                detail.put("imagenes",jsArray.toJSONString());
                detail.put("tag", lista_tag[spinner.getSelectedItemPosition()]);


                String params =AnadirSitioActivity.this.hashMapToUrl(detail);
                Log.i("AnadirSitio", "Params:   "+ params);
                DBRemote dbr = new DBRemote(AnadirSitioActivity.this, "anadirSitio", "sitiosMod", params);
                dbr.execute();
            }
        });


        TextView tv_registro = (TextView) findViewById(R.id.tv_ubic);
        tv_registro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AnadirSitioActivity.this);
                builder.setTitle("Elige")
                        .setItems(R.array.ubicacionOpcion, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                if(which==0){//actual
                                    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
                                    boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                                    if(statusOfGPS) {
                                        if (ContextCompat.checkSelfPermission(AnadirSitioActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            //EL PERMISO NO ESTÁ CONCEDIDO, PEDIRLO
                                        } else {
                                            //EL PERMISO ESTÁ CONCEDIDO, EJECUTAR LA FUNCIONALIDAD
                                            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(AnadirSitioActivity.this);
                                            mFusedLocationClient.getLastLocation()
                                                    .addOnSuccessListener(AnadirSitioActivity.this, new OnSuccessListener<Location>() {
                                                        @Override
                                                        public void onSuccess(Location location) {
                                                            if (location != null) {
                                                                latitud = location.getLatitude();
                                                                longitud = location.getLongitude();
                                                                tv_ubicacion.setText(latitud + "," + longitud);
                                                            } else {

                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(AnadirSitioActivity.this, new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });
                                        }
                                    }else{
                                        AlertDialog alertDialog = new AlertDialog.Builder(AnadirSitioActivity.this).create();
                                        alertDialog.setTitle(getResources().getString(R.string.dg_titNOgps));
                                        alertDialog.setMessage(getResources().getString(R.string.dg_msgNOgps));
                                        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                }else{//place picker

                                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();


                                    try {
                                        startActivityForResult(builder.build(AnadirSitioActivity.this), PLACE_PICKER_REQUEST);
                                    } catch (GooglePlayServicesRepairableException e) {
                                        e.printStackTrace();
                                    } catch (GooglePlayServicesNotAvailableException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                builder.show();
            }
        });

        imagenAnadir = (ImageButton) findViewById(R.id.anadirImagen);
        imagenAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AnadirSitioActivity.this);
                builder.setTitle(R.string.selecciona)
                        .setItems(R.array.fotoOpcion, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                if(which==0){//sacar foto
                                    Intent elIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (elIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(elIntent, IMAGEN_CAMARA);
                                    }
                                }else{//coger de galeria
                                    Intent elIntentGal = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(elIntentGal, IMAGEN_GALERIA);
                                }
                            }
                        });
                builder.show();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.i("AnadirSitio", "onActivityResult placePicker");
        if (resultCode == RESULT_OK) {
            if (requestCode == PLACE_PICKER_REQUEST) {


                    Place place = PlacePicker.getPlace(this,data);
                    Log.i("AnadirSitio", "Place: "+place.getName());
                    LatLng ll = place.getLatLng();
                    latitud = ll.latitude;
                    longitud = ll.longitude;
                    tv_ubicacion.setText(latitud+","+longitud);
            }else{
                if (requestCode == IMAGEN_GALERIA) {//GALERIA

                    Uri imagenSeleccionada= data.getData();
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagenSeleccionada);
                        imagenes.add(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(imagenes.size()==1){
                        imagenAnadir.setImageBitmap(imagenes.get(0));
                    }

                }else{
                    if (requestCode == IMAGEN_CAMARA) {//CAMARA


                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imagenes.add(imageBitmap);

                        if(imagenes.size()==1){
                            imagenAnadir.setImageBitmap(imagenes.get(0));
                        }
                    }
                }
            }
        }
    }



    @Override
    public void responderDB(String resultados, String id) {
        switch (id){
            case "anadirSitio":
                Intent i = new Intent(AnadirSitioActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //al volver a la lista para que recargue la actividad
                startActivity(i);
                break;
        }
    }
}
