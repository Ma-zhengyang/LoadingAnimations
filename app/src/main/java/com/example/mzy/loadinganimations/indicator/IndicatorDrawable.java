package com.example.mzy.loadinganimations.indicator;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mazhengyang on 18-9-18.
 */

public abstract class IndicatorDrawable extends Drawable implements Animatable {

    private final String TAG = IndicatorDrawable.class.getSimpleName();

    private ArrayList<ValueAnimator> mAnimatorsList;
    private Rect mBounds = new Rect();
    private Paint mPaint = new Paint();

    public IndicatorDrawable() {
        Log.d(TAG, "Indicator: ");
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.WHITE);
    }

    protected abstract void draw(Canvas canvas, Paint paint);

    protected abstract ArrayList<ValueAnimator> initAnimation();

    @Override
    public int getAlpha() {
        return super.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Nullable
    @Override
    public ColorFilter getColorFilter() {
        return super.getColorFilter();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mBounds.set(bounds);
    }

    protected int getWidth() {
        return mBounds.width();
    }

    protected int getHeight() {
        return mBounds.height();
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        draw(canvas, mPaint);
    }

    @Override
    public void start() {
        if (mAnimatorsList == null) {
            mAnimatorsList = initAnimation();
        }

        startAnimation();
    }

    @Override
    public void stop() {
        stopAnimation();
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    private void startAnimation() {
        for (ValueAnimator animator : mAnimatorsList) {
            animator.start();
        }
    }

    private void stopAnimation() {
        for (ValueAnimator animator : mAnimatorsList) {
            animator.end();
        }
    }

}
