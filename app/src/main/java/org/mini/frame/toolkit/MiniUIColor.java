package org.mini.frame.toolkit;

import android.graphics.Color;

public class MiniUIColor {

    public static int color(String color) {
        if (color.startsWith("#")) {
            return Color.parseColor(color);
        } else {
            return Color.parseColor("#"+color);
        }
    }

}
