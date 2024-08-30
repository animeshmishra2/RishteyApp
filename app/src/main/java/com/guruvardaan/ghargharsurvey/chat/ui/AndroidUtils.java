package com.guruvardaan.ghargharsurvey.chat.ui;

import android.content.Context;

/**
 * Created by Shain Singh on 2/4/19.
 */
public class AndroidUtils {

    private static float density = 1f;

    public static int dp(float value, Context context) {
        if (density == 1f) {
            checkDisplaySize(context);
        }
        return (value == 0f) ? 0 : (int) Math.ceil((density * value));
    }

    private static void checkDisplaySize(Context context) {
        try {
            density = context.getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
