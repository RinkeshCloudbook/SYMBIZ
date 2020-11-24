package com.symbiz.test.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.symbiz.test.Fragment.ContactFragment;
import com.symbiz.test.Fragment.FragmentVcard;
import com.symbiz.test.Fragment.GallaryFragment;
import com.symbiz.test.Fragment.OffersFragment;
import com.symbiz.test.Fragment.ServiceFragment;
import com.symbiz.test.MainActivity;

public class TabAdapter extends FragmentPagerAdapter {
    Context context;
    int tabCount;
    public Fragment[] mfragment;

    public TabAdapter(MainActivity mainActivity, FragmentManager fm, int tabCount) {
        super(fm);
        context = mainActivity;
        this.tabCount = tabCount;
        mfragment = new Fragment[tabCount];
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                FragmentVcard fVcard = new FragmentVcard();
                mfragment[i] = fVcard;
                return fVcard;
            case 1:
                ServiceFragment PF = new ServiceFragment();
                mfragment[i] = PF;
                return PF;
            case 2:
                OffersFragment SF = new OffersFragment();
                mfragment[i] = SF;
                return SF;
            case 3:
                GallaryFragment OF = new GallaryFragment();
                mfragment[i] = OF;
                return OF;
            case 4:
                ContactFragment CF = new ContactFragment();
                mfragment[i] = CF;
                return CF;

        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
