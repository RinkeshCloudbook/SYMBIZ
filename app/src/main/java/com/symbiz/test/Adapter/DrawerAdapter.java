package com.symbiz.test.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.symbiz.test.Fragment.ClientFragment;
import com.symbiz.test.Fragment.ContactFragment;
import com.symbiz.test.Fragment.FragmentVcard;
import com.symbiz.test.Fragment.OffersFragment;
import com.symbiz.test.Fragment.ProfileFragment;
import com.symbiz.test.Fragment.ServiceFragment;
import com.symbiz.test.MainActivity;

public class DrawerAdapter extends FragmentPagerAdapter {
    Context context;
    int tabCount;
    public Fragment[] mfragment;

    public DrawerAdapter(MainActivity mainActivity, FragmentManager fm, int tabCount) {
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
                ProfileFragment PF = new ProfileFragment();
                mfragment[i] = PF;
                return PF;
            case 2:
                ServiceFragment SF = new ServiceFragment();
                mfragment[i] = SF;
                return SF;
            case 3:
                OffersFragment OF = new OffersFragment();
                mfragment[i] = OF;
                return OF;
            case 4:
                ClientFragment CF = new ClientFragment();
                mfragment[i] = CF;
                return CF;
            case 5:
                ContactFragment COF = new ContactFragment();
                mfragment[i] = COF;
                return COF;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
