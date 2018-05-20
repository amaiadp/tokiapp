package com.example.amaia.grupo;

import android.annotation.SuppressLint;
import android.app.Service;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by amaia on 24/04/2018.
 */

public class ServicioFireBase extends FirebaseInstanceIdService{
    public ServicioFireBase() {
    }

    @SuppressLint("WrongThread")
    @Override
    public void onTokenRefresh() {
        Log.i("TOKEN", "onTokenRefresh");




// Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.i("TOKEN", refreshedToken);

        //context null porque no queremos que vuelva tras ejecutarse
        DBRemote dbr = new DBRemote(null, null,"actualizarToken", "token", "token=" +refreshedToken);
        dbr.execute();



    }

}
