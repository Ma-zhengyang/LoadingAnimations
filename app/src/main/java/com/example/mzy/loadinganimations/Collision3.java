package com.example.mzy.loadinganimations;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by mazhengyang on 18-9-7.
 */

public class Collision3 extends View {
    private final String TAG = Collision3.class.getSimpleName();

    private static final float DEFAULT_BALL_RADIUS = 5.0f;
    private static final float MAX_MOVE_OFFSET = 15.0f;

    private Paint mPaint;
    private int ballCount = 2;
    private float ballRadius = 0;
    private float ballOffsetX = 0;
    private float ballCenterY = 0;
    private float leftBallMoveXOffset = 0;
    private float rightBallMoveXOffset = 0;
    private float maxMoveXOffset = 0;

    private ValueAnimator valueAnimator;

    public Collision3(Context context) {
        super(context);
    }

    public Collision3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initValue(context);
    }

    private void initValue(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.parseColor("#EE4000"));

        ballRadius = DensityUtil.dip2px(context, DEFAULT_BALL_RADIUS);
        maxMoveXOffset = DensityUtil.dip2px(context, MAX_MOVE_OFFSET);
    }

    private void initAnimation() {
        valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();

                Log.d(TAG, "onAnimationUpdate: " + value);
                leftBallMoveXOffset = -value * maxMoveXOffset;
                rightBallMoveXOffset = value * maxMoveXOffset;

                invalidate();
            }
        });

        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(500);
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

            // 12
            // oo

            ballCenterY = getHeight() / 2;
            ballOffsetX = getWidth() / 2;

            initAnimation();
            startAnimation();

        } else {
            drawBall(canvas);
        }
    }

    private void drawBall(Canvas canvas) {
        // the first ball
        canvas.drawCircle(ballOffsetX + leftBallMoveXOffset,
                ballCenterY, ballRadius, mPaint);

        // the second ball
        canvas.drawCircle(ballOffsetX + rightBallMoveXOffset,
                ballCenterY, ballRadius, mPaint);
    }
}
