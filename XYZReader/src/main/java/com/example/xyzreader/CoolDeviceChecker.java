package com.example.xyzreader;

import android.os.Build;

/**
 * Created by regardtschindler on 2016/01/15.
 */
public class CoolDeviceChecker {

    public static boolean isCool(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

}
