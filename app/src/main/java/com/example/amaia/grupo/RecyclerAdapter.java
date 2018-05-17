package com.example.amaia.grupo;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.laclaseViewHolder>{
    private static Sitio[] sitios;


    public RecyclerAdapter(Sitio[] psitios) {
        sitios = psitios;
    }

    @Override
    public laclaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mis_sitios,null);
        laclaseViewHolder rcv = new laclaseViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(laclaseViewHolder holder, int position) {
        holder.nombre.setText(sitios[position].getNombre());
        holder.descripcion.setText(sitios[position].getDescripcion());
        holder.foto.setImageBitmap(sitios[position].getPrimeraImagen());
    }

    @Override
    public int getItemCount() {
        return sitios.length;
    }

    public static class laclaseViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre,descripcion;
        public ImageView foto;

        public laclaseViewHolder(View v) {
            super(v);
            nombre = v.findViewById(R.id.textonombre);
            descripcion = v.findViewById(R.id.textodescripcion);
            foto = v.findViewById(R.id.foto);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("APP","getLayoutPosition(): "+getLayoutPosition());
                    Log.i("APP","getAdapterPosition(): "+getAdapterPosition());
                    Sitio sitio = sitios[getAdapterPosition()];
                    Intent intent = new Intent(view.getContext(),MostrarSitioPrivado.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    //mandar sitio
                    intent.putExtra("id",sitio.getID());
                    intent.putExtra("nombre",sitio.getNombre());
                    intent.putExtra("descripcion",sitio.getDescripcion());
                    intent.putExtra("latitud",sitio.getLatitud());
                    intent.putExtra("longitud",sitio.getLongitud());
                    intent.putExtra("tag",sitio.getTAG());
                    intent.putExtra("imagen", sitio.getImagen());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

}
