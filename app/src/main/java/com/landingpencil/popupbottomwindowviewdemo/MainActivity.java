package com.landingpencil.popupbottomwindowviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.landingpencil.popupbottomwindowview.AnimatorListener;
import com.landingpencil.popupbottomwindowview.Popupbottomwindowview;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AnimatorListener {

    private Popupbottomwindowview popupbottomwindowview;
    private View contentView;
    private View bottomView;
    private LinearLayout mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = (LinearLayout) findViewById(R.id.main_view);

        bottomView = LayoutInflater.from(this).inflate(R.layout.layout_bottom_view, null);
        (bottomView.findViewById(R.id.promptly_buy)).setOnClickListener(this);
        (findViewById(R.id.guige)).setOnClickListener(this);
        popupbottomwindowview = (Popupbottomwindowview) findViewById(R.id.bottom_popup);
        popupbottomwindowview.setOnClickListener(this);
        popupbottomwindowview.setBaseViewiew(bottomView);
        contentView = LayoutInflater.from(this).inflate(R.layout.layout_content_view, null);
        popupbottomwindowview.setContentView(contentView);
        (contentView.findViewById(R.id.ic_cancel)).setOnClickListener(this);
        popupbottomwindowview.setAnimatorListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.promptly_buy:
            case R.id.ic_cancel:
                popupbottomwindowview.disMissPopupView();
                break;
            case R.id.guige:
                popupbottomwindowview.showPopupView();
                break;
        }
    }

    @Override
    public void startValue(int value) {
        setMargins(mainView, value - 10, value, value - 10, value);
    }

    @Override
    public void endValue(int value) {
        setMargins(mainView, value, value, value, value);
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


}
