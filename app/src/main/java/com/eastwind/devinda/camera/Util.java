package com.eastwind.devinda.camera;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Janithanoma on 3/24/18.
 */

public class Util {

    /**
     * Get Display Pixel
     *
     * @param context
     * @return
     */
    public static int getDisplayWidthPixel(Context context) {

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}
