package com.croissant.croissant;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

/**
 * Created by brandon on 12/03/16.
 */
public class ShowInfoUser {

    public static final String DEFAULT = "N/A";
    public static TextView nameUser;


    public static void show(Activity activity) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences("myData", activity.MODE_PRIVATE);
        String name = sharedPreferences.getString("nameUser", DEFAULT);
        nameUser = (TextView)activity.findViewById(R.id.nameUser);
        nameUser.setText(activity.getResources().getString(R.string.hi) + name);
    }

    public static void showFromView(Activity activity, View v) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences("myData", activity.MODE_PRIVATE);
        String name = sharedPreferences.getString("nameUser", DEFAULT);
        nameUser = (TextView)v.findViewById(R.id.nameUser);
        nameUser.setText(activity.getResources().getString(R.string.hi) + name);
    }
}
