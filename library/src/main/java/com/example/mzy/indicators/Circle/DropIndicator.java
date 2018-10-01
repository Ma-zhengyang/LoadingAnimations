package com.example.mzy.indicators.Circle;

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
 * Created by mazhengyang on 18-9-25.
 */

public class DropIndicator extends IndicatorDrawable {

    private final String TAG = DropIndicator.class.getSimpleName();

    private float mAnimatedValue;

    private RectF mRectF = new RectF();

    private boolean drawAssist = false;

    public DropIndicator(Context context) {
        Log.d(TAG, "DropIndicator: " + getWidth());
        mContext = context;
        init();
    }

    @Override
    protected ArrayList<ValueAnimator> getAnimation() {

        ArrayList<ValueAnimator> list = new ArrayList<>();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatedValue = (float) valueAnimator.getAnimatedValue();
                invalidateSelf();
            }
        });

        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(1500);
        list.add(valueAnimator);

        return list;
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {
        drawAssist(canvas, paint);
        drawBall(canvas, paint);
    }

    private void drawAssist(Canvas canvas, Paint paint) {

        if (drawAssist) {
            canvas.drawColor(Color.RED);

            float maxMove = getWidth() / 2;

            float x = (getWidth() - maxMove) / 2;

            canvas.drawLine(x, 0, x, getHeight(), paint);
            canvas.drawLine(x + maxMove / 2, 0, x + maxMove / 2, getHeight(), paint);
            canvas.drawLine(x + maxMove, 0, x + maxMove, getHeight(), paint);
        }

    }

    private void drawBall(Canvas canvas, Paint paint) {

        float maxMove = getWidth() / 2;

        float radius = getWidth() / 25;

        float x = (getWidth() - maxMove) / 2;
        float y = getHeight() / 2;

        float percent = 0.0f;
        if (mAnimatedValue <= 0.5) { // 0~0.5，从左到右
            percent = mAnimatedValue;
        } else if (mAnimatedValue <= 1.0f) { // 0.5~1.0，从右到左
            percent = 1.0f - mAnimatedValue;
        }

        float extra_percent = 0.0f;
        if (mAnimatedValue <= 0.25) { // 0~0.25
            extra_percent = mAnimatedValue;
        } else if (mAnimatedValue <= 0.5f) { // 0.25~0.5
            extra_percent = 0.5f - mAnimatedValue;
        } else if (mAnimatedValue <= 0.75) {// 0.5~0.75
            extra_percent = mAnimatedValue - 0.5f;
        } else if (mAnimatedValue <= 1.0) {// 0.75~1.0
            extra_percent = 1.0f - mAnimatedValue;
        }


        // 0.5时要达到maxMove最大，*2
        // extra_percent控制椭圆效果，等于0.25或0.75时，即最中间区域，达到两倍直径

        mRectF.set(x - radius + maxMove * 2 * percent - radius * 4 * extra_percent,
                y - radius,
                x + radius + maxMove * 2 * percent + radius * 4 * extra_percent,
                y + radius);
        canvas.drawOval(mRectF, paint);
    }

}
