package com.morax.cashtrack.utils;

import com.morax.cashtrack.R;

public class Utils {

    public static int getCategoryThumbnail(String category){
        if ("Fitness".equals(category)) {
            return R.drawable.fitness;
        } else if ("Education".equals(category)) {
            return R.drawable.education;
        } else {
            return R.drawable.sneakers;
        }
    }
}
