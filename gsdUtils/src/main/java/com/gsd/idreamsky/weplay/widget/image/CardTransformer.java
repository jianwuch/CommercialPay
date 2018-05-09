package com.gsd.idreamsky.weplay.widget.image;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Awesome Launcher-inspired page transformer
 */
public class CardTransformer implements ViewPager.PageTransformer {
    private final float scaleAmount;

    public CardTransformer(float scalingStart) {
        scaleAmount = 1 - scalingStart;
    }

    @Override
    public void transformPage(View page, float position) {
        if (position >= 0f) {
            final int w = page.getWidth();
            float scaleFactor = 1 - scaleAmount * position;
            page.setAlpha(1f - position);
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setTranslationX(w * (1 - position) - w);
        }
    }
}