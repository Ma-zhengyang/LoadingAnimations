package com.example.mzy.indicators.Circle;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.example.mzy.indicators.IndicatorDrawable;

import java.util.ArrayList;

/**
 * Created by mazhengyang on 18-10-24.
 */

public class CircleWaveIndicator extends IndicatorDrawable {

    private final String TAG = CircleWaveIndicator.class.getSimpleName();

    private final int mCount = 3;
    private float space;//相邻两圆间距
    private float radius;//圆的半径
    private float leftPadding;

    private float[] mAnimatedValue = new float[]{
            0.0f, 0.0f, 0.0f
    };

    public CircleWaveIndicator(Context context, int indicatorColor, int indicatorSpeed) {
        Log.d(TAG, "CircleWaveIndicator: ");
        this.indicatorColor = indicatorColor;
        this.indicatorSpeed = indicatorSpeed;
        if (indicatorSpeed <= 0) {
            this.indicatorSpeed = 1000;
        }

        init(context);
    }

    @Override
    protected void init(Context context) {
        Log.d(TAG, "init: ");
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(dip2px(context, 1.0f));
        mPaint.setColor(indicatorColor);

        space = dip2px(context, 2.0f);
    }

    @Override
    protected ArrayList<Animator> getAnimation() {
        int[] delay = new int[]{100, 200, 300};

        ArrayList<Animator> list = new ArrayList<>();

        for (int i = 0; i < mCount; i++) {
            final int index = i;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f, 0.0f);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mAnimatedValue[index] = ((float) valueAnimator.getAnimatedValue());
                    invalidateSelf();
                }
            });

            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setDuration(indicatorSpeed);
            valueAnimator.setStartDelay(delay[i]);
            list.add(valueAnimator);
        }

        return list;
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {
        if (leftPadding == 0) {
            radius = getWidth() / 25;
            leftPadding = (getWidth() - (radius * 2) * mCount + space * (mCount - 1)) / 2;
        }

        for (int i = 0; i < mCount; i++) {
            float x = leftPadding + radius * ((i + 1) * 2 - 1) + space * i;
            float y = getHeight() / 2 - getHeight() / 4 * mAnimatedValue[i];
            canvas.drawCircle(x, y, radius, paint);
        }
    }
}
