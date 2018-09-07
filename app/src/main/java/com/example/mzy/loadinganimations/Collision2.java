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
import android.view.animation.LinearInterpolator;

/**
 * Created by mazhengyang on 18-9-4.
 */

public class Collision2 extends View {

    private final String TAG = Collision2.class.getSimpleName();

    private static final float DEFAULT_BALL_RADIUS = 5.0f;

    private Paint mPaint;
    private int ballCount = 5;
    private float ballSpace = 0; //space of two balls
    private float ballRadius = 0;
    private float ballOffsetX = 0;
    private float ballCenterY = 0;
    private float ball1MoveXOffset, ball2MoveXOffset, ball3MoveXOffset, ball4MoveXOffset, ball5MoveXOffset;

    private ValueAnimator valueAnimator;

    public Collision2(Context context) {
        super(context);
    }

    public Collision2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initValue(context);
    }

    private void initValue(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);

        ballRadius = DensityUtil.dip2px(context, DEFAULT_BALL_RADIUS);
        ballSpace = ballRadius * 2;
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
        valueAnimator.setDuration(4000);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow: ");
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

            // 1  2  3  4  5
            // o——o——o——o——o

            float b = ballCount * ballRadius * 2;// 1 2 3 4 5 balls
            float s = (ballCount - 1) * ballSpace;// space between 1 2 3 4 5

            ballOffsetX = (getWidth() - b - s) / 2;
            Log.d(TAG, "onDraw: ballOffsetX=" + ballOffsetX);

            mPaint.setShader(new LinearGradient(ballOffsetX, 0, getWidth() - ballOffsetX, 0
                    , new int[]{Color.parseColor("#EE4000"), Color.parseColor("#FF7F00")}
                    , null, LinearGradient.TileMode.CLAMP));

            initAnimation();
            startAnimation();

        } else {
            drawBall1(canvas);
            drawBall2(canvas);
            drawBall3(canvas);
            drawBall4(canvas);
            drawBall5(canvas);
        }
    }

    private void drawBall1(Canvas canvas) {

        float start = ballOffsetX + ballRadius;

        canvas.drawCircle(start + ball1MoveXOffset,
                ballCenterY, ballRadius, mPaint);
    }

    private void drawBall2(Canvas canvas) {

        float b = 1 * ballRadius * 2;// 1 ball
        float s = 1 * ballSpace;// space between 1 2

        float start = ballOffsetX + b + s + ballRadius;

        canvas.drawCircle(start + ball2MoveXOffset,
                ballCenterY, ballRadius, mPaint);
    }

    private void drawBall3(Canvas canvas) {

        float b = 2 * ballRadius * 2;// 1 2 balls
        float s = 2 * ballSpace;// space between 1 2 3

        float start = ballOffsetX + b + s + ballRadius;

        canvas.drawCircle(start + ball3MoveXOffset,
                ballCenterY, ballRadius, mPaint);
    }

    private void drawBall4(Canvas canvas) {

        float b = 3 * ballRadius * 2;// 1 2 3 balls
        float s = 3 * ballSpace;// space between 1 2 3 4

        float start = ballOffsetX + b + s + ballRadius;
        canvas.drawCircle(start + ball4MoveXOffset,
                ballCenterY, ballRadius, mPaint);
    }

    private void drawBall5(Canvas canvas) {

        float b = 4 * ballRadius * 2;// 1 2 3 4 balls
        float s = 4 * ballSpace;// space between 1 2 3 4 5

        float start = ballOffsetX + b + s + ballRadius;

        canvas.drawCircle(start + ball5MoveXOffset,
                ballCenterY, ballRadius, mPaint);
    }

    private void compute(float progress) {
        if (progress <= 0.1f) {// 0 ~ 10%,  ball 1 move to right

            ball1MoveXOffset = progress * 10 * ballSpace;

        } else if (progress <= 0.2f) {// 10% ~ 20%,  ball 2 move to right

            ball2MoveXOffset = (progress - 0.1f) * 10 * ballSpace;

        } else if (progress <= 0.3f) {// 20% ~ 30%,  ball 3 move to right

            ball3MoveXOffset = (progress - 0.2f) * 10 * ballSpace;

        } else if (progress <= 0.4f) {// 30% ~ 40%,  ball 4 move to right

            ball4MoveXOffset = (progress - 0.3f) * 10 * ballSpace;

        } else if (progress <= 0.5f) {// 40% ~ 50%,  ball 5 move to right

            ball5MoveXOffset = (progress - 0.4f) * 10 * ballSpace;

        } else if (progress <= 0.6f) {// 50% ~ 60%,  ball 5 move to left

            ball5MoveXOffset = (0.6f - progress) * 10 * ballSpace;

        } else if (progress <= 0.7f) {// 60% ~ 70%,  ball 4 move to left

            ball4MoveXOffset = (0.7f - progress) * 10 * ballSpace;

        } else if (progress <= 0.8f) {// 70% ~ 80%,  ball 3 move to left

            ball3MoveXOffset = (0.8f - progress) * 10 * ballSpace;

        } else if (progress <= 0.9f) {// 80% ~ 90%,  ball 2 move to left

            ball2MoveXOffset = (0.9f - progress) * 10 * ballSpace;

        } else if (progress <= 1.0f) {// 90% ~ 100%,  ball 1 move to left

            ball1MoveXOffset = (1.0f - progress) * 10 * ballSpace;

        }
    }

}

