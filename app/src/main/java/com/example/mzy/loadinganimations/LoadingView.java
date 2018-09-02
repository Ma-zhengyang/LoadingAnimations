package com.example.mzy.loadinganimations;

import android.animation.Animator;
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

public class LoadingView extends View {

    private final String TAG =LoadingView.class.getSimpleName();

    private Paint mPaint;
    private int radius = 30;
    private int startX = 0;
    private int startY = 0;
    private int endY = 0;
    private int currentY = 0;

    public LoadingView(Context context) {
        super(context);
        Log.d(TAG, "LoadingView: Constructor 1");
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "LoadingView: Constructor 2");

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        float desity = getResources().getDisplayMetrics().density;
        Log.d(TAG, "desity=" + desity);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "LoadingView: Constructor 3");
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.d(TAG, "LoadingView: Constructor 4");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(startX == 0) {
            startX = getWidth() / 2;
            startY = getHeight() / 4;
            endY = getHeight() / 2;
            playAnimation();
        } else {
//            drawLine(canvas);
            drawCircle(canvas);
            drawShader(canvas);
        }
    }

    private void playAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startY, endY);
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
                Integer value = (Integer) valueAnimator.getAnimatedValue();
                // Log.d(TAG, "onAnimationUpdate: value="+value);
                currentY = value;
                invalidate();
            }
        });

        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }

    private void drawLine(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        canvas.drawLine(0, 0, 0, getHeight(), mPaint);

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        canvas.drawPoint(0, 0, mPaint);
        canvas.drawPoint(0, getHeight() / 4, mPaint);
        canvas.drawPoint(0, getHeight() * 3 / 4, mPaint);
        canvas.drawPoint(0, getHeight(), mPaint);

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);
        canvas.drawLine(0, currentY, getWidth(), currentY, mPaint);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#0000FF"));
        if (endY - currentY > 10) {
            canvas.drawCircle(startX, currentY, radius, mPaint);
        } else {
            RectF rectF = new RectF(startX - radius, currentY - radius,
                    startX + radius, currentY + radius - 5);
            canvas.drawOval(rectF, mPaint);
        }
    }

    private void drawShader(Canvas canvas) {
        int availableY = endY - startY;
        int moved = currentY - startY;

        float ratio = (float) (moved * 1.0 / availableY);
        if (ratio < 0.3) {
            return;
        }

        int cut = (int) (radius * ratio);

        mPaint.setColor(Color.parseColor("#8C8C8C"));
        RectF rectF = new RectF(startX - cut, endY + 10, startX + cut, endY + 20);
        canvas.drawOval(rectF, mPaint);
    }

}
