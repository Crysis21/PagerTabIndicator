package com.hold1.pagertabsdemo.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hold1.pagertabsdemo.MainActivity;
import com.hold1.pagertabsdemo.R;
import com.hold1.pagertabsindicator.PagerTabsIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */

public class ContactFragment extends Fragment implements FragmentPresenter{

    private PagerTabsIndicator tabsIndicator;

//    @BindView(R.id.markdown_view)
//    MarkdownView markdownView;

    @BindView(R.id.open_git)
    Button github;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabsIndicator = ((MainActivity)getActivity()).getTabsIndicator();
        if (tabsIndicator==null) return;
        ButterKnife.bind(this, view);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/Crysis21/PagerTabIndicator"));
                startActivity(intent);
            }
        });
    }

    @Override
    public String getTabName() {
        return "Contact";
    }

    @Override
    public int getTabImage() {
        return R.drawable.ic_contact;
    }

    @Override
    public String getTabImageUrl() {
        return "https://s3-us-west-2.amazonaws.com/anaface-pictures/ic_contact.png";
    }
}
