package com.example.amaia.grupo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity implements  DBRemote.BaseDatosRespueta{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Button bt_regis = (Button) findViewById(R.id.bt_registro);
        bt_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et_user = (EditText)findViewById(R.id.et_username);
                EditText et_pass = (EditText) findViewById(R.id.et_password);
                String txt_user = String.valueOf(et_user.getText());
                String txt_pass = String.valueOf(et_pass.getText());
                if(txt_user.isEmpty() || txt_pass.isEmpty()){
                    //si el usuario o contraseña están vacios
                    Toast.makeText(RegistroActivity.this, getResources().getString(R.string.mns_userPassEmpty), Toast.LENGTH_SHORT).show();
                }else{
                    if(txt_user.indexOf(" ")!=-1){
                        //si el usuario tiene al menos un espacio, no vale
                        Toast.makeText(RegistroActivity.this, getResources().getString(R.string.mns_userEspacio), Toast.LENGTH_SHORT).show();
                    }else{

                        DBRemote dbr = new DBRemote(RegistroActivity.this, RegistroActivity.this, "registrarUsuario", "users", "username="+txt_user+"&password="+txt_pass);
                        dbr.execute();
                    }
                }

            }
        });
    }

    @Override
    public void responderDB(String resultados, String id) {
        Log.i("REGISTRO, resultado.  ", resultados);

        if(Boolean.parseBoolean(resultados)){
            Toast.makeText(RegistroActivity.this, getResources().getString(R.string.mns_registroCorrecto), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(RegistroActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }else{
            //si el usuario ya exite en la DB
            Toast.makeText(RegistroActivity.this, getResources().getString(R.string.mns_usuarioYaexiste), Toast.LENGTH_SHORT).show();
        }
    }
}
