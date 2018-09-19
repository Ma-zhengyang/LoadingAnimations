package com.example.mzy.loadinganimations.indicator;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.mzy.loadinganimations.Indicator;

import java.util.ArrayList;

/**
 * Created by mzy on 2018/9/19.
 */

public class LineIndicator extends Indicator {

    private final String TAG = LineIndicator.class.getSimpleName();

    private int count = 5;

    private float[] mAnimatedValue = new float[]{
            1.0f, 1.0f, 1.0f, 1.0f, 1.0f
    };

    @Override
    protected ArrayList<ValueAnimator> initAnimation() {

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

        float lineWidth = getWidth() / 25;
        float lineSpace = lineWidth;
        float padding = (getWidth() - (lineWidth * count + lineSpace * (count - 1))) / 2;

        for (int i = 0; i < count; i++) {
            canvas.save();
            RectF rectF = new RectF(padding + i * (lineWidth + lineSpace), getHeight() / 1.5f * mAnimatedValue[i],
                    padding + i * (lineWidth + lineSpace) + lineWidth, getHeight() / 1.5f);
            canvas.drawRect(rectF, paint);
            canvas.restore();
        }
    }

}
