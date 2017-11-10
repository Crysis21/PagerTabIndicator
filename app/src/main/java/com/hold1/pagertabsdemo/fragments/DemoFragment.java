package com.hold1.pagertabsdemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lzyzsd.randomcolor.RandomColor;
import com.hold1.pagertabsdemo.R;

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */

public class DemoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.demo_fragment, null);
        RandomColor randomColor = new RandomColor();
        int color = randomColor.randomColor();
        view.setBackgroundColor(color);
        return view;
    }
}
