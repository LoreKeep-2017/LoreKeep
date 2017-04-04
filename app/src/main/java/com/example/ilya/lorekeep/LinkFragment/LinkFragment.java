package com.example.ilya.lorekeep.LinkFragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ilya.lorekeep.DAO.LinkInfo;
import com.example.ilya.lorekeep.InfoActivity;
import com.example.ilya.lorekeep.LinksActivity;

public class LinkFragment {

    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    private LinearLayout layout;

    @Nullable
    public View CreateView(ViewGroup parent, LinkInfo linkInfo) {

        layout = new LinearLayout(parent.getContext());
        layout.setBackgroundColor(Color.GRAY);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(parent.getContext());
        title.setLayoutParams(params);

        if(!linkInfo.getLinkDescription().isEmpty()) {
            TextView content = new TextView(parent.getContext());
            content.setLayoutParams(params);
            content.setText(linkInfo.getLinkDescription());
            content.setGravity(Gravity.CENTER);
            content.setTextColor(Color.WHITE);
            layout.addView(content);
        }

        TextView link = new TextView(parent.getContext());
        link.setLayoutParams(params);

        params.setMargins(20,20,20,20);
        layout.setLayoutParams(params);

        title.setText(linkInfo.getLinkTitle());
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        link.setText(linkInfo.getLink());
        link.setGravity(Gravity.CENTER);
        layout.addView(link);

        return layout;
    }

    private int getChildNumber(){
        return ((ViewGroup)layout.getParent()).getChildCount();
    }
}
