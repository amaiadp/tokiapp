package com.example.amaia.grupo;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String listaSitios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listaSitios = extras.getString("listaSitios");
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

            if (listaSitios != null) {

                LatLng sitio=null;
                JSONArray jsonarray = (JSONArray) parser.parse(listaSitios);
                for (int i = 0; i < jsonarray.size(); i++) {
                    JSONObject obj = (JSONObject) jsonarray.get(i);
                    String descripcion = (String) obj.get("descripcion");
                    double latitud = Double.parseDouble((String) obj.get("latitud"));
                    double longitud= Double.parseDouble((String) obj.get("longitud"));
                    sitio = new LatLng(latitud,longitud);
                    mMap.addMarker(new MarkerOptions().position(sitio).title(descripcion));
                }

                if(sitio!=null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sitio,15));
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
