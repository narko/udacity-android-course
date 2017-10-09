package com.example.xyzreader.ui;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.xyzreader.R;

/**
 * Created by narko on 09/10/17.
 */

public class SlowPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();

        if (position < -1) {
            // page off the screen on the left side
            page.setAlpha(1);
        } else if (position > 1) {
            // page off the screen on the right side
            page.setAlpha(1);
        } else if (position <= 1) {
            View appBar = page.findViewById(R.id.app_bar);
            appBar.setTranslationX(-position * (pageWidth / 2));
        }
    }
}
