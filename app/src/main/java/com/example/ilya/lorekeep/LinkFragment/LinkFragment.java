package com.example.ilya.lorekeep.LinkFragment;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ilya.lorekeep.R;

/**
 * Created by ilya on 3/14/17.
 */

public class LinkFragment extends Fragment {

    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    private int titleId = 10;
    private int contentId = 11;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //
        // return inflater.inflate(R.layout.link_layout,container, false);
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
