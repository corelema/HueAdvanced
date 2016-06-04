package com.cocorporation.hueadvanced.core.lightgroup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Corentin on 1/12/2016.
 */
public class LightGroupsSectionsPagerAdapter extends FragmentPagerAdapter {

    public LightGroupsSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return new LightGroupsListFragment();
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "ALL LIGHTS";
            case 1:
                return "LIVING ROOM";
            case 2:
                return "ENTRANCE";
            case 3:
                return "OTHER GROUP";
        }
        return null;
    }
}