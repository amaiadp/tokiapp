package com.example.amaia.grupo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AnadirImagenesAdapter extends RecyclerView.Adapter<AnadirImagenesAdapter.laclaseViewHolder>{

    private static ArrayList<Bitmap> imagenes;


    public AnadirImagenesAdapter(ArrayList<Bitmap> pimagenes) {
        imagenes = pimagenes;
    }

    @Override
    public laclaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagenes_anadir,null);
        final laclaseViewHolder rcv = new laclaseViewHolder(layoutView);
        rcv.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPos = rcv.getAdapterPosition();
                Log.w("APP", "Posicion en el adapter: "+adapterPos);
                Log.w("APP", "Tamaño del adapter: "+imagenes.size());
                if (adapterPos!= RecyclerView.NO_POSITION){
                    imagenes.remove(adapterPos);
                    notifyItemRemoved(adapterPos);
                }
            }
        });
        return rcv;
    }

    @Override
    public void onBindViewHolder(laclaseViewHolder holder, int position) {
        Bitmap bitmap = imagenes.get(position);
        holder.foto.setImageBitmap(imagenes.get(position));
    }

    @Override
    public int getItemCount() {
        return imagenes.size();
    }

    @Override
    public void onBindViewHolder(laclaseViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public static class laclaseViewHolder extends RecyclerView.ViewHolder {
        public ImageView foto;
        public Button eliminar;

        public laclaseViewHolder(View v) {
            super(v);
            foto = v.findViewById(R.id.foto);
            eliminar = v.findViewById(R.id.deleteImagen);

        }
    }

}
