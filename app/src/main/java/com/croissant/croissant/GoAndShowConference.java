package com.croissant.croissant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.croissant.croissant.ActivityEvent.goAndShowActivity;

/**
 * Created by brandon on 12/03/16.
 */
public class GoAndShowConference extends Activity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conference);
        ShowInfoUser.show(this);
        ShowInfoConference.show(this);
        Button showActivity = (Button)findViewById(R.id.btnShowActivity);
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
        startActivity(new Intent(this, GoAndShowScoresByConference.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, GoAndShowConferences.class));
    }

}
