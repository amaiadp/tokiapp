package com.example.amaia.grupo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MostrarComentarios extends Fragment implements DBRemote.BaseDatosRespueta{

    private int id;
    private Comentario[] comentarios;
    private ListView comentariosLV;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mostrar_comentarios, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle extras = getActivity().getIntent().getExtras();
        View v = getView();

        if (extras != null) {
            id = extras.getInt("id");
            comentariosLV = v.findViewById(R.id.lista_comentarios);

            //llamada a conseguirComentarios
            DBRemote db;
            db = new DBRemote(this, null, "conseguirComentarios", "comentarios", "sitioID=" + id);
            db.execute();
        }

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Llamada a Dialog
                DialogNuevoComentario dnc = new DialogNuevoComentario();
                dnc.show();
            }
        });

    }

    @Override
    public void responderDB(String resultados, String id) {
        JSONParser parser = new JSONParser();
        switch (id){
            case "conseguirComentarios":
                try {
                    JSONArray array = (JSONArray)parser.parse(resultados);
                    JSONObject json;
                    comentarios = new Comentario[array.size()];
                    for(int i = 0; i<array.size();i++){
                        json = (JSONObject) array.get(i);
                        comentarios[i] = new Comentario(json.toJSONString());
                    }

                    comentariosLV.setItemsCanFocus(false);
                    ComentariosListAdaptador eladap = new
                            ComentariosListAdaptador(getActivity(),comentarios);
                    comentariosLV.setAdapter(eladap);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case "anadirComentario":
                if(Boolean.parseBoolean(resultados)){
                    Toast.makeText(getActivity(),R.string.mensajeComentarioAnadido, Toast.LENGTH_SHORT).show();
                    DBRemote db;
                    db = new DBRemote(this, null,"conseguirComentarios", "comentarios", "sitioID=" + this.id);
                    db.execute();
                }else {
                    Toast.makeText(getActivity(),R.string.comentarioNoAnadido, Toast.LENGTH_SHORT).show();
                }

        }

    }


    public class DialogNuevoComentario extends Dialog {

        public Button aceptar, cancelar;
        public EditText comentario;

        public DialogNuevoComentario() {
            super(MostrarComentarios.this.getActivity());
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialogo_comentario);
            aceptar = findViewById(R.id.bttnaceptar);
            cancelar = findViewById(R.id.bttncancelar);
            comentario = findViewById(R.id.comentario);
            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mensaje = comentario.getText().toString().trim();
                    if(mensaje.equals("")){
                        Toast.makeText(MostrarComentarios.this.getActivity(),R.string.noRelleno, Toast.LENGTH_SHORT).show();

                    }else{
                        DBRemote db = new DBRemote(MostrarComentarios.this,null,"anadirComentario","comentarios","userID="+MainActivity.getUserId()+"&sitioID="+id+"&texto="+mensaje);
                        db.execute();
                        dismiss();
                    }
                }
            });
            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }



    }
}
