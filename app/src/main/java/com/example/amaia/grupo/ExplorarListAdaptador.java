package com.example.amaia.grupo;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ExplorarListAdaptador extends BaseAdapter {
    private Context contexto;
    private LayoutInflater inflater;
    private Sitio[] sitios;

    public ExplorarListAdaptador(Context pcontext, Sitio[] sts) {
        contexto = pcontext;
        sitios = sts;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sitios.length;
    }

    @Override
    public Object getItem(int i) {
        return sitios[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= inflater.inflate(R.layout.item_explorar, null);

        TextView tvnombre = (TextView) view.findViewById(R.id.nombre);
        String nombre = sitios[i].getNombre();
        tvnombre.setText(nombre);

        TextView tvdescripcion = (TextView) view.findViewById(R.id.descripcion);
        String descripcion = sitios[i].getDescripcion();
        tvdescripcion.setText(descripcion);

        TextView tvTag = (TextView) view.findViewById(R.id.TAG);
        String TAG = sitios[i].getTAG();
        tvTag.setText(TAG);

        TextView tvDistancia = (TextView) view.findViewById(R.id.distancia);
        //TODO: Conseguir latlong del momento
        double lat = 0.0;
        double lng = 0.0;
        double distancia = sitios[i].calcularDistancia(lat,lng);
        tvDistancia.setText(String.valueOf(distancia));

        ImageView img = (ImageView) view.findViewById(R.id.imagen);
        Bitmap imagen = sitios[i].getPrimeraImagen();
        img.setImageBitmap(imagen);

        return view;
    }
}
