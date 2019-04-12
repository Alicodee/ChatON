package com.chaton.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.chaton.Fragments.CameraFragment;
import com.chaton.Fragments.Chat_Fragment;
import com.chaton.Fragments.ContactsFragment;

public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                 CameraFragment tab1 = new CameraFragment();
                return tab1;
            case 1:
                 Chat_Fragment tab2 = new Chat_Fragment();
                return tab2;
            case 2:
                ContactsFragment tab3 = new ContactsFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
