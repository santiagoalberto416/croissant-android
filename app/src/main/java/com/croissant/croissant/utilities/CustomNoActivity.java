package com.croissant.croissant.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

import com.croissant.croissant.Graphics.utils.Constants;
import com.croissant.croissant.R;
import com.squareup.picasso.Picasso;

/**
 * Created by santiago on 07/08/2016.
 */
public class CustomNoActivity {



    private String LOGO = "N/A";
    private String PRIMARY = "N/A";
    private String DARK= "N/A";
    private String LIGHT= "N/A";
    private String ICONS= "N/A";
    private String ASCENT= "N/A";
    private Context context;

    public CustomNoActivity(Context context){
        this.context = context;
        getColors();

    }

    public void getColors(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myData", Context.MODE_PRIVATE);
        LOGO = sharedPreferences.getString(Constants.LOGO, "");
        PRIMARY = sharedPreferences.getString(Constants.PRIMARY, context.getString(R.string.primary));
        DARK = sharedPreferences.getString(Constants.DARK, context.getString(R.string.primary_dark));
        LIGHT = sharedPreferences.getString(Constants.LIGHT, context.getString(R.string.primary_light));
        ICONS = sharedPreferences.getString(Constants.ICONS, context.getString(R.string.icons));
        ASCENT = sharedPreferences.getString(Constants.ASCENT, context.getString(R.string.accent));
    }

    public String getDARK() {
        return DARK;
    }

    public void setDARK(String DARK) {
        this.DARK = DARK;
    }

    public String getPRIMARY() {
        return PRIMARY;
    }

    public void setPRIMARY(String PRIMARY) {
        this.PRIMARY = PRIMARY;
    }

    public String getLIGHT() {
        return LIGHT;
    }

    public void setLIGHT(String LIGHT) {
        this.LIGHT = LIGHT;
    }

    public String getICONS() {
        return ICONS;
    }

    public void setICONS(String ICONS) {
        this.ICONS = ICONS;
    }

    public String getASCENT() {
        return ASCENT;
    }

    public void setASCENT(String ASCENT) {
        this.ASCENT = ASCENT;
    }

    public String getLOGO() {
        return LOGO;
    }

    public void setLOGO(String LOGO) {
        this.LOGO = LOGO;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
