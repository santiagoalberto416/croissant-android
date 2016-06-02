package com.croissant.croissant;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by brandon on 12/03/16.
 */
public class GoAndShowConference extends Activity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conference);
        ShowInfoUser.show(this);
        ShowInfoConference.show(this);
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
