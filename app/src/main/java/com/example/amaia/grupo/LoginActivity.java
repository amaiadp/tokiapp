package com.example.amaia.grupo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LoginActivity extends AppCompatActivity implements  DBRemote.BaseDatosRespueta {

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        extras = getIntent().getExtras();
        if (prefs.contains("user_id")) {
            if(extras!=null && extras.getString("tag", null)!=null){
                Log.i("LOGIN", "mantener sesion + aviso");
                MainActivity.setUserId(prefs.getInt("user_id", -1));
                MainActivity.setUsername(prefs.getString("user_name", ""));
                Intent i = new Intent(this,TabsPublico.class);
                i.putExtras(extras);
                startActivity(i);
                this.finish();
            }else {
                Log.i("LOGIN", "mantener sesion - aviso");
                //si en las preferencias existe el id del usuario (mantener sesion), abre directamente el main
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("user_id", prefs.getInt("user_id", -1));
                i.putExtra("user_name", prefs.getString("user_name", ""));
                startActivity(i);
                this.finish();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et_user = (EditText) findViewById(R.id.et_username);
                EditText et_pass = (EditText) findViewById(R.id.et_password);
                String txt_user = String.valueOf(et_user.getText());
                String txt_pass = String.valueOf(et_pass.getText());
                DBRemote dbr = new DBRemote(LoginActivity.this, LoginActivity.this, "comprobarUsuario", "users", "username=" + txt_user + "&password=" + txt_pass);
                dbr.execute();
            }
        });

        TextView tv_registro = (TextView) findViewById(R.id.txck_registrarse);
        tv_registro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void responderDB(String resultados, String id) {

//        Log.i("LOGIN, resultado.    ", resultados);
        JSONParser parser = new JSONParser();
        try {

            if (resultados != null) {
                JSONArray jsonarray = (JSONArray) parser.parse(resultados);
                //el usuario existe

                if (jsonarray.size() > 0) {

                    CheckBox cb_sesion = (CheckBox) findViewById(R.id.cb_mantenerSesion);
                    JSONObject obj = (JSONObject) jsonarray.get(0);
                    int idUser = Integer.parseInt((String) obj.get("id"));
                    String username = (String) obj.get("username");
                    if (cb_sesion.isChecked()) {
                        //si el checkBox de mantener sesion esta activado, guardar el id del usuario en las preferencias
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();

                        editor.putInt("user_id", idUser);
                        editor.putString("user_name", username);
                        editor.apply();


                        if(extras!=null && extras.getString("tag", null)!=null){
                            Log.i("LOGIN", "mantener sesion (primera vez) + aviso");
                            Intent i = new Intent(this,TabsPublico.class);

                            MainActivity.setUserId(idUser);
                            MainActivity.setUsername(username);
                            i.putExtras(extras);
                            startActivity(i);
                            LoginActivity.this.finish();
                        }else {
                            Log.i("LOGIN", "mantener sesion (primera vez) - aviso");
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.putExtra("user_id", idUser);
                            i.putExtra("user_name", username);
                            startActivity(i);
                            LoginActivity.this.finish();
                        }
                    } else {
                        if(extras!=null && extras.getString("tag", null)!=null){
                            Log.i("LOGIN", "NO mantener sesion + aviso");
                            Intent i = new Intent(this,TabsPublico.class);
                            MainActivity.setUserId(idUser);
                            MainActivity.setUsername(username);
                            i.putExtras(extras);
                            startActivity(i);
                            LoginActivity.this.finish();
                        }else {
                            Log.i("LOGIN", "NO mantener sesion - aviso");
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.putExtra("user_id", idUser);
                            i.putExtra("user_name", username);
                            startActivity(i);
                        }
                    }

                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.mns_datosLoginIncorrectos), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
