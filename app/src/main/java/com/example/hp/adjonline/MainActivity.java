package com.example.hp.adjonline;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public ViewPagerAdapter adapter;
    NavigationView navigationView;
    Toolbar toolbar;
    private DrawerLayout drawer;
    public TextView mTitle;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_action_home,
            R.drawable.ic_action_search,
            R.drawable.ic_action_person,
            R.drawable.ic_action_restaurant_menu,
            // R.drawable.icons8_shutdown_24
    };
    private static int navItemIndex=0;
//    private void setupTabIcons() {
//        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
//        //  tabLayout.getTabAt(4).setIcon(tabIcons[4]);
//    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragment1(), "Home");
        adapter.addFragment(new fragment2(), "Search");
        adapter.addFragment(new fragment3(), "Contact us");
        adapter.addFragment(new fragment1(), "Menu");
        viewPager.setAdapter(adapter);
    }
        @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences =getSharedPreferences(LoginActivity.SharedPreferenceTag,MODE_PRIVATE);
        boolean bool=sharedPreferences.getBoolean(LoginActivity.SP_Status_TAG,false);
        if(!bool){
            Intent i=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
       }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        Intent i=new Intent(MainActivity.this,AdvanceSearch.class);
        startActivity(i);





        SearchQuery searchQuery=new SearchQuery();
        ArrayList<Integer> jsonArray=searchQuery.pagenosearch("ADJ",2008,1);
        //searchQuery.DateSearch(2008,7,10,MainActivity.this);
        Log.e("MAINACT",jsonArray.toString());




        drawer=findViewById(R.id.drawer_layout);
        tabLayout= findViewById(R.id.tabs);
        viewPager =findViewById(R.id.viewpager);
        navigationView = findViewById(R.id.nav_view);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);
        //setupTabIcons();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Allahabad Daily Judgments");
        mTitle.setTextAppearance(R.style.nimbusromno9lreg);


        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.adjlogonew);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //  getSupportActionBar().setDisplayShowHomeEnabled(false);
        //   getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        setUpNavigationView();
    }


    class ViewPagerAdapter extends FragmentPagerAdapter

    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
//                        CURRENT_TAG = TAG_HOME;
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawers();
                            viewPager.setCurrentItem(0);
                        }
                        break;
                    /*case R.id.nav_notifications:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;*/
                    case R.id.nav_settings:
                        navItemIndex = 2;
                        //CURRENT_TAG = TAG_SETTINGS;
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_log_out :
                        Toast.makeText(getApplicationContext(), "User logged out", Toast.LENGTH_LONG).show();
                       Intent intent3 = new Intent(MainActivity.this, LoginActivity.class);
                       intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent3);
                        return true;


                    case R.id.nav_contact_us :
                        viewPager.setCurrentItem(2);
                        // Intent intent2 = new Intent(MainActivity.this, PopSearch.class);
                        // startActivity(intent2);
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawers();
                        }
                        return true;

                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                //loadHomeFragment();

                return true;
            }
        });

    }}