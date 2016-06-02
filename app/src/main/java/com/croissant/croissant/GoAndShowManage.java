package com.croissant.croissant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Santiago on 14/03/2016.
 */
public class GoAndShowManage extends Activity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conferencemoderator);
        ShowInfoUser.show(this);
        ShowInfoConference.show(this);
    }

    public void manageConference(View v) {
        startActivity(new Intent(this, GoAndShowQuestionsManage.class));
    }

    public void showConferences(View v) {
        startActivity(new Intent(this, GoAndShowConferences.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, GoAndShowConferences.class));
    }
}
