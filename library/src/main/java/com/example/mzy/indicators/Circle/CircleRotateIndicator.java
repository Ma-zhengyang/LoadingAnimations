package com.example.mzy.indicators.Circle;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.example.mzy.indicators.IndicatorDrawable;

import java.util.ArrayList;

/**
 * Created by mazhengyang on 18-12-14.
 */

public class CircleRotateIndicator extends IndicatorDrawable {

    private final String TAG = CircleRotateIndicator.class.getSimpleName();

    private float rotateX = 0.0f;
    private float rotateY = 0.0f;

    private Camera mCamera;
    private Matrix mMatrix;

    public CircleRotateIndicator(Context context, int indicatorColor, int indicatorSpeed) {
        Log.d(TAG, "CircleRotateIndicator: ");
        this.indicatorColor = indicatorColor;
        this.indicatorSpeed = indicatorSpeed;
        if (indicatorSpeed <= 0) {
            this.indicatorSpeed = 3000;
        }

        init(context);
    }

    @Override
    protected void init(Context context) {
        Log.d(TAG, "init: ");

        mCamera = new Camera();
        mMatrix = new Matrix();

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dip2px(context, 1.0f));
        mPaint.setColor(indicatorColor);
    }

    @Override
    protected ArrayList<Animator> getAnimation() {

        ArrayList<Animator> list = new ArrayList<>();

        ValueAnimator valueAnimatorX = ValueAnimator.ofFloat(0, 360);
        valueAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                rotateX = ((float) valueAnimator.getAnimatedValue());
                invalidateSelf();
            }
        });
        valueAnimatorX.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorX.setDuration(indicatorSpeed);
        list.add(valueAnimatorX);

        ValueAnimator valueAnimatorY = ValueAnimator.ofFloat(0, 360);
        valueAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                rotateY = ((float) valueAnimator.getAnimatedValue());
                invalidateSelf();
            }
        });

        valueAnimatorY.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorY.setDuration(indicatorSpeed / 3);
        list.add(valueAnimatorY);

        return list;
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {

        mMatrix.reset();
        mCamera.save();
        mCamera.rotateX(rotateX);
        mCamera.rotateY(rotateY);
        mCamera.getMatrix(mMatrix);
        mCamera.restore();

        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        //由于缩放/旋转等操作是以（0,0）为中心的，所以为了把界面的中心与（0,0）对齐，就要preTranslate(-centerX, -centerY)，
        //操作结束后再postTranslate(centerX, centerY)移回来，这样看到的动画就是界面图片以中心点不停的缩放/旋转了
        mMatrix.preTranslate(-centerX, -centerY);
        mMatrix.postTranslate(centerX, centerY);
        canvas.concat(mMatrix);

        float radius = getWidth() / 10;

        canvas.drawCircle(centerX, centerY, radius, paint);
    }
}
