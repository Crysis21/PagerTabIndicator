package com.hold1.pagertabsdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
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

        PagerAdapter viewPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new DemoFragment();
            }

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "Tab " + position;
            }
        };

        viewPager.setAdapter(viewPagerAdapter);
        tabsIndicator.setViewPager(viewPager);
    }


}
