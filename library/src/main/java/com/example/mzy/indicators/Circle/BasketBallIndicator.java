package com.example.mzy.indicators.Circle;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;

import com.example.mzy.indicators.IndicatorDrawable;

import java.util.ArrayList;

/**
 * Created by mzy on 2018/8/26.
 */

public class BasketBallIndicator extends IndicatorDrawable {

    private final String TAG = BasketBallIndicator.class.getSimpleName();

    private float mAnimatedValue = 0.0f;

    public BasketBallIndicator(Context context) {
        Log.d(TAG, "BasketBallIndicator: ");
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
    protected ArrayList<ValueAnimator> getAnimation() {

        /**
         *    |___o
         *    |___o
         *    |
         *    |
         */
        ArrayList<ValueAnimator> list = new ArrayList<>();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.25f, 0.5f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatedValue = ((float) valueAnimator.getAnimatedValue());
                invalidateSelf();
            }
        });

        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(500);
        list.add(valueAnimator);

        return list;
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {
        canvas.save();
        drawBall(canvas, paint);
        drawInverted(canvas, paint);
        canvas.restore();
    }

    private void drawBall(Canvas canvas, Paint paint) {
        paint.setColor(Color.WHITE);

        float x = getWidth() / 2;
        float move_y = getHeight() * mAnimatedValue;
        float radius = getWidth() / 20;

        if (mAnimatedValue < 0.4f) {
            canvas.drawCircle(x, move_y, radius, paint);
        } else {
            RectF rectF = new RectF(x - radius, move_y - radius,
                    x + radius, move_y + radius - 2);
            canvas.drawOval(rectF, paint);
        }
    }

    private void drawInverted(Canvas canvas, Paint paint) {

        float x = getWidth() / 2;

        float ratio = (float) ((mAnimatedValue - 0.25) / 0.25);

        if (ratio < 0.3) {
            return;
        }

        float radius = getWidth() / 10;

        float cut = radius * ratio * 0.7f;

        paint.setColor(Color.GRAY);
        RectF rectF = new RectF(x - cut, getHeight() / 2 + 10, x + cut, getHeight() / 2 + 20);
        canvas.drawOval(rectF, paint);
    }

}
