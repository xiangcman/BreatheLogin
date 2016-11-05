package com.single.breathelogin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.single.breathelogin.breath.BreathingViewHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    BreathingViewHelper breathingFirst = null;
    BreathingViewHelper breathingLast = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText first_name_edit_input = (EditText) findViewById(R.id.first_name_edit_input);
        final EditText last_name_edit_input = (EditText) findViewById(R.id.last_name_edit_input);
        final View first_name_view = findViewById(R.id.first_name_card_v);
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = first_name_edit_input.getText().toString();
                String str2 = last_name_edit_input.getText().toString();
                if (TextUtils.isEmpty(str1)) {
                    if (breathingFirst == null) {
                        breathingFirst = new BreathingViewHelper(first_name_edit_input, Color.parseColor("#FF0099"), "please input your first name", Color.parseColor("#3300FF"), true, first_name_view, MainActivity.this);
                    }
                    if (breathingFirst.isCancelled()) {
                        breathingFirst.setBreathingBackgroundColor();
                    }
                }
                if (TextUtils.isEmpty(str2)) {
                    if (breathingLast == null) {
                        breathingLast = new BreathingViewHelper(last_name_edit_input, Color.parseColor("#FF0099"), "please input your last name", Color.parseColor("#3300FF"), MainActivity.this);
                    }
                    if (breathingLast.isCancelled()) {
                        breathingLast.setBreathingBackgroundColor();
                    }
                }
            }
        });
        first_name_edit_input.setOnTouchListener(new View.OnTouchListener() {
            //如果ontouch事件返回true的话，表示自己消费了
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (breathingFirst != null && !breathingFirst.isCancelled()) {
                    Log.d(TAG, "breathingFirst cancel");
                    breathingFirst.setCancel();
                }
                return false;
            }
        });
        last_name_edit_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (breathingLast != null && !breathingLast.isCancelled()) {
                    Log.d(TAG, "breathingLast cancel");
                    breathingLast.setCancel();
                }
                return false;
            }
        });
    }

}
