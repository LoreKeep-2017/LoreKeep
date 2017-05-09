package com.example.ilya.lorekeep.auth;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_ITEMS = 2;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return LoginFragment.newInstance("Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return SignUpFragment.newInstance("Page # 2");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return "LogIn";
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return "SignUp";
            default:
                return null;
        }
    }
}
