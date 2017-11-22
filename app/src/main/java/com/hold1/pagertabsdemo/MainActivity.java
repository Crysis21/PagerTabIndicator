package com.hold1.pagertabsdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hold1.pagertabsdemo.fragments.AdaptersFragment;
import com.hold1.pagertabsdemo.fragments.ColorsFragment;
import com.hold1.pagertabsdemo.fragments.ContactFragment;
import com.hold1.pagertabsdemo.fragments.DemoFragment;
import com.hold1.pagertabsdemo.fragments.DividerFragment;
import com.hold1.pagertabsdemo.fragments.FragmentPresenter;
import com.hold1.pagertabsdemo.fragments.TabsFragment;
import com.hold1.pagertabsindicator.PagerTabsIndicator;
import com.hold1.pagertabsindicator.TabViewProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerTabsIndicator tabsIndicator;
    private PagerAdapter viewImageAdapter;
    private PagerAdapter webImageAdapter;
    private PagerAdapter viewCustomAdapter;
    private PagerAdapter viewCustomAnimAdapter;
    private PagerAdapter viewTextAdapter;

    private List<Fragment> demoFragments = new ArrayList<>();

    public enum TabAdapterType {
        TEXT,
        IMAGE,
        WEB,
        CUSTOM,
        CUSTOM_ANIM
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        tabsIndicator = findViewById(R.id.tabs_indicator);

        demoFragments.add(new TabsFragment());
        demoFragments.add(new DividerFragment());
        demoFragments.add(new ColorsFragment());
        demoFragments.add(new AdaptersFragment());
        demoFragments.add(new ContactFragment());

        viewTextAdapter = new TextAdapter(getSupportFragmentManager());
        viewImageAdapter = new ImageAdapter(getSupportFragmentManager());
        webImageAdapter = new WebImageAdapter(getSupportFragmentManager());
        viewCustomAdapter = new CustomAdapter(getSupportFragmentManager());
        viewCustomAnimAdapter = new CustomAnimAdapter(getSupportFragmentManager());

        viewPager.setAdapter(viewCustomAdapter);
        tabsIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.text:
                changeTabAdapter(TabAdapterType.TEXT);
                break;
            case R.id.image:
                changeTabAdapter(TabAdapterType.IMAGE);
                break;
            case R.id.web:
                changeTabAdapter(TabAdapterType.WEB);
                break;
            case R.id.custom:
                changeTabAdapter(TabAdapterType.CUSTOM);
                break;
            case R.id.custom_anim:
                changeTabAdapter(TabAdapterType.CUSTOM_ANIM);
                break;
        }
        return true;
    }

    public void changeTabAdapter(TabAdapterType adapterType) {
        viewPager.removeAllViews();
        switch (adapterType) {
            case TEXT:
                viewPager.setAdapter(viewTextAdapter);
                break;
            case IMAGE:
                viewPager.setAdapter(viewImageAdapter);
                break;
            case WEB:
                viewPager.setAdapter(webImageAdapter);
                break;
            case CUSTOM:
                viewPager.setAdapter(viewCustomAdapter);
                break;
            case CUSTOM_ANIM:
                viewPager.setAdapter(viewCustomAnimAdapter);
                tabsIndicator.setShowDivider(false)
                        .setIndicatorBgHeight(0)
                        .setShowBarIndicator(false)
                        .setHeight(getResources().getDimensionPixelSize(R.dimen.tab_height_min))
                        .refresh();
                break;
        }
    }

    class ImageAdapter extends FragmentPagerAdapter implements TabViewProvider.ImageProvider {

        public ImageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return demoFragments.get(position);
        }

        @Override
        public int getCount() {
            return demoFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Tab " + position;
        }

        @Override
        public Uri getImageUri(int position) {
            return null;
        }

        @Override
        public int getImageResourceId(int position) {
            Fragment fragment = demoFragments.get(position);
            if (fragment instanceof FragmentPresenter) {
                return ((FragmentPresenter) fragment).getTabImage();
            }
            return R.drawable.ic_add_shopping_cart_black_24dp;
        }
    }


    class WebImageAdapter extends FragmentPagerAdapter implements TabViewProvider.ImageProvider {

        public WebImageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return demoFragments.get(position);
        }

        @Override
        public int getCount() {
            return demoFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Tab " + position;
        }

        @Override
        public Uri getImageUri(int position) {
            Fragment fragment = demoFragments.get(position);
            if (fragment instanceof FragmentPresenter) {
                return Uri.parse(((FragmentPresenter) fragment).getTabImageUrl());
            }
            return Uri.parse("https://icons8.github.io/flat-color-icons/svg/opened_folder.svg");
        }

        @Override
        public int getImageResourceId(int position) {
            Fragment fragment = demoFragments.get(position);
            if (fragment instanceof FragmentPresenter) {
                return ((FragmentPresenter) fragment).getTabImage();
            }
            return R.drawable.ic_add_shopping_cart_black_24dp;
        }
    }

    class TextAdapter extends FragmentPagerAdapter {

        public TextAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return demoFragments.get(position);
        }

        @Override
        public int getCount() {
            return demoFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Fragment fragment = demoFragments.get(position);
            if (fragment instanceof FragmentPresenter) {
                return ((FragmentPresenter) fragment).getTabName();
            }
            return "Tab " + position;
        }
    }


    class CustomAdapter extends FragmentPagerAdapter implements TabViewProvider.CustomView {

        public CustomAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return demoFragments.get(position);
        }

        @Override
        public int getCount() {
            return demoFragments.size();
        }

        @Override
        public View getView(int position) {
            Fragment fragment = demoFragments.get(position);
            if (fragment instanceof FragmentPresenter) {
                View view = getLayoutInflater().inflate(R.layout.tab_item, null);
                TextView title = view.findViewById(R.id.tab_name);
                title.setText(((FragmentPresenter) fragment).getTabName());
                ImageView icon = view.findViewById(R.id.tab_icon);
                icon.setImageResource(((FragmentPresenter) fragment).getTabImage());
                return view;
            }
            return null;
        }
    }


    class CustomAnimAdapter extends FragmentPagerAdapter implements TabViewProvider.CustomView {

        public CustomAnimAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return demoFragments.get(position);
        }

        @Override
        public int getCount() {
            return demoFragments.size();
        }

        @Override
        public View getView(int position) {
            Fragment fragment = demoFragments.get(position);
            if (fragment instanceof FragmentPresenter) {
                return new TabItemView(getApplicationContext(), ((FragmentPresenter) fragment).getTabName(), ((FragmentPresenter) fragment).getTabImage(), 0xFF363636, 0xFFFF0000);
            }
            return null;
        }
    }

    //Just for easing the demo :)
    public PagerTabsIndicator getTabsIndicator() {
        return tabsIndicator;
    }

    public void addDummyTab() {
        demoFragments.add(new DemoFragment());
        viewPager.getAdapter().notifyDataSetChanged();
    }
}
