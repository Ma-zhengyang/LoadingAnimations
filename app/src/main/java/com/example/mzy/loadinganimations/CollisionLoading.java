package com.example.mzy.loadinganimations;

import android.animation.Animator;
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
 * Created by mzy on 2018/9/3.
 */

public class CollisionLoading extends View {

    private final String TAG = CollisionLoading.class.getSimpleName();

    private Paint mPaint;
    private int ballRadius = 20;
    private int ballCount = 5;
    private float ballCenterY = 0;
    private float ballOffsetX = 0;
    private float mLeftBallMoveXOffset = 0;
    private float mRightBallMoveXOffset = 0;

    public CollisionLoading(Context context) {
        super(context);
    }

    public CollisionLoading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.RED);
    }

    public CollisionLoading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CollisionLoading(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (ballCenterY == 0) {
            ballCenterY = getHeight() / 2;
            ballOffsetX = (getWidth() - (ballCount - 2) * ballRadius * 2.0f) / 2;
            Log.d(TAG, "onDraw: ballOffsetX = " + ballOffsetX);
            playAnimation();
        } else {
            for (int i = 1; i < ballCount - 1; i++) {
                canvas.drawCircle(ballRadius * (i * 2 - 1) + ballOffsetX,
                        ballCenterY, ballRadius, mPaint);
            }

            canvas.drawCircle(ballOffsetX - ballRadius - mLeftBallMoveXOffset,
                    ballCenterY, ballRadius, mPaint);

            canvas.drawCircle(ballOffsetX + (ballCount - 2) * ballRadius * 2 + ballRadius + mRightBallMoveXOffset,
                    ballCenterY, ballRadius, mPaint);
        }
    }

    private void playAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 80.0f);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
//                Log.d(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
//                Log.d(TAG, "onAnimationEnd: ");
            }

            @Override
            public void onAnimationCancel(Animator animator) {
//                Log.d(TAG, "onAnimationCancel: ");
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
//                Log.d(TAG, "onAnimationRepeat: ");
            }
        });

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
//                 Log.d(TAG, "onAnimationUpdate: value="+value);
                mLeftBallMoveXOffset = value;
                mRightBallMoveXOffset = value;
                invalidate();
            }
        });

        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }
}
