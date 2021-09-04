package com.hold1.pagertabsdemo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flask.colorpicker.ColorPickerView;
import com.google.android.flexbox.FlexboxLayout;
import com.hold1.pagertabsdemo.MainActivity;
import com.hold1.pagertabsdemo.R;
import com.hold1.pagertabsindicator.PagerTabsIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */

public class ColorsFragment extends Fragment implements FragmentPresenter {

    private PagerTabsIndicator tabsIndicator;

    @BindView(R.id.color_picker_view)
    ColorPickerView colorPicker;

    @BindView(R.id.elements)
    FlexboxLayout elements;

    View.OnClickListener colorClick;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.colors_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabsIndicator = ((MainActivity) getActivity()).getTabsIndicator();
        if (tabsIndicator == null) return;
        ButterKnife.bind(this, view);

        colorClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.color_text:
                        tabsIndicator.setTextColor(colorPicker.getSelectedColor());
                        break;
                    case R.id.color_ind_bg:
                        tabsIndicator.setIndicatorBgColor(colorPicker.getSelectedColor());
                        break;
                    case R.id.color_ind:
                        tabsIndicator.setIndicatorColor(colorPicker.getSelectedColor());
                        break;
                    case R.id.color_div:
                        tabsIndicator.setDividerColor(colorPicker.getSelectedColor());
                        break;
                    case R.id.color_bg:
                        tabsIndicator.setBackgroundColor(colorPicker.getSelectedColor());
                        break;
                    case R.id.highlight_color:
                        tabsIndicator.setHighlightTextColor(colorPicker.getSelectedColor());
                        break;
                }
                tabsIndicator.refresh();
            }
        };

        for (int i = 0; i < elements.getChildCount(); i++)
            elements.getChildAt(i).setOnClickListener(colorClick);
    }

    @Override
    public String getTabName() {
        return "Colors";
    }

    @Override
    public int getTabImage() {
        return R.drawable.ic_colors;
    }

    @Override
    public String getTabImageUrl() {
        return "https://s3-us-west-2.amazonaws.com/anaface-pictures/ic_colors.png";
    }
}
