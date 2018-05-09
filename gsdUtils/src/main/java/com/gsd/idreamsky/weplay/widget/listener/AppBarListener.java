package com.gsd.idreamsky.weplay.widget.listener;

import android.support.design.widget.AppBarLayout;

/**
 * Created by magical.zhang on 2018/1/24.
 * Description :
 */

public abstract class AppBarListener implements AppBarLayout.OnOffsetChangedListener {

    int totalRange;
    boolean isExpand;
    float changeFactor;

    public AppBarListener(float changeFactor) {
        this.changeFactor = changeFactor;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (totalRange == 0) {
            totalRange = appBarLayout.getTotalScrollRange();
        }

        int offset = Math.abs(verticalOffset);
        if (offset < totalRange / getChangeFactor()) {
            //伸张态
            if (isExpand) {
                onExpandIcon();
                isExpand = false;
            }
        } else {
            //折叠态
            if (!isExpand) {
                onCollapIcon();
                isExpand = true;
            }
        }
    }

    public float getChangeFactor() {
        if (changeFactor == 0) {
            changeFactor = 2;
        }
        return changeFactor;
    }

    public abstract void onExpandIcon();

    public abstract void onCollapIcon();
}
