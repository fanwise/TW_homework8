package com.example.library.util;

import android.content.Context;

/**
 * Created by Fanwise on 2015/12/23.
 */
public class Utils {

    public static int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}
