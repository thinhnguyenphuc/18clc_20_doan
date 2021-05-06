package com.example.photo_manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.photo_manager.ui.Album.AlbumDatabase.Album.Album;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

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

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void callBroadCast(Context context) {
        Log.e("-->", " >= 14");
        MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            /*
             *   (non-Javadoc)
             * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
             */
            public void onScanCompleted(String path, Uri uri) {
                Log.e("ExternalStorage", "Scanned " + path + ":");
                Log.e("ExternalStorage", "-> uri=" + uri);
            }
        });
    }

    public static float getScreenDpHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels / displayMetrics.density;
    }

    public static float getScreenDpWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels / displayMetrics.density;
    }

    public static float getGridItemDpWidth(Context context, int col, float dpGapWidth) {
        return ((getScreenDpWidth(context) - ((col - 1) * dpGapWidth)) / col);
    }

    //THIS IS NOT USABLE
    public static void askDialog(Context context, String title, String content) {
//        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_text, null);
//        TextView textView = view.findViewById(R.id.text);
//        textView.setText(content);
//        alertDialog.setTitle(title);
//        alertDialog.setContentView(view);
//        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.OK), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//
//            }
//        });
//        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
//        alertDialog.show();

    }

}
