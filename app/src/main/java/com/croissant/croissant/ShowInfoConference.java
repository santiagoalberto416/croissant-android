package com.croissant.croissant;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

/**
 * Created by brandon on 12/03/16.
 */
public class ShowInfoConference {
    public static final String DEFAULT = "N/A";
    public static TextView tvTitle, tvSpeaker, tvPlace, tvDate;


    public static void show(Activity activity) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences("myData", activity.MODE_PRIVATE);
        String title = sharedPreferences.getString("nameConference", DEFAULT);
        String speaker = sharedPreferences.getString("nameSpeaker", DEFAULT);
        String place = sharedPreferences.getString("placeConference", DEFAULT);
        String date = sharedPreferences.getString("timeConference", DEFAULT);
        tvTitle = (TextView)activity.findViewById(R.id.conferenceTitle);
        tvSpeaker = (TextView)activity.findViewById(R.id.nameSpeaker);
        tvPlace = (TextView)activity.findViewById(R.id.conferencePlace);
        tvDate = (TextView)activity.findViewById(R.id.timeConference);
        tvTitle.setText(title);
        tvSpeaker.setText(speaker);
        tvPlace.setText(place);
        tvDate.setText(date);
    }

    public static void showFromView(Activity activity, View v) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences("myData", activity.MODE_PRIVATE);
        String title = sharedPreferences.getString("nameConference", DEFAULT);
        String speaker = sharedPreferences.getString("nameSpeaker", DEFAULT);
        String place = sharedPreferences.getString("placeConference", DEFAULT);
        String date = sharedPreferences.getString("timeConference", DEFAULT);
        tvTitle = (TextView)v.findViewById(R.id.conferenceTitle);
        tvSpeaker = (TextView)v.findViewById(R.id.nameSpeaker);
        tvPlace = (TextView)v.findViewById(R.id.conferencePlace);
        tvDate = (TextView)v.findViewById(R.id.timeConference);
        tvTitle.setText(title);
        tvSpeaker.setText(speaker);
        tvPlace.setText(place);
        tvDate.setText(date);
    }
}
