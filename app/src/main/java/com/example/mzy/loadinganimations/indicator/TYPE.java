package com.example.mzy.loadinganimations.indicator;

import android.util.Log;

/**
 * Created by mazhengyang on 18-9-20.
 */

public enum TYPE {

    BASKETBALL(BasketBallIndicator.class),
    ZOOM(ZoomIndicator.class),
    COLLISION(CollisionIndicator.class),
    CHART1(ChartRectIndicator1.class),
    CHART2(ChartRectIndicator2.class);

    private final String TAG = TYPE.class.getSimpleName();

    private Class<?> mClass;

    TYPE(Class<?> mClass) {
        this.mClass = mClass;
    }

    public <T extends IndicatorDrawable> T newInstance() {

        try {
            String className = mClass.getName();
            Log.d(TAG, "newInstance: " + className);
            Class<?> indicatorClass = Class.forName(className);
            return (T) indicatorClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

}
