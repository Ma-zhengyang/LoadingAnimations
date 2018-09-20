package com.example.mzy.loadinganimations;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.mzy.loadinganimations.indicator.IndicatorDrawable;
import com.example.mzy.loadinganimations.indicator.TYPE;

/**
 * Created by mazhengyang on 18-9-18.
 */

public class LoadingIndicator extends View {

    private final String TAG = LoadingIndicator.class.getSimpleName();

    private int mMinWidth;
    private int mMaxWidth;
    private int mMinHeight;
    private int mMaxHeight;

    private IndicatorDrawable mIndicator;

    public LoadingIndicator(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public LoadingIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, R.style.LoadingIndicator);
    }

    public LoadingIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, R.style.LoadingIndicator);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingIndicator, defStyleAttr, defStyleRes);
        mMinWidth = ta.getDimensionPixelSize(R.styleable.LoadingIndicator_minWidth, 60);
        mMaxWidth = ta.getDimensionPixelSize(R.styleable.LoadingIndicator_maxWidth, 60);
        mMinHeight = ta.getDimensionPixelSize(R.styleable.LoadingIndicator_minHeight, 60);
        mMaxHeight = ta.getDimensionPixelSize(R.styleable.LoadingIndicator_maxHeight, 60);
        ta.recycle();

        Log.d(TAG, "init: density=" + context.getResources().getDisplayMetrics().density);
        Log.d(TAG, "init: mMinWidth=" + mMinWidth + ", mMaxWidth=" + mMaxWidth);
        Log.d(TAG, "init: mMinHeight=" + mMinHeight + ", mMaxHeight=" + mMaxHeight);
    }

    public void setIndicator(TYPE type) {
        IndicatorDrawable indicator = type.newInstance();
        indicator.setCallback(this);
        mIndicator = indicator;
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == mIndicator || super.verifyDrawable(who);
    }

    @Override
    public void invalidateDrawable(Drawable dr) {
        if (verifyDrawable(dr)) {
            Rect dirty = dr.getBounds();
            int scrollX = getScrollX() + getPaddingLeft();
            int scrollY = getScrollY() + getPaddingTop();

            invalidate(dirty.left + scrollX, dirty.top + scrollY,
                    dirty.right + scrollX, dirty.bottom + scrollY);
        } else {
            super.invalidateDrawable(dr);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int saveCount = canvas.save();
        mIndicator.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateDrawableBounds(w, h);
    }

    private void updateDrawableBounds(int w, int h) {

        w = w - (getPaddingRight() + getPaddingLeft());
        h = h - (getPaddingTop() + getPaddingBottom());

        Log.d(TAG, "updateDrawableBounds: w=" + w + ", h=" + h);

        int left = 0;
        int top = 0;
        int right = w;
        int bottom = h;

        if (mIndicator != null) {
            int intrinsicWidth = mIndicator.getIntrinsicWidth();
            int intrinsicHeight = mIndicator.getIntrinsicHeight();
            float intrinsicAspect = (float) intrinsicWidth / intrinsicHeight;
            float boundAspect = (float) w / h;
            if (intrinsicAspect != boundAspect) {
                if (boundAspect > intrinsicAspect) {
                    // New width is larger. Make it smaller to match height.
                    final int width = (int) (h * intrinsicAspect);
                    left = (w - width) / 2;
                    right = left + width;
                } else {
                    // New height is larger. Make it smaller to match width.
                    final int height = (int) (w * (1 / intrinsicAspect));
                    top = (h - height) / 2;
                    bottom = top + height;
                }
            }
            Log.d(TAG, "updateDrawableBounds: left=" + left + ", top=" + top + ", right="
                    + right + ", bottom=" + bottom);
            mIndicator.setBounds(left, top, right, bottom);
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw = 0;
        int dh = 0;

        final Drawable d = mIndicator;
        if (d != null) {
            dw = Math.max(mMinWidth, Math.min(mMaxWidth, d.getIntrinsicWidth()));
            dh = Math.max(mMinHeight, Math.min(mMaxHeight, d.getIntrinsicHeight()));
        }

        dw = dw + (getPaddingLeft() + getPaddingRight());
        dh = dh + (getPaddingTop() + getPaddingBottom());

        final int measuredWidth = resolveSizeAndState(dw, widthMeasureSpec, 0);
        final int measuredHeight = resolveSizeAndState(dh, heightMeasureSpec, 0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }

    private void startAnimation() {
        if (getVisibility() != VISIBLE) {
            return;
        }
        mIndicator.start();
        postInvalidate();
    }

    private void stopAnimation() {
        mIndicator.stop();
        postInvalidate();
    }
}
