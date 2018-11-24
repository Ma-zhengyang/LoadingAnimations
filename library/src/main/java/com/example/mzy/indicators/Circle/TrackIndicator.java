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

public class TrackIndicator extends IndicatorDrawable {

    private final String TAG = TrackIndicator.class.getSimpleName();

    private final int mCount = 3;

    private float radius = 0;
    private float moveDistance = 0;//相对于中心点的可移动距离，这里设置为view宽度四分之一

    private float[][] translationX_Array = new float[][]{
            {-0.5f, -1.0f, -0.5f, 0.0f, 0.5f, 1.0f, 0.5f, 0.0f, -0.5f},
            {0.0f, 0.5f, 1.0f, 0.5f, 0.0f, -0.5f, -1.0f, -0.5f, 0.0f},
            {0.5f, 0.0f, -0.5f, -1.0f, -0.5f, 0.0f, 0.5f, 1.0f, 0.5f}
    };

    private float[][] scaleXY_Array = new float[][]{
            {0.5f, 1.0f, 1.5f, 1.0f, 0.5f, 1.0f, 1.5f, 1.0f, 0.5f},
            {1.0f, 0.5f, 1.0f, 1.5f, 1.0f, 0.5f, 1.0f, 1.5f, 1.0f},
            {1.5f, 1.0f, 0.5f, 1.0f, 1.5f, 1.0f, 0.5f, 1.0f, 1.5f}
    };

    private float[] translationXValue = new float[]{
            1.0f, 1.0f, 1.0f
    };
    private float[] scaleXYValue = new float[]{
            1.0f, 1.0f, 1.0f
    };

    private int[] color = new int[]{
            Color.RED, Color.GREEN, Color.BLUE
    };

    private boolean drawAssist = false;

    public TrackIndicator(Context context) {
        Log.d(TAG, "TrackIndicator: ");
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

        for (int i = 0; i < mCount; i++) {
            final int index = i;
            ValueAnimator translationX = ValueAnimator.ofFloat(translationX_Array[i]);
            translationX.setRepeatCount(ValueAnimator.INFINITE);
            translationX.setDuration(2000);
            translationX.setInterpolator(new LinearInterpolator());

            ValueAnimator scaleXY = ValueAnimator.ofFloat(scaleXY_Array[i]);
            scaleXY.setRepeatCount(ValueAnimator.INFINITE);
            scaleXY.setDuration(2000);
            translationX.setInterpolator(new LinearInterpolator());

            translationX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    translationXValue[index] = ((float) animation.getAnimatedValue());
                }
            });

            scaleXY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleXYValue[index] = ((float) animation.getAnimatedValue());
                    invalidateSelf();
                }
            });

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(translationX, scaleXY);

            list.add(animatorSet);
        }
        return list;
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {

        if (moveDistance == 0) {
            moveDistance = getWidth() / 4;
            radius = getWidth() / 25;
        }

        drawAssist(canvas, paint);
        drawBall(canvas, paint);
    }


    private void drawAssist(Canvas canvas, Paint paint) {
        if (drawAssist) {
            canvas.drawColor(Color.RED);

            canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
            canvas.drawLine(getWidth() / 4, 0, getWidth() / 4, getHeight(), paint);
            canvas.drawLine(getWidth() / 4 * 3, 0, getWidth() / 4 * 3, getHeight(), paint);
        }
    }

    private void drawBall(Canvas canvas, Paint paint) {
        for (int i = 0; i < mCount; i++) {
            float x = getWidth() / 2 + translationXValue[i] * moveDistance;
//            paint.setColor(color[i]);
            canvas.drawCircle(x, getHeight() / 2, scaleXYValue[i] * radius, paint);
        }
    }

}
