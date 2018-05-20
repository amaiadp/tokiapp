package com.example.amaia.grupo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by amaia on 17/04/2018.
 */

public class DBRemote extends AsyncTask<Void, Void, String> {

    private BaseDatosRespueta responder;
    private Context contexto;
    private String id;
    private String phpName;
    private String params;

    private ProgressDialog dialog;  //dialogo de espera

    public interface BaseDatosRespueta{
        void responderDB(String resultados, String id);
    }

    public DBRemote(BaseDatosRespueta responder,Context context, String id, String php, String pparams) {

        this.responder = responder;
        this.contexto = context;
        this.id = id;
        this.phpName = php;
        this.params = pparams;
        if(contexto!=null) {//contexto==null ,sin actividad
            this.dialog = new ProgressDialog(contexto);
        }

    }

    @Override
    protected void onPreExecute() {
        if(dialog!=null) {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
    }

    @Override
    protected String doInBackground(Void... voids) {

        Log.i("DB", "Entra en DBRemote");

        try {
            String param = "id=" + this.id;
            String st = "http://galan.ehu.eus/adepablo002/WEB/grupo/" + phpName + ".php";
            URL targetURL = new URL(st);
            HttpURLConnection urlConnection = (HttpURLConnection) targetURL.openConnection();

            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setDoOutput(true);

            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(param+"&"+this.params);
            out.close();
            int statusCode = urlConnection.getResponseCode();


            if (statusCode == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                StringBuilder resultBuilder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    resultBuilder.append(line);
                }
                String result = resultBuilder.toString();
                inputStream.close();


                return result;
            }




        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (dialog!=null &&dialog.isShowing()) {
            dialog.dismiss();
        }

        Log.i("DB POSTexe", "resultado"+ result);
        if(responder!=null) {
            ((BaseDatosRespueta) this.responder).responderDB(result, this.id);
        }
    }
}
