package com.example.mzy.indicators.Circle;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.example.mzy.indicators.IndicatorDrawable;

import java.util.ArrayList;

/**
 * Created by mazhengyang on 18-10-24.
 */

public class CircleRotateScaleIndicator extends IndicatorDrawable {

    private final String TAG = CircleRotateScaleIndicator.class.getSimpleName();

    private final int mCount = 5;

    private float mAnimatedValue = 0.0f;

    private float[] scaleAnimatedValue = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};

    private boolean drawAssist = false;

    public CircleRotateScaleIndicator(Context context) {
        Log.d(TAG, "CircleRotateScaleIndicator: ");
        init(context);
    }

    @Override
    protected void init(Context context) {
        Log.d(TAG, "init: ");
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected ArrayList<Animator> getAnimation() {

        ArrayList<Animator> list = new ArrayList<>();

        ValueAnimator rotateAnimator = ValueAnimator.ofFloat(0, 360);
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimator.setDuration(2000);
        rotateAnimator.setInterpolator(new LinearInterpolator());

        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = ((float) animation.getAnimatedValue());
                invalidateSelf();
            }
        });

        int[] delays = {0, 100, 200, 300, 400};
        for (int i = 0; i < mCount; i++) {
            final int index = i;
            ValueAnimator scaleAnimator = ValueAnimator.ofFloat(1, 0.4f, 1);
            scaleAnimator.setDuration(1000);
            scaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
            scaleAnimator.setStartDelay(delays[i]);
            scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleAnimatedValue[index] = ((float) animation.getAnimatedValue());
                }
            });

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(rotateAnimator, scaleAnimator);

            list.add(animatorSet);
        }

        return list;
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {

        canvas.save();
        drawAssist(canvas, paint);
        drawBall(canvas, paint);
        canvas.restore();
    }

    private void drawAssist(Canvas canvas, Paint paint) {
        if (drawAssist) {
            canvas.drawColor(Color.RED);

            canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
            canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
        }
    }

    private void drawBall(Canvas canvas, Paint paint) {

        //五个圆的中心点到每个小圆的圆心距离
        float out_radius = getWidth() / 10;
        float radius = getWidth() / 25;

        for (int i = 0; i < mCount; i++) {

            int degree = (int) (360 / mCount * i + mAnimatedValue);//改成负值变成逆时针
            double angle = degree * Math.PI / 180;
            float x = getWidth() / 2 + out_radius * (float) Math.cos(angle);
            float y = getHeight() / 2 + out_radius * (float) Math.sin(angle);
            canvas.drawCircle(x, y, radius * scaleAnimatedValue[i], paint);
        }
    }

}
