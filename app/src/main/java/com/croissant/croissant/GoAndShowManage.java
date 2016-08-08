package com.croissant.croissant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.croissant.croissant.utilities.CustomActivity;
import com.croissant.croissant.utilities.CustomNoActivity;

/**
 * Created by Santiago on 14/03/2016.
 */
public class GoAndShowManage extends CustomActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conferencemoderator);
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

    @Override
    public void customizeViews(){
        super.customizeViews();
        customInfoConference();
        customButtonsManage();
    }
}
