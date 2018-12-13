package com.example.mzy.indicators.Rect;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.example.mzy.indicators.IndicatorDrawable;

import java.util.ArrayList;

/**
 * Created by mazhengyang on 18-12-7.
 */

public class RectJumpMoveIndicator extends IndicatorDrawable {

    //TODO 暂未解决向右移动，倒数第二个方块伸缩效果，向左移动，顺数第二个方块伸缩效果

    private final String TAG = RectJumpMoveIndicator.class.getSimpleName();

    private final int mCount = 5;

    private int mState = 0;//0静止，1向右移动，2向左移动
    private float mPreviousAnimatedValue = 0.0f;
    private float mAnimatedValue = 0.0f;

    private RectF rectF = new RectF();

    private boolean drawAssist = false;

    private int currentJumpIndex = 0;

    public RectJumpMoveIndicator(Context context) {
        Log.d(TAG, "RectJumpMoveIndicator: ");
        init(context);
    }

    @Override
    protected void init(Context context) {
        Log.d(TAG, "init: ");
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(dip2px(context, 1.0f));
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected ArrayList<Animator> getAnimation() {

        ArrayList<Animator> list = new ArrayList<>();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 0.0f, 1.0f, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatedValue = ((float) valueAnimator.getAnimatedValue());
//                Log.d(TAG, "onAnimationUpdate: mAnimatedValue=" + mAnimatedValue);

                if (mAnimatedValue - mPreviousAnimatedValue > 0) {
                    mState = 1;
                } else if (mAnimatedValue - mPreviousAnimatedValue < 0) {
                    mState = 2;
                } else {
                    mState = 0;
                }
                mPreviousAnimatedValue = mAnimatedValue;

                invalidateSelf();
            }
        });

        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(2000);
        list.add(valueAnimator);

        return list;
    }

    private void drawAssist(Canvas canvas, Paint paint) {
        if (drawAssist) {
            canvas.drawColor(Color.RED);

            canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
            canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
        }
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {
        canvas.save();
        drawAssist(canvas, paint);
        drawRect(canvas, paint);
        canvas.restore();
    }

    private void drawRect(Canvas canvas, Paint paint) {

        float rectWidth = getWidth() / 15;
        float rectSpace = getWidth() / 30;
        float canMoveMax = (rectWidth + rectSpace) * (mCount - 1);
        float startX = (getWidth() - (canMoveMax + rectWidth)) / 2;

        if (drawAssist) {
            for (int i = 0; i < mCount; i++) {
                float left_i = startX + i * (rectWidth + rectSpace); //第i个方块的left
                canvas.drawLine(left_i, 100, left_i, getHeight() - 100, paint);
            }
        }

        //静止状态，直接画5个方块
        if (mState == 0) {
            for (int i = 0; i < mCount; i++) {
                float left_i = startX + i * (rectWidth + rectSpace);
                float top = getHeight() / 2 - rectWidth / 2;
                rectF.setEmpty();
                rectF.set(left_i, top,
                        left_i + rectWidth,
                        getHeight() / 2 + rectWidth / 2);
                canvas.drawRoundRect(rectF, 5, 5, paint);

            }
            return;
        }

        //运动状态

        float movedRectX = startX + canMoveMax * mAnimatedValue;//横向移动的方块，目前移动的x轴距离
        float moved_ = (canMoveMax * mAnimatedValue) % (rectWidth + rectSpace);
        float percent = moved_ / (rectWidth + rectSpace);//当前移动百分比（相对相邻两方块距离d而言）

        //横向移动的方块
        rectF.setEmpty();
        rectF.set(movedRectX,
                getHeight() / 2 - rectWidth / 2,
                movedRectX + rectWidth,
                getHeight() / 2 + rectWidth / 2);
        canvas.drawRoundRect(rectF, 5, 5, paint);

        //当前移动的方块
        int currentMoveIndex = 0;

        //当前跳动的方块
        for (int i = 0; i < mCount; i++) {

            float left_i = startX + i * (rectWidth + rectSpace);
            float left_next_i = startX + (i + 1) * (rectWidth + rectSpace);

            if (movedRectX >= left_i && movedRectX <= left_next_i) {

                /**
                 *    A   B
                 *    o___o___o___o___o
                 *    | d |
                 *
                 *    相对相邻两方块距离 d = (B方块left - A方块left，即 rectWidth + rectSpace)
                 *    A目前的移动距离 moved_（相对相邻两方块距离d而言） / d = B目前逆时针旋转的角度 degree / 180
                 *
                 *    degree = moved_ * 180 / d
                 *
                 *    其他情况类似推导
                 */

                int degree = -(int) (moved_ * 180 / (rectWidth + rectSpace));
                float radius = rectWidth / 2 + rectSpace / 2;//跳动半径
                double angle = degree * Math.PI / 180;
                float x = radius * (float) Math.cos(angle);
                float y = radius * (float) Math.sin(angle);

                float centerX = left_i + rectWidth + rectSpace / 2;//两方块中心点

                float l = centerX + x - rectWidth / 2;
                float t = getHeight() / 2 - rectWidth / 2 + y;
                float r = l + rectWidth;
                float b = t + rectWidth;

                rectF.setEmpty();
                rectF.set(l, t, r, b);

                if (mState == 1) {
                    currentJumpIndex = i + 1;
                    currentMoveIndex = i;
                } else if (mState == 2) {
                    currentJumpIndex = i;
                    currentMoveIndex = i + 1;
                }

                canvas.drawRoundRect(rectF, 5, 5, paint);
                break;
            }
        }

        //其他方块
        for (int i = 0; i < mCount; i++) {

            if (i == currentJumpIndex
                    || i == currentMoveIndex) {
                continue;
            }

            float left_i = startX + i * (rectWidth + rectSpace);

            float top = getHeight() / 2 - rectWidth / 2;

            int shakeIndex = 0;
            if (mState == 1) {
                shakeIndex = currentJumpIndex - 2;
            } else if (mState == 2) {
                shakeIndex = currentJumpIndex + 2;
            }

            if (i == shakeIndex) {
                if (percent <= 0.5) {//下压状态
                    top += (rectWidth * percent);
                } else if (percent > 0.5 && percent <= 1.0) {//弹起状态
                    top += (rectWidth * (1 - percent));
                }
            }

            rectF.setEmpty();
            rectF.set(left_i, top,
                    left_i + rectWidth,
                    getHeight() / 2 + rectWidth / 2);
            canvas.drawRoundRect(rectF, 5, 5, paint);
        }
    }

}
