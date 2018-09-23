package com.example.mzy.loadinganimations.indicator.Rect;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.example.mzy.loadinganimations.indicator.IndicatorDrawable;

import java.util.ArrayList;

/**
 * Created by mzy on 2018/9/19.
 */

public class ChartRectIndicator1 extends IndicatorDrawable {

    private final String TAG = ChartRectIndicator1.class.getSimpleName();

    private final int count = 5;

    private float[] mAnimatedValue = new float[]{
            1.0f, 1.0f, 1.0f, 1.0f, 1.0f
    };
    private RectF rectF = new RectF();

    public ChartRectIndicator1(Context context) {
        Log.d(TAG, "ChartRectIndicator1: ");
        mContext = context;
        init();
    }

    @Override
    protected ArrayList<ValueAnimator> getAnimation() {

        int[] delay = new int[]{100, 200, 300, 400, 500};

        ArrayList<ValueAnimator> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            final int index = i;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.5f, 1.0f);
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

        float rectWidth = getWidth() / 25;
        float rectSpace = rectWidth;
        float startX = (getWidth() - (rectWidth * count + rectSpace * (count - 1))) / 2;
        float bottomY = getHeight() / 1.5f;
        float rectMax = getHeight() / 1.5f;

        for (int i = 0; i < count; i++) {
            canvas.save();
            rectF.setEmpty();
            rectF.set(startX + i * (rectWidth + rectSpace),
                    rectMax * mAnimatedValue[i],
                    startX + i * (rectWidth + rectSpace) + rectWidth,
                    bottomY);
            canvas.drawRect(rectF, paint);
            canvas.restore();
        }
    }

}
