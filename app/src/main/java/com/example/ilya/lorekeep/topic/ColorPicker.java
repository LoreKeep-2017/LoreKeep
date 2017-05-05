package com.example.ilya.lorekeep.topic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ilya.lorekeep.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;

public class ColorPicker extends AppCompatActivity {
    public static final String EXTRA_ANSWER_COLOR =
            "com.bignerdranch.android.answer_color";

    private ColorPickerView mColorPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);


        ColorPickerView colorPickerView = (ColorPickerView) findViewById(R.id.color_picker_view);
        colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                // Handle on color change
                Log.d("ColorPicker", "onColorChanged: 0x" + Integer.toHexString(selectedColor));
                Intent data = new Intent();
                data.putExtra(EXTRA_ANSWER_COLOR, selectedColor);
                setResult(RESULT_OK, data);
            }
        });
        colorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                Toast.makeText(
                        ColorPicker.this,
                        "selectedColor: " + Integer.toHexString(selectedColor).toUpperCase(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static int getColor(Intent result) {
        return result.getIntExtra(EXTRA_ANSWER_COLOR, 0xff15170d);
    }
}
