package com.hold1.pagertabsdemo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hold1.pagertabsdemo.MainActivity;
import com.hold1.pagertabsdemo.R;
import com.hold1.pagertabsindicator.PagerTabsIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristian Holdunu on 09/11/2017.
 */

public class TabsFragment extends Fragment implements FragmentPresenter {

    PagerTabsIndicator tabsIndicator;


    @BindView(R.id.text_size)
    SeekBar textSize;

    @BindView((R.id.tab_padding))
    SeekBar tabPadding;

    @BindView(R.id.tab_elevation)
    SeekBar tabElevation;

    @BindView(R.id.indicator_height)
    SeekBar indicatorHeight;

    @BindView(R.id.indicator_bg_height)
    SeekBar indicatorBgHeight;

    @BindView(R.id.indicator_margin)
    SeekBar indicatorMargin;

    @BindView(R.id.lock_expanded)
    CheckBox lockExpanded;

    @BindView(R.id.indicator_type)
    RadioGroup indicatorType;

    @BindView(R.id.add_dummy)
    Button addDummy;

    @BindView(R.id.show_bar_indicator)
    CheckBox showBarIndicator;

    @BindView(R.id.highlight_text)
    CheckBox highlightText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabs_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabsIndicator = ((MainActivity) getActivity()).getTabsIndicator();
        if (tabsIndicator == null) return;
        ButterKnife.bind(this, view);

        textSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tabsIndicator.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tabPadding.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tabsIndicator.setTabPadding(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tabElevation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tabsIndicator.setTabElevation(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        indicatorHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tabsIndicator.setIndicatorHeight(progress);
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
                tabsIndicator.setIndicatorBgHeight(progress);
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
                tabsIndicator.setIndicatorMargin(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        lockExpanded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tabsIndicator.setLockExpanded(isChecked);
            }
        });

        indicatorType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.bar_top) {
                    tabsIndicator.setIndicatorType(PagerTabsIndicator.TAB_INDICATOR_TOP);
                } else {
                    tabsIndicator.setIndicatorType(PagerTabsIndicator.TAB_INDICATOR_BOTTOM);
                }
            }
        });
        addDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).addDummyTab();
            }
        });

        showBarIndicator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tabsIndicator.setShowBarIndicator(isChecked);
            }
        });

        highlightText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tabsIndicator.setHighlightText(true);
                tabsIndicator.setHighlightTextColor(Color.RED);
            }
        });
    }

    @Override
    public String getTabName() {
        return "Tabs";
    }

    @Override
    public int getTabImage() {
        return R.drawable.ic_tabs;
    }

    @Override
    public String getTabImageUrl() {
        return "https://s3-us-west-2.amazonaws.com/anaface-pictures/ic_tab.png";
    }
}
