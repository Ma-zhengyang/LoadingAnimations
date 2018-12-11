package com.example.mzy.indicators.Rect;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.example.mzy.indicators.IndicatorDrawable;

import java.util.ArrayList;

/**
 * Created by mazhengyang on 18-10-11.
 */

public class ParallelogramIndicator extends IndicatorDrawable {

    private final String TAG = ParallelogramIndicator.class.getSimpleName();

    private Path path = new Path();

    private final int mCount = 3;
    private float space;//相邻平行四边形间距
    private float leftPadding;

    private float offsetWidth; //平行四边形凸出部分宽度
    private float paraWidth; //平行四边形宽度
    private float paraHeight; //平行四边形高度
    private boolean drawAssist = false;

    private float[] mAnimatedValue = new float[]{
            1.0f, 1.0f, 1.0f
    };

    public ParallelogramIndicator(Context context) {
        Log.d(TAG, "ParallelogramIndicator: ");
        init(context);
    }

    @Override
    protected void init(Context context) {
        Log.d(TAG, "init: ");
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.WHITE);

        space = dip2px(context, 5.0f);
    }

    @Override
    protected ArrayList<Animator> getAnimation() {
        int[] delay = new int[]{100, 200, 300};

        ArrayList<Animator> list = new ArrayList<>();

        for (int i = 0; i < mCount; i++) {
            final int index = i;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.3f, 1.0f);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mAnimatedValue[index] = ((float) valueAnimator.getAnimatedValue());
                    invalidateSelf();
                }
            });

            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setDuration(1000);
            valueAnimator.setStartDelay(delay[i]);
            list.add(valueAnimator);
        }

        return list;
    }


    @Override
    protected void draw(Canvas canvas, Paint paint) {

        if (leftPadding == 0) {

            paraWidth = getWidth() / 20;
            paraHeight = getHeight() / 12;
            offsetWidth = paraWidth / 2;

            //第一个四边形左下点坐标
            leftPadding = (getWidth() - ((paraWidth + offsetWidth) * mCount + space * (mCount - 1))) / 2;
        }

        drawAssist(canvas, paint);
        drawParallelogram(canvas, paint);
    }

    private void drawAssist(Canvas canvas, Paint paint) {

        if (drawAssist) {
            canvas.drawColor(Color.GRAY);
            canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
            canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
        }
    }


    private void drawParallelogram(Canvas canvas, Paint paint) {

        for (int i = 0; i < mCount; i++) {
            canvas.save();

            float x1 = leftPadding + offsetWidth * (i + 1) + (paraWidth + space) * i;
            float y1 = getHeight() / 2 - paraHeight / 2;
            float x2 = x1 + paraWidth;
            float y2 = y1 + paraHeight;

            //对角线交点坐标，分别为左上点，右上点对应轴坐标相加一半
            float centerX = (x1 + x2) / 2;
            float centerY = (y1 + y2) / 2;

            //将画布移动到对角线交点
            canvas.translate(centerX, centerY);
            canvas.scale(mAnimatedValue[i], mAnimatedValue[i]);

            path.reset();//重置路径
            //此时画布起始坐标位于对角线交点，四边形各角坐标需相对此交点
            path.moveTo(-(centerX - x1), -paraHeight / 2);//左上点
            path.lineTo(-(centerX - x1) - offsetWidth, paraHeight / 2);//左下点
            path.lineTo(-(centerX - x1) - offsetWidth + paraWidth, paraHeight / 2);//右下点
            path.lineTo(-(centerX - x1) + paraWidth, -paraHeight / 2);//右上点
            path.close();

            canvas.drawPath(path, paint);
//            canvas.clipPath(path);//截取路径所绘制的图形, ?? clipPath会出现锯齿
//            canvas.drawColor(Color.WHITE);

            canvas.restore();
        }
    }
}
