package com.croissant.croissant.Graphics.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.croissant.croissant.Graphics.utils.TabsContactsAdapter;
import com.croissant.croissant.R;
import com.croissant.croissant.ShowInfoConference;
import com.croissant.croissant.ShowInfoUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class graphics extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics);
        ButterKnife.bind(this);

        ShowInfoConference.show(this);

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



}
