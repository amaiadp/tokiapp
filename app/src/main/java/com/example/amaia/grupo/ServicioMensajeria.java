package com.example.amaia.grupo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by amaia on 24/04/2018.
 */

public class ServicioMensajeria extends FirebaseMessagingService {
    public ServicioMensajeria() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.i("ServivioMensajeria", "entra en la clase");

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //TRATAR LOS DATOS
            double latitud = Double.parseDouble(remoteMessage.getData().get("latitud"));
//            Log.i("ServivioMensajeria", "Latitud  "+latitud);
            double longitud = Double.parseDouble(remoteMessage.getData().get("longitud"));
//            Log.i("ServivioMensajeria", "longitud  "+longitud);
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+latitud+","+longitud+"?q="+latitud+","+longitud));
//            PendingIntent intentEnNot = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT:);

            int id = Integer.parseInt(remoteMessage.getData().get("id"));
            String nombre = remoteMessage.getData().get("nombre");
            String descripcion = remoteMessage.getData().get("descripcion");
            String tag = remoteMessage.getData().get("tag");


            Intent intent = new Intent(this,LoginActivity.class);

            intent.putExtra("id",id);
            intent.putExtra("nombre",nombre);
            intent.putExtra("descripcion",descripcion);
            intent.putExtra("latitud",latitud);
            intent.putExtra("longitud",longitud);
            intent.putExtra("tag",tag);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder nb= new NotificationCompat.Builder(this, "IdCanal");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel nc= new NotificationChannel("Aviso", "SitioNuevo", NotificationManager.IMPORTANCE_DEFAULT);

                nm.createNotificationChannel(nc);
            }
//            nb.setSmallIcon(Integer.parseInt(remoteMessage.getNotification().getIcon()));

            nb.setCategory(NotificationCompat.CATEGORY_STATUS)
                    .setContentIntent(pendIntent);

            nb.setSmallIcon(R.drawable.icono);
            nb.setContentTitle(remoteMessage.getNotification().getTitle());
            nb.setContentText(remoteMessage.getNotification().getBody());
            nb.setVibrate(new long[]{0, 1000, 500, 1000});
            nb.setAutoCancel(true);
            nm.notify(1,nb.build());

            NotificationManager mgr =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mgr.notify(1, nb.build());

        }
    }
}
