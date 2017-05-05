package com.example.ilya.lorekeep.topic.draw_and_colorpicker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.topic.image_flickr.CapturePhotoUtils;


public class DrawActivity extends AppCompatActivity {

    private DrawPicture mDrawPicture;
    private Button mGetDraw;
    private RelativeLayout parent;
    public static final String EXTRA_ANSWER_DRAW =
            "com.bignerdranch.android.answer_draw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_topic);

        parent = (RelativeLayout) findViewById(R.id.sign_image_parent);
        mDrawPicture = new DrawPicture(this);
        parent.addView(mDrawPicture);

        mGetDraw = (Button) findViewById(R.id.button_draw_picture);
        mGetDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.setDrawingCacheEnabled(true);
                Bitmap bitmap = parent.getDrawingCache();
                BitmapDrawable bdrawable = new BitmapDrawable(getResources(), bitmap);
                String targetUri = CapturePhotoUtils.insertImage(getContentResolver(), bitmap, "hell", "hell");

                Intent data = new Intent();
                data.putExtra(EXTRA_ANSWER_DRAW, targetUri);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    public static String getImage(Intent result) {
        return result.getStringExtra(EXTRA_ANSWER_DRAW);
    }

}
