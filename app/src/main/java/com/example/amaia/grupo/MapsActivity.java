package com.example.amaia.grupo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String listaSitios;
    private ArrayList<JSONObject> listaJSON = new ArrayList<>();
    private ArrayList<Marker> listaMarkers = new ArrayList<>();
    private String nombre, descripcion;
    private Double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listaSitios = extras.getString("listaSitios");
            nombre= extras.getString("nombre");
            descripcion = extras.getString("descripcion");
            lat = extras.getDouble("latitud");
            lng = extras.getDouble("longitud");

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        JSONParser parser = new JSONParser();
        try {

            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(statusOfGPS) {
                if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    //EL PERMISO NO ESTÁ CONCEDIDO, PEDIRLO

                    if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                    {
                        // MOSTRAR AL USUARIO UNA EXPLICACIÓN DE POR QUÉ ES NECESARIO EL PERMISO
                        //PEDIR EL PERMISO DE NUEVO
                        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                AnadirSitioActivity.PERMISO_UBICACION);
                    } else {
                        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                               AnadirSitioActivity.PERMISO_UBICACION);
                    }

                } else {
                    //EL PERMISO ESTÁ CONCEDIDO, EJECUTAR LA FUNCIONALIDAD
                    FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {

                                    mMap.setMyLocationEnabled(true);
                                    if(listaSitios!=null &&location!=null){
                                        LatLng sitio =new LatLng(location.getLatitude(),location.getLongitude());
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sitio,15));
                                    }
                                }
                            })
                            .addOnFailureListener(MapsActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            }else{//si no esta el gps activado
                AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
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


            if (listaSitios != null) {

                LatLng sitio=null;
                JSONArray jsonarray = (JSONArray) parser.parse(listaSitios);
                for (int i = 0; i < jsonarray.size(); i++) {
                    JSONObject obj = (JSONObject) jsonarray.get(i);
                    String descripcion = (String) obj.get("descripcion");
                    String nombre = (String) obj.get("nombre");
                    double latitud = Double.parseDouble((String) obj.get("latitud"));
                    double longitud= Double.parseDouble((String) obj.get("longitud"));
                    listaJSON.add(obj);
                    sitio = new LatLng(latitud,longitud);
                    MarkerOptions mark = new MarkerOptions().position(sitio).title(nombre).snippet(descripcion);
                    listaMarkers.add(mMap.addMarker(mark));

                }

//                if(sitio!=null) {
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sitio,15));
//                }







                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        if(listaSitios!=null){
                            int index=  listaMarkers.indexOf(marker);
                            Intent intent;
                            JSONObject json = listaJSON.get(index);
                            if(Integer.parseInt((String) json.get("privado")) > 0){
                                intent = new Intent(MapsActivity.this, MostrarSitioPrivado.class);
                            }else{
                                intent = new Intent(MapsActivity.this, TabsPublico.class);
                            }


                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            int IDSitio = (int) Integer.valueOf((String)json.get("id"));
                            String nombre = (String) json.get("nombre");
                            String descripcion = (String) json.get("descripcion");
                            double latitud = (double) Double.valueOf((String) json.get("latitud"));
                            double longitud = (double) Double.valueOf((String)json.get("longitud"));
                            String TAG = (String)json.get("tag");


                            //mandar sitio
                            intent.putExtra("id",IDSitio);
                            intent.putExtra("nombre",nombre);
                            intent.putExtra("descripcion",descripcion);
                            intent.putExtra("latitud",latitud);
                            intent.putExtra("longitud",longitud);
                            intent.putExtra("tag",TAG);
                            startActivity(intent);
                        }

                    }
                });

            }else{ //si solo hay que mostrar un sitio
                LatLng sitio = new LatLng(lat,lng);
                MarkerOptions mark = new MarkerOptions().position(sitio).title(nombre).snippet(descripcion);
                mMap.addMarker(mark);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sitio,15));

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AnadirSitioActivity.PERMISO_UBICACION: {
                // Si la petición se cancela, granResults estará vacío
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISO CONCEDIDO
                    // EJECUTAR LA FUNCIONALIDAD

                    FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {

                                    mMap.setMyLocationEnabled(true);
                                    if(listaSitios!=null &&location!=null){
                                        LatLng sitio =new LatLng(location.getLatitude(),location.getLongitude());
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sitio,15));
                                    }
                                }
                            })
                            .addOnFailureListener(MapsActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                } else {
                    // PERMISO DENEGADO, DESHABILITAR LA FUNCIONALIDAD
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
                    alertDialog.setTitle(getResources().getString(R.string.dg_sinPermisoLocalizacion));
                    alertDialog.setMessage(getResources().getString(R.string.dg_msg_sinPermisoLocalizacion));
                    alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
                return;
            }
        }
    }


}
