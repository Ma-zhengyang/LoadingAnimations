package com.example.mzy.indicators.Rect;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.example.mzy.indicators.IndicatorDrawable;

import java.util.ArrayList;

/**
 * Created by mzy on 2018/9/19.
 */

public class ChartRectIndicator1 extends IndicatorDrawable {

    private final String TAG = ChartRectIndicator1.class.getSimpleName();

    private final int mCount = 5;

    private float[] mAnimatedValue = new float[]{
            1.0f, 1.0f, 1.0f, 1.0f, 1.0f
    };
    private RectF rectF = new RectF();

    public ChartRectIndicator1(Context context, int indicatorColor, int indicatorSpeed) {
        Log.d(TAG, "ChartRectIndicator1: ");
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
    }

    @Override
    protected ArrayList<Animator> getAnimation() {

        int[] delay = new int[]{100, 200, 300, 400, 500};

        ArrayList<Animator> list = new ArrayList<>();

        for (int i = 0; i < mCount; i++) {
            final int index = i;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.5f, 1.0f);
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

        float rectWidth = getWidth() / 25;
        float rectSpace = rectWidth;
        float startX = (getWidth() - (rectWidth * mCount + rectSpace * (mCount - 1))) / 2;
        float bottomY = getHeight() / 1.5f;
        float rectMax = getHeight() / 1.5f;

        for (int i = 0; i < mCount; i++) {
            canvas.save();
            rectF.setEmpty();
            rectF.set(startX + i * (rectWidth + rectSpace),
                    rectMax * mAnimatedValue[i],
                    startX + i * (rectWidth + rectSpace) + rectWidth,
                    bottomY);
            canvas.drawRect(rectF, paint);
            canvas.restore();
        }
    }

}
