package com.example.mzy.indicators.Circle;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;


import com.example.mzy.indicators.IndicatorDrawable;

import java.util.ArrayList;

/**
 * Created by mzy on 2018/9/3.
 */

public class CollisionIndicator extends IndicatorDrawable {

    private final String TAG = CollisionIndicator.class.getSimpleName();

    private final int count = 4;
    private float leftBallMoveXOffset = 0;
    private float rightBallMoveXOffset = 0;
    private float maxMoveXOffset;

    private Interpolator mAccelerateInterpolator = new AccelerateInterpolator();
    private Interpolator mDecelerateInterpolator = new DecelerateInterpolator();

    private static final float DEFAULT_BALL_RADIUS = 5.0f;
    private static final float MAX_MOVE_OFFSET = 50.0f;

    public CollisionIndicator(Context context) {
        Log.d(TAG, "CollisionIndicator: ");
        init(context);
    }

    @Override
    protected void init(Context context) {
        Log.d(TAG, "init: ");
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.WHITE);

        maxMoveXOffset = dip2px(context, 30.0f);
    }

    @Override
    protected ArrayList<ValueAnimator> getAnimation() {

        ArrayList<ValueAnimator> list = new ArrayList<>();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                compute((float) valueAnimator.getAnimatedValue());
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
        canvas.save();

//        paint.setShader(new LinearGradient(ballOffsetX, 0, getWidth() - ballOffsetX, 0
//                , new int[]{Color.parseColor("#EE4000"), Color.parseColor("#FF7F00")}
//                , null, LinearGradient.TileMode.CLAMP));

        drawBall(canvas, paint);
        canvas.restore();
    }

    private void drawBall(Canvas canvas, Paint paint) {

        float radius = getWidth() / 25;

        float ballOffsetX = (getWidth() - (count - 2) * radius * 2) / 2;
        float ballCenterY = getHeight() / 2;

        for (int i = 1; i < count - 1; i++) {
            canvas.drawCircle(ballOffsetX + radius * (i * 2 - 1),
                    ballCenterY, radius, paint);
        }

        // the first ball
        canvas.drawCircle(ballOffsetX - radius - leftBallMoveXOffset,
                ballCenterY, radius, paint);

        // the last ball
        canvas.drawCircle(ballOffsetX + (count - 2) * radius * 2 + radius + rightBallMoveXOffset,
                ballCenterY, radius, paint);
    }

    private void compute(float progress) {
        if (progress <= 0.25f) {// 0 ~ 25%
            rightBallMoveXOffset = 0;
            computeLeft(progress);
        } else if (progress <= 0.5f) {// 25% ~ 50%
            rightBallMoveXOffset = 0;
            computeLeft(0.5f - progress);
        } else if (progress <= 0.75f) {// 50% ~ 75%
            leftBallMoveXOffset = 0;
            computeRight(progress - 0.5f);
        } else if (progress <= 1.0f) {// 75% ~ 100%
            leftBallMoveXOffset = 0;
            computeRight(1.0f - progress);
        }
    }

    private void computeLeft(float progress) {
        rightBallMoveXOffset = 0;
        leftBallMoveXOffset = progress * maxMoveXOffset;
    }

    private void computeRight(float progress) {
        leftBallMoveXOffset = 0;
        rightBallMoveXOffset = progress * maxMoveXOffset;
    }

}
