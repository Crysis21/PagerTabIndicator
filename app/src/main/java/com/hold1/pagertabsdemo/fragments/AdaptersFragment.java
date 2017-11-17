package com.hold1.pagertabsdemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hold1.pagertabsdemo.MainActivity;
import com.hold1.pagertabsdemo.R;
import com.hold1.pagertabsindicator.PagerTabsIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */

public class AdaptersFragment extends Fragment implements FragmentPresenter {

    private PagerTabsIndicator tabsIndicator;

    @BindView(R.id.tabs_text)
    View tabsText;

    @BindView(R.id.tabs_image)
    View tabsImage;

    @BindView(R.id.tabs_web)
    View tabsWeb;

    @BindView(R.id.tabs_custom)
    View tabsCustom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adapters_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabsIndicator = ((MainActivity) getActivity()).getTabsIndicator();
        if (tabsIndicator == null) return;
        ButterKnife.bind(this, view);
        tabsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeTabAdapter(MainActivity.TabAdapterType.TEXT);
            }
        });
        tabsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeTabAdapter(MainActivity.TabAdapterType.IMAGE);
            }
        });
        tabsWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeTabAdapter(MainActivity.TabAdapterType.WEB);
            }
        });
        tabsCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeTabAdapter(MainActivity.TabAdapterType.CUSTOM);
            }
        });
    }

    @Override
    public String getTabName() {
        return "Adapters";
    }

    @Override
    public int getTabImage() {
        return R.drawable.ic_adapter;
    }

    @Override
    public String getTabImageUrl() {
        return "https://s3-us-west-2.amazonaws.com/anaface-pictures/ic_adapter.png";
    }
}
