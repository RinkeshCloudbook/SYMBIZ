package com.symbiz.test.Model;

import android.view.View;

public class Tools {

    public static boolean toggleArrow(boolean show, View view, boolean delay) {
        if (show) {
            view.animate().setDuration(delay ? 200 : 0).rotation(180);
            return true;
        } else {
            view.animate().setDuration(delay ? 200 : 0).rotation(0);
            return false;
        }
    }

    public static boolean toggleArrow(boolean show, View view) {
        return toggleArrow(show, view, true);
    }
}
