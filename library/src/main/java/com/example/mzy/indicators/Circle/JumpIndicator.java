package com.example.mzy.indicators.Circle;

import android.animation.Animator;
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

public class JumpIndicator extends IndicatorDrawable {

    private final String TAG = JumpIndicator.class.getSimpleName();

    private final int mCount = 3;
    private float space;//相邻两圆间距
    private float radius;//圆的半径
    private float leftPadding;

    private float[] mAnimatedValue = new float[]{
            0.0f, 0.0f, 0.0f
    };

    public JumpIndicator(Context context) {
        Log.d(TAG, "JumpIndicator: ");
        init(context);
    }

    @Override
    protected void init(Context context) {
        Log.d(TAG, "init: ");
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(dip2px(context, 1.0f));
        mPaint.setColor(Color.WHITE);

        space = dip2px(context, 2.0f);
    }

    @Override
    protected ArrayList<Animator> getAnimation() {
        int[] delay = new int[]{100, 400, 800};

        ArrayList<Animator> list = new ArrayList<>();

        for (int i = 0; i < mCount; i++) {
            final int index = i;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f, 0.0f, -1.0f, 0.0f);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mAnimatedValue[index] = ((float) valueAnimator.getAnimatedValue());
                    if (mAnimatedValue[index] <= 0) {
                        mAnimatedValue[index] = 0;
                    }
                    invalidateSelf();
                }
            });

            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setDuration(2000);
            valueAnimator.setStartDelay(delay[i]);
            list.add(valueAnimator);
        }

        //监听最后一个对象结束，重新播放动画
//        list.get(mCount - 1).addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                Log.d(TAG, "onAnimationEnd: ");
//                stop();
//                start();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });

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
