package com.example.ilya.lorekeep.topic.image_flickr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ilya.lorekeep.R;

import static android.app.Activity.RESULT_OK;


public class SearchFragment extends Fragment {

    public static final String EXTRA_REQUEST =
            "extra_request";

    private EditText mSearchImage;
    private String request;

    public static Fragment newInstance() {
        Fragment f = new SearchFragment();
        Bundle args = new Bundle();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.search_fragment, container, false);


        mSearchImage = (EditText) v.findViewById(R.id.flickr_search_image);
        mSearchImage.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (!event.isShiftPressed()) {
                        sendResult(RESULT_OK, v.getText().toString());
                        closefragment();
                        return true; // consume.
                    }
                }
                return false; // pass on to other listeners.
            }
        });

        return v;
    }

    private void closefragment() {
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.loading_text_fragment_search);
        getActivity().getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
    }


    private void sendResult(int resultCode, String imageText) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_REQUEST, imageText);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }


}
