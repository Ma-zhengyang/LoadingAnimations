package com.example.mzy.indicators.Circle;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.example.mzy.indicators.IndicatorDrawable;

import java.util.ArrayList;

/**
 * Created by mazhengyang on 18-10-9.
 */

public class ArcScaleRotateIndicator extends IndicatorDrawable {
    private final String TAG = ArcScaleRotateIndicator.class.getSimpleName();

    private float radius;

    private RectF mRectF;

    private float mScaleValue;
    private float mRotateValue;

    public ArcScaleRotateIndicator(Context context) {
        Log.d(TAG, "ArcScaleRotateIndicator: ");
        init(context);
    }

    @Override
    protected void init(Context context) {
        Log.d(TAG, "init: ");
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setDither(true);

        radius = dip2px(context, 10.0f);
    }

    @Override
    protected ArrayList<ValueAnimator> getAnimation() {

        ArrayList<ValueAnimator> list = new ArrayList<>();

        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(1.0f, 0.0f, 1.0f);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mScaleValue = (float) valueAnimator.getAnimatedValue();
                invalidateSelf();
            }
        });

        scaleAnimator.setInterpolator(new LinearInterpolator());
        scaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleAnimator.setDuration(2000);

        ValueAnimator rotateAnimator = ValueAnimator.ofFloat(0, 180, 360);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mRotateValue = (float) valueAnimator.getAnimatedValue();
                invalidateSelf();
            }
        });

        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimator.setDuration(1000);

        list.add(scaleAnimator);
        list.add(rotateAnimator);

        return list;
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {

        if (mRectF == null) {
            mRectF = new RectF();
            mRectF.set(-radius, -radius, radius, radius);
        }

        canvas.save();

        float x = getWidth() / 2;
        float y = getHeight() / 2;
        canvas.translate(x, y);
        canvas.scale(mScaleValue, mScaleValue);
        canvas.rotate(mRotateValue);

//        paint.setColor(Color.RED);
//        canvas.drawOval(mRectF,paint);

        /**
         *          360
         *           |
         *       225 |
         *  270 ----------- 0
         *           | 45
         *           |
         *          90
         */
//        paint.setColor(Color.WHITE);
        if (mScaleValue < 0.1f) {
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(0, 0, radius, paint);
        } else {
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(mRectF, 225, 90, false, paint);
            canvas.drawArc(mRectF, 45, 90, false, paint);
        }

        canvas.restore();

    }
}
