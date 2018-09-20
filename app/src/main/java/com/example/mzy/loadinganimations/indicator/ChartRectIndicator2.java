package com.example.mzy.loadinganimations.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mazhengyang on 18-9-20.
 */

public class ChartRectIndicator2 extends IndicatorDrawable {

    private final String TAG = ChartRectIndicator1.class.getSimpleName();

    private final int count = 5;
    private int mCurrAnimatorState = 0;

    private float mAnimatedValue;
    private RectF rectF = new RectF();

    @Override
    protected ArrayList<ValueAnimator> initAnimation() {

        ArrayList<ValueAnimator> list = new ArrayList<>();


        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatedValue = ((float) valueAnimator.getAnimatedValue());
                invalidateSelf();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.d(TAG, "onAnimationRepeat: mCurrAnimatorState=" + mCurrAnimatorState);
                int stateCount = count + 1;
                if (++mCurrAnimatorState > stateCount) {
                    mCurrAnimatorState = 0;
                }
            }
        });

        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(1000);
        list.add(valueAnimator);

        return list;
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {

        float rectWidth = getWidth() / 25;
        float rectSpace = rectWidth;
        float startX = (getWidth() - (rectWidth * count + rectSpace * (count - 1))) / 2;
        float bottomY = getHeight() / 1.5f;
        float rectMax = 30;

        for (int i = 0; i < count; i++) {

            if (i > mCurrAnimatorState)
            {
                break;
            }

            canvas.save();
            float offsetHV = (0.5f - Math.abs(mAnimatedValue - 0.5f)) * rectMax;
            int j = i % 3;

            rectF.setEmpty();
            if (i == mCurrAnimatorState) {
                rectF.set(startX + i * (rectWidth + rectSpace),
                        bottomY - (j + 1) * rectMax * mAnimatedValue,
                        startX + i * (rectWidth + rectSpace) + rectWidth,
                        bottomY);
            } else {
                rectF.set(startX + i * (rectWidth + rectSpace),
                        bottomY - (j + 1) * rectMax - offsetHV,
                        startX + i * (rectWidth + rectSpace) + rectWidth,
                        bottomY);
            }
            canvas.drawRect(rectF, paint);
            canvas.restore();
        }


    }
}
