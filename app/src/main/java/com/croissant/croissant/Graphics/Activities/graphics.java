package com.croissant.croissant.Graphics.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.croissant.croissant.Graphics.utils.TabsContactsAdapter;
import com.croissant.croissant.R;
import com.croissant.croissant.ShowInfoConference;
import com.croissant.croissant.ShowInfoUser;
import com.croissant.croissant.utilities.CustomNoActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class graphics extends AppCompatActivity {


    private CustomNoActivity utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics);
        ButterKnife.bind(this);
        //getActionBar().hide();
        getSupportActionBar().hide();
        ShowInfoConference.show(this);
        customizeViews();
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        final ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);

        //i need to recibe the id from the conference
        viewPager.setAdapter(new TabsContactsAdapter(getSupportFragmentManager(),1,this));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });


    }

    public void customizeViews(){
        utils = new CustomNoActivity(this);
        ImageView imageView =(ImageView)findViewById(R.id.imageLogo);
        Picasso.with(this)
                .load(this.getResources().getString(R.string.imgUrl)+utils.getLOGO())
                .placeholder(R.id.imgLogo)
                .error(R.drawable.ic_mood_bad_white_48dp)
                .into(imageView)
        ;

        findViewById(R.id.header).setBackgroundColor(Color.parseColor(utils.getDARK()));
        TabLayout layout = (TabLayout)findViewById(R.id.tabLayout);
        layout.setBackgroundColor(Color.parseColor(utils.getPRIMARY()));
        //android:backgroundTint="@color/primary_light"
        findViewById(R.id.infoConference).setBackgroundColor(Color.parseColor(utils.getPRIMARY()));
        ImageView imageIconProfile = (ImageView)findViewById(R.id.imgSpeaker);
        ImageView imageIconPlace = (ImageView)findViewById(R.id.imgPlace);
        ImageView imageIconHour = (ImageView)findViewById(R.id.imgHour);
        imageIconProfile.setColorFilter(Color.parseColor(utils.getLIGHT()));
        imageIconPlace.setColorFilter(Color.parseColor(utils.getLIGHT()));
        imageIconHour.setColorFilter(Color.parseColor(utils.getLIGHT()));
    }



}
