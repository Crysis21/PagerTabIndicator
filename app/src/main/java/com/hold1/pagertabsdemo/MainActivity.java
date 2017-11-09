package com.hold1.pagertabsdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hold1.pagertabsindicator.PagerTabsIndicator;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerTabsIndicator tabsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        tabsIndicator = findViewById(R.id.tabs_indicator);

        PagerAdapter viewPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(viewPagerAdapter);
        tabsIndicator.setViewPager(viewPager);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new DemoFragment();
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Tab " + position;
        }

//        @Override
//        public Uri getImageUri(int position) {
//            Uri uri = Uri.parse("https://image.flaticon.com/teams/slug/freepik.jpg");
//            return uri;
//        }
    }
}
