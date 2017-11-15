package com.hold1.pagertabsdemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.hold1.pagertabsdemo.MainActivity;
import com.hold1.pagertabsdemo.R;
import com.hold1.pagertabsindicator.PagerTabsIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristian Holdunu on 09/11/2017.
 */

public class IndicatorFragment extends Fragment {

    PagerTabsIndicator tabsIndicator;

    @BindView(R.id.indicator_height)
    SeekBar indicatorHeight;


    @BindView(R.id.indicator_bg_height)
    SeekBar indicatorBgHeight;

    @BindView(R.id.indicator_margin)
    SeekBar indicatorMargin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabsIndicator = ((MainActivity)getActivity()).getTabsIndicator();
        if (tabsIndicator==null) return;
        ButterKnife.bind(this, view);

        indicatorHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tabsIndicator.setIndicatorHeight(progress).refresh();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        indicatorBgHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tabsIndicator.setIndicatorBgHeight(progress).refresh();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        indicatorMargin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tabsIndicator.setIndicatorMargin(progress).refresh();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
