package com.croissant.croissant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.croissant.croissant.ActivityEvent.goAndShowActivity;
import com.croissant.croissant.Graphics.Activities.graphics;
import com.croissant.croissant.utilities.CustomActivity;

/**
 * Created by brandon on 12/03/16.
 */
public class GoAndShowConference extends CustomActivity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conference);
        ShowInfoConference.show(this);
        RelativeLayout showActivity = (RelativeLayout) findViewById(R.id.btnShowActivity);
        showActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), goAndShowActivity.class));
            }
        });

    }

    public void showAskQuestion(View v) {
        startActivity(new Intent(this, GoAndShowAskQuestion.class));
    }

    public void showConferences(View v) {
        startActivity(new Intent(this, GoAndShowConferences.class));
    }

    public void showScores(View v) {
        Intent intent = new Intent(this, graphics.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, GoAndShowConferences.class));
    }

    @Override
    public void customizeViews(){
        super.customizeViews();
        customInfoConference();
        customButtons();

    }

}
