package com.example.mzy.loadinganimations;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by mzy on 2018/9/3.
 */

public class Collision extends View {

    private final String TAG = Collision.class.getSimpleName();

    private Interpolator mAccelerateInterpolator = new AccelerateInterpolator();
    private Interpolator mDecelerateInterpolator = new DecelerateInterpolator();

    private static final float DEFAULT_BALL_RADIUS = 5.0f;
    private static final float MAX_MOVE_OFFSET = 100.0f;

    private Paint mPaint;
    private int ballCount = 5;
    private float ballRadius = 0;
    private float ballOffsetX = 0;
    private float ballCenterY = 0;
    private float leftBallMoveXOffset = 0;
    private float rightBallMoveXOffset = 0;
    private float maxMoveXOffset = 0;

    private ValueAnimator valueAnimator;

    public Collision(Context context) {
        super(context);
    }

    public Collision(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initValue(context);
    }

    private void initValue(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);

        ballRadius = DensityUtil.dip2px(context, DEFAULT_BALL_RADIUS);
        maxMoveXOffset = DensityUtil.dip2px(context, MAX_MOVE_OFFSET);
    }

    private void initAnimation() {
        valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                compute((float) valueAnimator.getAnimatedValue());
                invalidate();
            }
        });

        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(2000);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow: " + getWidth());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow: ");
        stopAnimation();
    }

    private void startAnimation() {
        if (valueAnimator != null) {
            valueAnimator.start();
        }
    }

    private void stopAnimation() {
        if (valueAnimator != null) {
            valueAnimator.end();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (ballCenterY == 0) {
            ballCenterY = getHeight() / 2;
            ballOffsetX = (getWidth() - (ballCount - 2) * ballRadius * 2) / 2;

            mPaint.setShader(new LinearGradient(ballOffsetX, 0, getWidth() - ballOffsetX, 0
                    , new int[]{Color.parseColor("#FF7F00"), Color.parseColor("#F4A460")}
                    , null, LinearGradient.TileMode.CLAMP));

            initAnimation();
            startAnimation();

        } else {
            drawBall(canvas);
        }
    }

    private void drawBall(Canvas canvas) {
        for (int i = 1; i < ballCount - 1; i++) {
            canvas.drawCircle(ballOffsetX + ballRadius * (i * 2 - 1),
                    ballCenterY, ballRadius, mPaint);
        }

        // the first ball
        canvas.drawCircle(ballOffsetX - ballRadius - leftBallMoveXOffset,
                ballCenterY, ballRadius, mPaint);

        // the last ball
        canvas.drawCircle(ballOffsetX + (ballCount - 2) * ballRadius * 2 + ballRadius + rightBallMoveXOffset,
                ballCenterY, ballRadius, mPaint);
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
