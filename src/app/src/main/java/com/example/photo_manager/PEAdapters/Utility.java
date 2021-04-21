package com.example.photo_manager.PEAdapters;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;

public class Utility {
    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

    public static boolean checkImageIsFavourite(String uri) {
        return false;
    }
}
