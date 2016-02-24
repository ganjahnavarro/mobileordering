package com.mobile.mobileordering.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by simplyph on 1/8/2016.
 */
public class FontManager{

    public static Typeface FONT_HIGHVOLTAGE;
    public static Typeface FONT_STREET;
    public static Typeface FONT_TIMES;
    public static Typeface FONT_TIMESB;
    public static Typeface FONT_TIMESBI;
    public static Typeface FONT_TIMESI;

    public FontManager(Context context){
        FONT_HIGHVOLTAGE = Typeface.createFromAsset(context.getAssets(), "HighVoltage Rough.ttf");
        FONT_STREET = Typeface.createFromAsset(context.getAssets(), "Street Sound.ttf");
        FONT_TIMES = Typeface.createFromAsset(context.getAssets(), "times.ttf");
        FONT_TIMESB = Typeface.createFromAsset(context.getAssets(), "timesbd.ttf");
        FONT_TIMESBI = Typeface.createFromAsset(context.getAssets(), "timesbi.ttf");
        FONT_TIMESI = Typeface.createFromAsset(context.getAssets(), "timesi.ttf");
    }
}
