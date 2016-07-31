package com.croissant.croissant.Graphics.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.croissant.croissant.Graphics.Fragments.GeneralInformation;
import com.croissant.croissant.Graphics.Fragments.ListOfQuestions;
import com.croissant.croissant.Graphics.Fragments.WinnersOfConference;
import com.croissant.croissant.R;

/**
 * Created by santiago on 11/07/2016.
 */

public class TabsContactsAdapter extends FragmentPagerAdapter {

        private String tabs_ [] = {"", ""};

        // Constructor
        public TabsContactsAdapter(FragmentManager fragmentManager, int idConference, Context context) {
            super(fragmentManager);
            tabs_[0] = context.getResources().getString(R.string.first_paces);
            tabs_[1] = context.getResources().getString(R.string.scores_of_conference);
        }

        /**
         * this method manage the different instances of frament list
         * in one show the list in general of collaborators
         * and in the other only the teams
         * @param position of the fragment selected
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    WinnersOfConference fragmentAlph = new WinnersOfConference();
                    return fragmentAlph;
                case 1:
                    ListOfQuestions fragmentTeam = new ListOfQuestions();
                    return fragmentTeam;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabs_.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs_[position];
        }
}


