package com.syrovama.dragndraw;

import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private DrawBox mCustomView;
    private TextView mXYTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mXYTextView = findViewById(R.id.xyTextView);
        mCustomView = findViewById(R.id.drawbox);
        DrawBox.Callback listener = new DrawBox.Callback() {
            @Override
            public void onPositionChanged(PointF center) {
                mXYTextView.setText(String.format(getString(R.string.coordinate_text), center.x, center.y));
            }
        };
        mCustomView.setCallback(listener);
    }
}
