package com.example.mzy.loadinganimations;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by mzy on 2018/8/26.
 */

public class Bounce extends View {

    private final String TAG = Bounce.class.getSimpleName();

    private static final float DEFAULT_BALL_RADIUS = 7.0f;

    private Paint mPaint;
    private float ballRadius = 0;
    private float startX = 0;
    private float startY = 0;
    private float endY = 0;
    private float ballMoveYOffset = 0;

    private ValueAnimator valueAnimator;

    public Bounce(Context context) {
        super(context);
    }

    public Bounce(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initValue(context);
    }

    private void initValue(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);

        ballRadius = DensityUtil.dip2px(context, DEFAULT_BALL_RADIUS);
    }


    private void initAnimation() {
        valueAnimator = ValueAnimator.ofFloat(startY, endY);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ballMoveYOffset = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(500);
        valueAnimator.start();
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
        if (startX == 0) {
            startX = getWidth() / 2;
            startY = getHeight() / 4;
            endY = getHeight() / 2;

            initAnimation();
            startAnimation();

        } else {
            drawBall(canvas);
            drawInverted(canvas);
        }
    }

    private void drawBall(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#EE4000"));
        if (endY - ballMoveYOffset > 10) {
            canvas.drawCircle(startX, ballMoveYOffset, ballRadius, mPaint);
        } else {
            RectF rectF = new RectF(startX - ballRadius, ballMoveYOffset - ballRadius,
                    startX + ballRadius, ballMoveYOffset + ballRadius - 5);
            canvas.drawOval(rectF, mPaint);
        }
    }

    private void drawInverted(Canvas canvas) {
        float availableY = endY - startY;
        float moved = ballMoveYOffset - startY;

        float ratio = (float) (moved * 1.0 / availableY);
        if (ratio < 0.3) {
            return;
        }

        float cut = (ballRadius * ratio);

        mPaint.setColor(Color.parseColor("#8C8C8C"));
        RectF rectF = new RectF(startX - cut, endY + 10, startX + cut, endY + 20);
        canvas.drawOval(rectF, mPaint);
    }

}
