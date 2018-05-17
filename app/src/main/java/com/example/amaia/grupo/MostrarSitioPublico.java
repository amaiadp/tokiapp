package com.example.amaia.grupo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MostrarSitioPublico extends Fragment {

    private int id;
    private double latitud,longitud;
    private EditText etnombre,etdescripcion,etTAG;
    private ImageView imagen;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mostrar_sitio_publico, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle extras = getActivity().getIntent().getExtras();
        View v = getView();

        etdescripcion = v.findViewById(R.id.etdescripcionpublico);
        etnombre = v.findViewById(R.id.etnombrepublico);
        etTAG = v.findViewById(R.id.etTAG);
        imagen = v.findViewById(R.id.imagenPublico);
        if (extras != null) {
            id = extras.getInt("id");
            latitud = extras.getDouble("latitud");
            longitud = extras.getDouble("longitud");
            cargarSitio(extras);
        }


        Button mostrar_en_mapa = v.findViewById(R.id.btnmostrarMapaPublico);
        mostrar_en_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO llamada al mapa
            }
        });

        Button mostrar_imagenes = v.findViewById(R.id.btnmostrarImagenesPublico);
        mostrar_imagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:mostrarImagenes (con ViewPager)
                Intent i = new Intent(getActivity(), MostrarImagenes.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("sitioID", id);
                startActivity(i);
            }
        });

    }

    private void cargarSitio(Bundle extras){
        etnombre.setText(extras.getString("nombre"));
        etdescripcion.setText(extras.getString("descripcion"));
        etTAG.setText(extras.getString("tag"));
        String img = extras.getString("imagen");
        if(img!=null) {
            byte[] imagenByte = Base64.decode(img, Base64.DEFAULT);
            Bitmap imagenBitmap = BitmapFactory.decodeByteArray(imagenByte, 0, imagenByte.length);
            imagen.setImageBitmap(imagenBitmap);
        }
    }
}
