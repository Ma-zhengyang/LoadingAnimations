package com.example.mzy.indicators;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mazhengyang on 18-9-18.
 */

public abstract class IndicatorDrawable extends Drawable implements Animatable {

    private final String TAG = IndicatorDrawable.class.getSimpleName();

    protected Context mContext;
    private ArrayList<ValueAnimator> mAnimatorsList;
    private Rect mBounds = new Rect();
    private Paint mPaint = new Paint();

    protected void init() {
        Log.d(TAG, "init: ");
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.WHITE);
    }

    protected abstract void draw(Canvas canvas, Paint paint);

    protected abstract ArrayList<ValueAnimator> getAnimation();

    @Override
    public int getAlpha() {
        return super.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public ColorFilter getColorFilter() {
        return super.getColorFilter();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

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
    public void draw(Canvas canvas) {
        draw(canvas, mPaint);
    }

    @Override
    public void start() {
        if (mAnimatorsList == null) {
            mAnimatorsList = getAnimation();
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
            if (!animator.isStarted()) {
                animator.start();
            }
        }
    }

    private void stopAnimation() {
        for (ValueAnimator animator : mAnimatorsList) {
            if (animator.isStarted()) {
                animator.end();
            }
        }
    }

    public static float dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }


}
