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

    private final String TAG = ChartRectIndicator2.class.getSimpleName();

    private final int count = 5;
    private int mCurrAnimatorState = 0;

    private float mAnimatedValue;
    private RectF rectF = new RectF();
    boolean repeatRunned = false;

    @Override
    protected ArrayList<ValueAnimator> initAnimation() {

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
     * �����onAnimationRepeat��ִ������mAnimatedValue����1.0��
     * ����״̬�ı���mCurrAnimatorState����Ӧ��mAnimatedValue��ʼֵ����1.0,��ʼ���ƽ׶γ�����˸����
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
        float rectMax = 30;

        for (int i = 0; i < count; i++) {

            //mCurrAnimatorState [0, count+1]
            // =0 ����ʾ��2 3 4 5��
            // =1 ����ʾ��3 4 5��
            // =2 ����ʾ��4 5��
            // =3 ����ʾ��5��
            // >= 4 ȫ��ʾ
            if (i > mCurrAnimatorState) {
                break;
            }

            canvas.save();

            //mAnimatedValue   0 ~ 0.5 ~ 1
            //range            0 ~ 0.5 ~ 0
            float range = (0.5f - Math.abs(mAnimatedValue - 0.5f));
            float offsetHeight = range * rectMax;

            int j = i % 3;//3��һ��ʾ����
            rectF.setEmpty();
            if (i == mCurrAnimatorState) {
                //��ǰ�����޵���ʾ
                Log.d(TAG, "draw: " + i + ", " + mAnimatedValue + ", " + (bottomY - (j + 1) * rectMax * mAnimatedValue));
                rectF.set(startX + i * (rectWidth + rectSpace),
                        bottomY - (j + 1) * rectMax * mAnimatedValue,
                        startX + i * (rectWidth + rectSpace) + rectWidth,
                        bottomY);
            } else {
                //����������Ч��
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
