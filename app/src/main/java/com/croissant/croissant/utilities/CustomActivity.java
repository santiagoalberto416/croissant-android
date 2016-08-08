package com.croissant.croissant.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.croissant.croissant.Graphics.utils.Constants;
import com.croissant.croissant.R;
import com.squareup.picasso.Picasso;

/**
 * Created by santiago on 07/08/2016.
 */
public class CustomActivity extends Activity {


    public String PRIMARY = "N/A";
    public String DARK= "N/A";
    public String LIGHT= "N/A";
    public String ICONS= "N/A";
    public String ASCENT= "N/A";
    public String LOGO= "N/A";

    @Override
    public void onStart() {
        super.onStart();
        getColors();
        customizeViews();

    }

    public void customizeViews(){
        findViewById(R.id.header).setBackgroundColor(Color.parseColor(DARK));
        ImageView imageView =(ImageView)findViewById(R.id.imageLogo);
        Picasso.with(this)
                .load(getResources().getString(R.string.imgUrl)+LOGO)
                .placeholder(R.id.imgLogo)
                .error(R.drawable.ic_mood_bad_white_48dp)
                .into(imageView)
        ;
    }

    public void getColors(){
        SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
        LOGO = sharedPreferences.getString(Constants.LOGO, "");
        PRIMARY = sharedPreferences.getString(Constants.PRIMARY, getString(R.string.primary));
        DARK = sharedPreferences.getString(Constants.DARK, getString(R.string.primary_dark));
        LIGHT = sharedPreferences.getString(Constants.LIGHT, getString(R.string.primary_light));
        ICONS = sharedPreferences.getString(Constants.ICONS, getString(R.string.icons));
        ASCENT = sharedPreferences.getString(Constants.ASCENT, getString(R.string.accent));
    }

    public void customButtons(){

        ImageView imageButton = (ImageView)findViewById(R.id.imageButon);
        ImageView imageButton2 = (ImageView)findViewById(R.id.imageButon2);
        ImageView imageButton3 = (ImageView)findViewById(R.id.imageButon3);
        ImageView imageButton4 = (ImageView)findViewById(R.id.imageButon4);
        imageButton.setColorFilter(Color.parseColor(ASCENT));
        imageButton2.setColorFilter(Color.parseColor(ASCENT));
        imageButton3.setColorFilter(Color.parseColor(ASCENT));
        imageButton4.setColorFilter(Color.parseColor(ASCENT));

        //findViewById(R.id.infoConference).

    }

    public void customButtonsManage(){

        ImageView imageButton = (ImageView)findViewById(R.id.imgBtnConf);
        ImageView imageButton2 = (ImageView)findViewById(R.id.imgBtnCreate);
        ImageView imageButton3 = (ImageView)findViewById(R.id.imgBtnBack);
        imageButton.setColorFilter(Color.parseColor(ASCENT));
        imageButton2.setColorFilter(Color.parseColor(ASCENT));
        imageButton3.setColorFilter(Color.parseColor(ASCENT));

        //findViewById(R.id.infoConference).

    }

    public void customInfoConference(){

        findViewById(R.id.infoConference).setBackgroundColor(Color.parseColor(PRIMARY));
        ImageView imageIconProfile = (ImageView)findViewById(R.id.imgSpeaker);
        ImageView imageIconPlace = (ImageView)findViewById(R.id.imgPlace);
        ImageView imageIconHour = (ImageView)findViewById(R.id.imgHour);
        imageIconProfile.setColorFilter(Color.parseColor(LIGHT));
        imageIconPlace.setColorFilter(Color.parseColor(LIGHT));
        imageIconHour.setColorFilter(Color.parseColor(LIGHT));
    }
}
