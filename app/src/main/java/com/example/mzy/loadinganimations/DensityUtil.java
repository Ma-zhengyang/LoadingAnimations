package com.example.mzy.loadinganimations;

import android.content.Context;

/**
 * Created by mazhengyang on 18-9-4.
 */

public class DensityUtil {

    public static float dip2px(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}
