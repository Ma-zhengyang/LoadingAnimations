package com.example.mzy.indicators.Rect;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.example.mzy.indicators.IndicatorDrawable;

import java.util.ArrayList;

/**
 * Created by mazhengyang on 18-9-20.
 */

public class ChartRectIndicator2 extends IndicatorDrawable {

    private final String TAG = ChartRectIndicator2.class.getSimpleName();

    private final int count = 5;
    private  float rectMax;
    private int mCurrAnimatorState = 0;

    private float mAnimatedValue;
    private RectF rectF = new RectF();
    boolean repeatRunned = false;

    public ChartRectIndicator2(Context context) {
        Log.d(TAG, "ChartRectIndicator2: ");
        init(context);
    }

    @Override
    protected void init(Context context) {
        Log.d(TAG, "init: ");
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.WHITE);

        rectMax = dip2px(context, 10.0f);
    }

    @Override
    protected ArrayList<ValueAnimator> getAnimation() {

        ArrayList<ValueAnimator> list = new ArrayList<>();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatedValue = ((float) valueAnimator.getAnimatedValue());
                Log.d(TAG, "onAnimationUpdate: mAnimatedValue=" + mAnimatedValue);
                invalidateSelf();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                Log.d(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "onAnimationEnd: ");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d(TAG, "onAnimationCancel: ");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.d(TAG, "onAnimationRepeat: ");

                repeatRunned = true;

//                mAnimatedValue = 0;
//                mCurrAnimatorState++;
//                Log.d(TAG, "onAnimationRepeat: mCurrAnimatorState=" + mCurrAnimatorState);
//                if (mCurrAnimatorState > count + 1) {
//                    Log.d(TAG, "onAnimationRepeat: set mCurrAnimatorState to 0");
//                    mCurrAnimatorState = 0;
//                }

            }
        });

        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(1000);
        list.add(valueAnimator);

        return list;
    }

    /**
     * 会出现onAnimationRepeat的执行先于mAnimatedValue设置1.0，
     * 导致状态改变后的mCurrAnimatorState，对应的mAnimatedValue初始值就是1.0,开始绘制阶段出现闪烁现象
     * <p>
     * 09-21 13:59:30.594  8977  8977 D ChartRectIndicator2: onAnimationUpdate: mAnimatedValue=0.9992871
     * 09-21 13:59:30.594  8977  8977 D ChartRectIndicator2: draw: 0, 0.9992871, 145.3547
     * 09-21 13:59:30.609  8977  8977 D ChartRectIndicator2: onAnimationRepeat:
     * 09-21 13:59:30.609  8977  8977 D ChartRectIndicator2: onAnimationRepeat: mCurrAnimatorState=1
     * 09-21 13:59:30.609  8977  8977 D ChartRectIndicator2: onAnimationUpdate: mAnimatedValue=1.0
     * 09-21 13:59:30.610  8977  8977 D ChartRectIndicator2: draw: 1, 1.0, 115.33333
     * 09-21 13:59:30.626  8977  8977 D ChartRectIndicator2: onAnimationUpdate: mAnimatedValue=7.1290135E-4
     * 09-21 13:59:30.626  8977  8977 D ChartRectIndicator2: draw: 1, 7.1290135E-4, 175.29056
     * 09-21 13:59:30.642  8977  8977 D ChartRectIndicator2: onAnimationUpdate: mAnimatedValue=0.0026845932
     * 09-21 13:59:30.643  8977  8977 D ChartRectIndicator2: draw: 1, 0.0026845932, 175.17226
     */
    private void changeState() {
        if (repeatRunned) {
            if (mAnimatedValue > 0.9f) {
                //do nothing
            } else {
                repeatRunned = false;

                mCurrAnimatorState++;
                Log.d(TAG, "onAnimationRepeat: mCurrAnimatorState=" + mCurrAnimatorState);
                if (mCurrAnimatorState > count + 1) {
                    Log.d(TAG, "onAnimationRepeat: set mCurrAnimatorState to 0");
                    mCurrAnimatorState = 0;
                }
            }
        }
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {

        changeState();

        float rectWidth = getWidth() / 25;
        float rectSpace = rectWidth;
        float startX = (getWidth() - (rectWidth * count + rectSpace * (count - 1))) / 2;
        float bottomY = getHeight() / 1.5f;

        for (int i = 0; i < count; i++) {

            //mCurrAnimatorState [0, count+1]
            // =0 不显示第2 3 4 5条
            // =1 不显示第3 4 5条
            // =2 不显示第4 5条
            // =3 不显示第5条
            // >= 4 全显示
            if (i > mCurrAnimatorState) {
                break;
            }

            canvas.save();

            //mAnimatedValue   0 ~ 0.5 ~ 1
            //range            0 ~ 0.5 ~ 0
            float range = (0.5f - Math.abs(mAnimatedValue - 0.5f));
            float offsetHeight = range * rectMax;

            int j = i % 3;//3条一显示周期
            rectF.setEmpty();
            if (i == mCurrAnimatorState) {
                //当前，从无到显示
                Log.d(TAG, "draw: " + i + ", " + mAnimatedValue + ", " + (bottomY - (j + 1) * rectMax * mAnimatedValue));
                rectF.set(startX + i * (rectWidth + rectSpace),
                        bottomY - (j + 1) * rectMax * mAnimatedValue,
                        startX + i * (rectWidth + rectSpace) + rectWidth,
                        bottomY);
            } else {
                //其他，抖动效果
                rectF.set(startX + i * (rectWidth + rectSpace),
                        bottomY - (j + 1) * rectMax - offsetHeight,
                        startX + i * (rectWidth + rectSpace) + rectWidth,
                        bottomY);
            }
            canvas.drawRect(rectF, paint);
            canvas.restore();
        }


    }
}
