package hu.lova.cinemapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID="notification_channel";
    private final int NOTIFICATION_ID= 0;
    private NotificationManager notificationManager;
    private Context nmContext;

    public NotificationHelper(Context context){
        this.notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.nmContext=context;
        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
            return;
        }
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "notification", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        channel.setDescription("CinemApp értésítés");
        notificationManager.createNotificationChannel(channel);
    }

    public void send(String message){
        Intent intent=new Intent(nmContext, TicketActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(nmContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(nmContext, CHANNEL_ID).setContentTitle("CinemApp mozijegy vásárlás").setContentText(message).setSmallIcon(R.drawable.cart).setContentIntent(pendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel(){
        notificationManager.cancel(NOTIFICATION_ID);
    }

}
