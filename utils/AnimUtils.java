package com.sjs.sjsapp.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;

/**
 * Created by xeno on 2016/5/20.
 */
public class AnimUtils {

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static void animChangeViewHeight(final View target, final int start, final int end, final int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {

                target.getLayoutParams().height = (Integer) animator.getAnimatedValue();
                target.requestLayout();
            }
        });

        valueAnimator.setDuration(duration).start();
    }

}
