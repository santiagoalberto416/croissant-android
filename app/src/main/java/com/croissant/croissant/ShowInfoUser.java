package com.croissant.croissant;

import android.app.Activity;
import android.content.SharedPreferences;
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
        nameUser.setText("Hi, " + name);
    }
}
