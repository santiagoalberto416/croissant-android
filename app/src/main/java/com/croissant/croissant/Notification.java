package com.croissant.croissant;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by brandon on 24/03/16.
 */
public class Notification extends Activity {

    private static Activity activityToShowNotification;
    private static String titleNotification;
    private static String textNotification;
    private static String tickerNotification;

    public static String getTitleNotification() {
        return titleNotification;
    }

    public static void setTitleNotification(String titleNotification) {
        Notification.titleNotification = titleNotification;
    }

    public static String getTextNotification() {
        return textNotification;
    }

    public static void setTextNotification(String textNotification) {
        Notification.textNotification = textNotification;
    }

    public static String getTickerNotification() {
        return tickerNotification;
    }

    public static void setTickerNotification(String tickerNotification) {
        Notification.tickerNotification = tickerNotification;
    }


    public Activity getActivityToShowNotification() {
        return activityToShowNotification;
    }

    public void setActivityToShowNotification(Activity activityToShowNotification) {
        this.activityToShowNotification = activityToShowNotification;
    }


    public static void showNotification()
    {
        Bitmap largeIcon = BitmapFactory.decodeResource(activityToShowNotification.getResources(), R.mipmap.ic_new_question);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(activityToShowNotification)
                .setSmallIcon(R.mipmap.ic_notification)
                .setLargeIcon(largeIcon)
                .setContentTitle(titleNotification)
                .setContentText(textNotification)
                .setTicker(tickerNotification);
        Intent iNotification = new Intent(activityToShowNotification, GoAndShowTrivia.class);
        PendingIntent intentPendiente = PendingIntent.getActivity(activityToShowNotification, 0, iNotification, 0);

        notification.setContentIntent(intentPendiente);
        NotificationManager nm = (NotificationManager) activityToShowNotification.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1,notification.build());
    }

    public void deleteNotifications()
    {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
    }
}
