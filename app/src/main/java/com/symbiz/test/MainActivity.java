package com.symbiz.test;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.symbiz.test.Activity.Login_user;
import com.symbiz.test.Adapter.DrawerItemCustomAdapter;
import com.symbiz.test.Adapter.TabAdapter;
import com.symbiz.test.Common.CommomDataset;
import com.symbiz.test.Fragment.DirectoryFragment;
import com.symbiz.test.Fragment.FragmentVcard;
import com.symbiz.test.Fragment.InquiryFragment;
import com.symbiz.test.Fragment.MessageFragment;
import com.symbiz.test.Fragment.WalkInsFragment;
import com.symbiz.test.Fragment.WebsiteFragment;
import com.symbiz.test.Model.DataModel;

public class MainActivity extends AppCompatActivity {

    private static final String APP_TITLE = "SYMBiz";
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    ActionBarDrawerToggle mDrawerToggle;
    TabLayout tab_layout;
    ViewPager view_pager;
    FrameLayout content_frame;
    SharedPreferences sp;

    private int[] tabIcons = {
            R.drawable.ic_tab_home,
            R.drawable.ic_tab_profile,
            R.drawable.ic_tab_services,
            R.drawable.ic_tab_offers,
            R.drawable.ic_tab_client
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content_frame = findViewById(R.id.content_frame);
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        //actionBar hide
      /*  if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }*/

        sp = getSharedPreferences("userDetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        String uId = sp.getString("uId","");
        ((TextView) findViewById(R.id.txt_drwUid)).setText(sp.getString("N",""));
        ((TextView) findViewById(R.id.txt_drwEmail)).setText(sp.getString("E",""));

        setupToolbar();
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        view_pager = findViewById(R.id.view_pager);
        tab_layout.addTab(tab_layout.newTab().setText("About"));
        tab_layout.addTab(tab_layout.newTab().setText("Service"));
        tab_layout.addTab(tab_layout.newTab().setText("Offers"));
        tab_layout.addTab(tab_layout.newTab().setText("Gallery"));
        tab_layout.addTab(tab_layout.newTab().setText("Contact"));

        tab_layout.getTabAt(0).setIcon(tabIcons[0]);
        tab_layout.getTabAt(1).setIcon(tabIcons[1]);
        tab_layout.getTabAt(2).setIcon(tabIcons[2]);
        tab_layout.getTabAt(3).setIcon(tabIcons[3]);
        tab_layout.getTabAt(4).setIcon(tabIcons[4]);


        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabAdapter tabAdapter = new TabAdapter(this, getSupportFragmentManager(), tab_layout.getTabCount());
        view_pager.setAdapter(tabAdapter);

        view_pager.setOffscreenPageLimit(tab_layout.getTabCount());

        //view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                content_frame.setVisibility(View.GONE);
                view_pager.setVisibility(View.VISIBLE);
                view_pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        DataModel[] drawerItem = new DataModel[12];
        drawerItem[0] = new DataModel(R.drawable.ic_menu_home,"Vcard");
        drawerItem[1] = new DataModel(R.drawable.ic_menu_website,"Website");
        drawerItem[2] = new DataModel(R.drawable.ic_menu_search,"Inquiry");
        //drawerItem[3] = new DataModel(R.drawable.ic_menu_khatabook,"Khata Book");
        //drawerItem[4] = new DataModel(R.drawable.ic_menu_pagarbook,"Pagar Book");
        drawerItem[3] = new DataModel(R.drawable.ic_menu_directory,"Directory");
        drawerItem[4] = new DataModel(R.drawable.ic_menu_website,"WalkIns");
        drawerItem[5] = new DataModel(R.drawable.ic_menu_message,"SMS");
        drawerItem[6] = new DataModel(R.drawable.ic_menu_message,"Space Drive");
        drawerItem[7] = new DataModel(R.drawable.ic_menu_message,"Inquiry Register");
        drawerItem[8] = new DataModel(R.drawable.ic_menu_message,"Payment");
        drawerItem[9] = new DataModel(R.drawable.ic_menu_share,"Share");
        drawerItem[10] = new DataModel(R.drawable.ic_menu_share,"Rating");
        drawerItem[11] = new DataModel(R.drawable.ic_menu_logout,"Logout");

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(MainActivity.this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();
        selectItem(0);

    }

    private void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name ,R.string.app_name);
        mDrawerToggle.syncState();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            view_pager.setVisibility(View.GONE);
            content_frame.setVisibility(View.VISIBLE);
            selectItem(position);
        }
    }

    private void selectItem(int i) {
        Fragment fragment = null;
        switch (i){
            case 0:
            fragment = new FragmentVcard();
                tab_layout.setVisibility(View.VISIBLE);
            break;
            case 1:
                fragment = new WebsiteFragment();
                tab_layout.setVisibility(View.GONE);
                break;
            case 2:
                fragment = new InquiryFragment();
                tab_layout.setVisibility(View.VISIBLE);
                break;
            /*case  3:
                fragment = new KhataBookFragment();
                tab_layout.setVisibility(View.VISIBLE);
                break;
            case  4:
                fragment = new PagarBookFragment();
                tab_layout.setVisibility(View.VISIBLE);
                break;*/

            case 3:
                /*fragment = new ShareFragment();
                tab_layout.setVisibility(View.VISIBLE);*/
                fragment = new DirectoryFragment();
                tab_layout.setVisibility(View.VISIBLE);
                break;
            case 4:
                fragment = new WalkInsFragment();
                tab_layout.setVisibility(View.VISIBLE);
                break;
            case 5:
                fragment = new MessageFragment();
                tab_layout.setVisibility(View.VISIBLE);
                break;
            case 9:
                shareLink();
            break;

            case 10:
                launchMarket();
                break;

            case 11:
                Intent intent = new Intent(getApplicationContext(), Login_user.class);
                startActivity(intent);
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(i, true);
            mDrawerList.setSelection(i);
            setTitle(mNavigationDrawerItemTitles[i]);
            //mDrawerLayout.closeDrawer(mDrawerList);
            mDrawerLayout.closeDrawers();

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private void launchMarket() {
        //Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName());
        Log.e("TEST","Get URI :"+uri);
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    private void shareLink() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "http://symbiz.vctechnology.org/Default";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Symbiz");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        String cName = CommomDataset.companyName;
        Log.e("TEST","CName :"+cName);
        getSupportActionBar().setTitle(mTitle);
        if(mTitle.equals("Website")){
            getSupportActionBar().setTitle(cName);
        }else {
            getSupportActionBar().setTitle(mTitle);
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

}