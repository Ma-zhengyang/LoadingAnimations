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
 * Created by mazhengyang on 18-12-19.
 */

public class CircleTriangleIndicator extends IndicatorDrawable {

    private final String TAG = CircleTriangleIndicator.class.getSimpleName();

    private float rotateAnimatedValue = 0.0f;
    private float scaleAnimatedValue = 0.0f;

    private boolean drawAssist = false;

    public CircleTriangleIndicator(Context context, int indicatorColor, int indicatorSpeed) {
        Log.d(TAG, "CircleTriangleIndicator: ");
        this.indicatorColor = indicatorColor;
        this.indicatorSpeed = indicatorSpeed;
        if (indicatorSpeed <= 0) {
            this.indicatorSpeed = 1500;
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

        ArrayList<Animator> list = new ArrayList<>();

        ValueAnimator rotateAnimator = ValueAnimator.ofFloat(360, 0);
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimator.setDuration(indicatorSpeed);
        rotateAnimator.setInterpolator(new LinearInterpolator());

        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateAnimatedValue = ((float) animation.getAnimatedValue());
                invalidateSelf();
            }
        });

        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(1.0f, 0.3f, 1.0f);
        scaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleAnimator.setDuration(indicatorSpeed);
        scaleAnimator.setInterpolator(new LinearInterpolator());

        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleAnimatedValue = ((float) animation.getAnimatedValue());
               // invalidateSelf();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimator, scaleAnimator);

        list.add(animatorSet);

        return list;
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {

        drawAssist(canvas, paint);
        drawCircleTriangle(canvas, paint);
    }

    private void drawAssist(Canvas canvas, Paint paint) {
        if (drawAssist) {
            canvas.drawColor(Color.RED);

            canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
            canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
        }
    }

    /**
     * 中心点o（0,0）,边长 a
     *
     * A  (0, - sqrt(3) * a / 3)
     * B  (- a/2, sqrt(3) * a / 6)
     * C  (a/2, sqrt(3) * a / 6)
     *
     *
     *          A
     *
     *
     *         O
     *
     * B              C
     *
     */

    private void drawCircleTriangle(Canvas canvas, Paint paint) {

        float sideLength = getWidth() / 5;
        float radius =  getWidth() / 30;
        canvas.save();
        canvas.translate(getWidth()/2, getHeight()/2);
        canvas.scale(scaleAnimatedValue, scaleAnimatedValue);
        canvas.rotate(rotateAnimatedValue);
        float xA = 0;
        float yA = -(float)Math.sqrt(3)*sideLength/3;
        float xB = -sideLength/2;
        float yB = (float)Math.sqrt(3)*sideLength/6;
        float xC = sideLength/2;
        float yC = (float)Math.sqrt(3)*sideLength/6;
        canvas.drawCircle(xA, yA, radius,paint);//A
        canvas.drawCircle(xB, yB, radius,paint);//B
        canvas.drawCircle(xC, yC, radius,paint);//C
        canvas.drawLine(xA,yA,xB,yB,paint);
        canvas.drawLine(xB,yB,xC,yC,paint);
        canvas.drawLine(xC,yC,xA,yA,paint);
        canvas.restore();
    }

}
