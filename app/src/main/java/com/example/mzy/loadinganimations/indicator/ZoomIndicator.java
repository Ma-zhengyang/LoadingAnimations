package com.example.mzy.loadinganimations.indicator;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by mazhengyang on 18-9-4.
 */

public class ZoomIndicator extends IndicatorDrawable {

    private final String TAG = ZoomIndicator.class.getSimpleName();

    private final int count = 4;

    private float[] mAnimatedValue = new float[]{
            1.0f, 1.0f, 1.0f, 1.0f
    };

    @Override
    protected ArrayList<ValueAnimator> initAnimation() {

        int[] delay = new int[]{100, 200, 300, 400};

        ArrayList<ValueAnimator> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            final int index = i;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.3f, 1.0f);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mAnimatedValue[index] = ((float) valueAnimator.getAnimatedValue());
                    invalidateSelf();
                }
            });

            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setDuration(1000);
            valueAnimator.setStartDelay(delay[i]);
            list.add(valueAnimator);
        }

        return list;
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {

        float radius = getWidth() / 20;
        float space = 5;

        float x = getWidth() / 2;
        float y = getHeight() / 2;

        float ball_space = (radius * 2) * count + space * (count - 1);

        for (int i = 0; i < 4; i++) {
            canvas.save();
            float translateX = (getWidth() - ball_space) / 2 + radius * ((i + 1) * 2 - 1) + space * i;
            canvas.translate(translateX, y);
            canvas.scale(mAnimatedValue[i], mAnimatedValue[i]);
            canvas.drawCircle(0, 0, radius, paint);
            canvas.restore();
        }

    }

}

