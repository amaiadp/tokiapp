package com.example.amaia.grupo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ComentariosListAdaptador extends BaseAdapter {
    private Context contexto;
    private LayoutInflater inflater;
    private Comentario[] comentarios;
    private TextView username,coment;

    public ComentariosListAdaptador(Context pcontext, Comentario[] cmts) {
        contexto = pcontext;
        comentarios = cmts;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return comentarios.length;
    }

    @Override
    public Object getItem(int i) {
        return comentarios[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= inflater.inflate(R.layout.comentario, null);

        username = view.findViewById(R.id.comusuario);
        String usuario = comentarios[i].getUsername();
        username.setText(usuario);

        coment = view.findViewById(R.id.comtext);
        String comentario = comentarios[i].getTexto();
        coment.setText(comentario);

        return view;
    }
}
