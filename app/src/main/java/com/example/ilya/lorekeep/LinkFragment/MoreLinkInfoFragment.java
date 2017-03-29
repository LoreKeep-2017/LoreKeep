package com.example.ilya.lorekeep.LinkFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ilya on 3/27/17.
 */

public class MoreLinkInfoFragment extends Fragment {

    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    public static MoreLinkInfoFragment newInstance() {

        Bundle args = new Bundle();

        MoreLinkInfoFragment fragment = new MoreLinkInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setBackgroundColor(Color.GRAY);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(getContext());
        title.setLayoutParams(params);

        TextView content = new TextView(getContext());
        content.setLayoutParams(params);

        params.setMargins(20,20,20,20);
        layout.setLayoutParams(params);

        title.setText("OLOLOLOL Title OLololol0");
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        content.setText("OLOLOLLOLO Content OLOLOLOLOL");
        content.setGravity(Gravity.CENTER);
        content.setTextColor(Color.WHITE);
        layout.addView(content);

        return layout;

    }
}
