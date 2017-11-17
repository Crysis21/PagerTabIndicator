package com.hold1.pagertabsdemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.hold1.pagertabsdemo.MainActivity;
import com.hold1.pagertabsdemo.R;
import com.hold1.pagertabsindicator.PagerTabsIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */

public class DividerFragment extends Fragment implements FragmentPresenter{


    private PagerTabsIndicator tabsIndicator;

    @BindView(R.id.div_width)
    SeekBar divWidth;

    @BindView(R.id.div_margin)
    SeekBar divMargin;

    @BindView(R.id.show_divider)
    CheckBox showDivider;

    @BindView(R.id.solid_color)
    Button solidColor;

    @BindView(R.id.divider_1)
    Button divider1;

    @BindView(R.id.divider_2)
    Button divider2;

    @BindView(R.id.divider_3)
    Button divider3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.divider_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabsIndicator = ((MainActivity)getActivity()).getTabsIndicator();
        if (tabsIndicator==null) return;
        ButterKnife.bind(this, view);
        divWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tabsIndicator.setDividerWidth(progress).refresh();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        divMargin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tabsIndicator.setDividerMargin(progress).refresh();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        showDivider.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tabsIndicator.setShowDivider(isChecked).refresh();
            }
        });
        solidColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabsIndicator.setDividerResource(-1).setDividerWidth(1).refresh();
            }
        });

        divider1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabsIndicator.setDividerResource(R.drawable.ic_divider1).setDividerWidth(20).setDividerMargin(40).refresh();
            }
        });

        divider2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabsIndicator.setDividerResource(R.drawable.ic_divider2).setDividerWidth(18).setDividerMargin(30).refresh();
            }
        });

        divider3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabsIndicator.setDividerResource(R.drawable.ic_divider3).setDividerWidth(40).setDividerMargin(36).refresh();
            }
        });

    }

    @Override
    public String getTabName() {
        return "Divider";
    }

    @Override
    public int getTabImage() {
        return R.drawable.ic_divider;
    }

    @Override
    public String getTabImageUrl() {
        return "https://s3-us-west-2.amazonaws.com/anaface-pictures/ic_divider.png";
    }
}
