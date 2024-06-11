package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    ViewPager mSLideViewPager;
    LinearLayout mDotLayout;
    Button backbtn, nextbtn, skipbtn;
    TextView[] dots;
    ViewPageAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mSLideViewPager = findViewById(R.id.slideViewPager);
            mDotLayout = findViewById(R.id.indicator_layout);
            backbtn = findViewById(R.id.backbtn);
            nextbtn = findViewById(R.id.nextbtn);
            skipbtn = findViewById(R.id.skipButton);

            if (mSLideViewPager == null) Log.e(TAG, "ViewPager is null");
            if (mDotLayout == null) Log.e(TAG, "DotLayout is null");
            if (backbtn == null) Log.e(TAG, "Back Button is null");
            if (nextbtn == null) Log.e(TAG, "Next Button is null");
            if (skipbtn == null) Log.e(TAG, "Skip Button is null");

            viewPagerAdapter = new ViewPageAdapter(this);
            mSLideViewPager.setAdapter(viewPagerAdapter);

            setUpIndicator(0);
            mSLideViewPager.addOnPageChangeListener(viewListener);

            backbtn.setOnClickListener(v -> {
                if (getItem(0) > 0) {
                    mSLideViewPager.setCurrentItem(getItem(-1), true);
                }
            });

            nextbtn.setOnClickListener(v -> {
                if (getItem(0) < 3) {
                    mSLideViewPager.setCurrentItem(getItem(1), true);
                } else {
                    Intent i = new Intent(MainActivity.this, Login.class);
                    startActivity(i);
                    finish();
                }
            });

            skipbtn.setOnClickListener(v -> {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                finish();
            });
        } catch (Exception e) {
            Log.e(TAG, "Error during initialization", e);
        }
    }

    public void setUpIndicator(int position) {
        dots = new TextView[4];
        mDotLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive, getApplicationContext().getTheme()));
            mDotLayout.addView(dots[i]);
        }

        if (dots.length > 0 && position < dots.length) {
            dots[position].setTextColor(getResources().getColor(R.color.active, getApplicationContext().getTheme()));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            setUpIndicator(position);

            if (position > 0) {
                backbtn.setVisibility(View.VISIBLE);
            } else {
                backbtn.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    private int getItem(int i) {
        return mSLideViewPager.getCurrentItem() + i;
    }
}
