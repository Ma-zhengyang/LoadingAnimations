package com.example.mzy.loadinganimations.indicator.Star;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

import com.example.mzy.loadinganimations.indicator.IndicatorDrawable;

import java.util.ArrayList;

/**
 * Created by mzy on 2018/9/22.
 */

public class StarIndicator extends IndicatorDrawable {

    private static final float THICKNESS = 0.7f; //星的胖度系数
    private final String TAG = StarIndicator.class.getSimpleName();

    //正多边形每个内角相等，度数为（n-2）*180/n，n为边数
    private float starHeight;
    private StarModel mStarModel = new StarModel(THICKNESS);
    private CornerPathEffect pathEffect = new CornerPathEffect(30);

    private float mAnimatedValue;
    private RectF mShadowRect = new RectF();
    private float mShadowWidth;

    public StarIndicator(Context context) {
        Log.d(TAG, "StarIndicator: ");
        mContext = context;
        init();

        starHeight = dip2px(mContext, 20.0f);
        mStarModel.setDrawingOuterRect(0, 0, starHeight);

        mShadowWidth = dip2px(mContext, 10.0f);
    }

    @Override
    protected ArrayList<ValueAnimator> getAnimation() {

        ArrayList<ValueAnimator> list = new ArrayList<>();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatedValue = ((float) valueAnimator.getAnimatedValue());
                invalidateSelf();
            }
        });

        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(2000);
        list.add(valueAnimator);

        return list;
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {

        canvas.save();

        float x = getWidth() / 2 - starHeight / 2;
        float value;
        if (mAnimatedValue <= 0.5) {//下落
            value = mAnimatedValue;
        } else {//弹起
            value = 1 - mAnimatedValue;
        }
        canvas.translate(x, getHeight() / 4 + value * starHeight);

        //以五角星中心位置为旋转原点
        canvas.rotate(360 * mAnimatedValue, starHeight / 2, starHeight / 2);
        drawStar(canvas, paint);

        canvas.restore();

        //恢复画布后再画阴影，否则阴影也会旋转
        drawShadow(canvas, paint);
    }

    private void drawStar(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setPathEffect(pathEffect);

        VertexF prev = mStarModel.getVertex(1);
        Path path = new Path();

        for (int i = 0; i < 5; i++) {
            path.rewind();
            path.moveTo(prev.x, prev.y);

            VertexF next = prev.next;

            path.lineTo(next.x, next.y);
            path.lineTo(next.next.x, next.next.y);
            canvas.drawPath(path, paint);

            prev = next.next;
        }

        // fill the middle hole. use +1.0 +1.5 because the path-API will leave 1px gap.
        path.rewind();
        prev = mStarModel.getVertex(1);
        path.moveTo(prev.x - 1f, prev.y - 1f);
        prev = prev.next.next;
        path.lineTo(prev.x + 1.5f, prev.y - 0.5f);
        prev = prev.next.next;
        path.lineTo(prev.x + 1.5f, prev.y + 1f);
        prev = prev.next.next;
        path.lineTo(prev.x, prev.y + 1f);
        prev = prev.next.next;
        path.lineTo(prev.x - 1f, prev.y + 1f);

        paint.setPathEffect(null);
        canvas.drawPath(path, paint);
    }

    private void drawShadow(Canvas canvas, Paint paint) {

        float value;
        if (mAnimatedValue <= 0.5) {//下落
            value = mAnimatedValue;
        } else {//弹起
            value = 1 - mAnimatedValue;
        }

        //getHeight() / 4 + 0.5f * starHeight为星的top那个角的y坐标
        float y = getHeight() / 4 + 0.5f * starHeight + starHeight;

        mShadowRect.set(getWidth() / 2 - value * mShadowWidth - 10,
                y,
                getWidth() / 2 + value * mShadowWidth + 10,
                y + 8);

        canvas.drawOval(mShadowRect, paint);
    }
}
