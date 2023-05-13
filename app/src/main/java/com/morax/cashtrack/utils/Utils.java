package com.morax.cashtrack.utils;

import com.morax.cashtrack.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}
