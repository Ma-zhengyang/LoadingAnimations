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
 * Created by mzy on 2018/10/3.
 */

public class DoubleArcIndicator extends IndicatorDrawable {

    private final String TAG = DoubleArcIndicator.class.getSimpleName();

    private static final int IN_ANGLE = 90;
    private static final int OUT_ANGLE = 270;

    private float inRadius;
    private float outRadius;

    private RectF inRectF, outRectF;

    private float mAnimatedValue;

    public DoubleArcIndicator(Context context) {
        Log.d(TAG, "DoubleArcIndicator: ");
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

        inRadius = dip2px(context, 5.0f);
        outRadius = dip2px(context, 10.0f);
    }

    @Override
    protected ArrayList<ValueAnimator> getAnimation() {

        ArrayList<ValueAnimator> list = new ArrayList<>();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatedValue = (float) valueAnimator.getAnimatedValue();
                invalidateSelf();
            }
        });

        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(2000);
        list.add(valueAnimator);

        return list;
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {

        if (inRectF == null || outRectF == null) {
            inRectF = new RectF();
            outRectF = new RectF();
            inRectF.set(getWidth() / 2 - inRadius,
                    getHeight() / 2 - inRadius,
                    getWidth() / 2 + inRadius,
                    getHeight() / 2 + inRadius);
            outRectF.set(getWidth() / 2 - outRadius,
                    getHeight() / 2 - outRadius,
                    getWidth() / 2 + outRadius,
                    getHeight() / 2 + outRadius);
        }

        int rotateAngle = (int) (360 * mAnimatedValue);

        canvas.save();

        //外圆
        canvas.drawArc(inRectF, rotateAngle % 360, IN_ANGLE, false, paint);
        //内圆
        canvas.drawArc(outRectF, 270 - rotateAngle % 360, OUT_ANGLE, false, paint);
        canvas.restore();

    }
}
