package com.zinzin.loltft.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Utils {

    public static String parseFile(Activity activity, String filename) {
        String text;
        try {
            InputStream is = activity.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            text = new String(buffer);

        } catch (IOException e) {
            // Should never happen!
            throw new RuntimeException(e);
        }
        return text;
    }


    public static String linkStringFromArray(List<String> array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            stringBuilder.append(array.get(i)).append("\n");
        }
        String s = stringBuilder.toString();
        return s.substring(0, s.length() - 2);
    }

    public static void setLocked(ImageView v) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        v.setImageAlpha(128);   // 128 = 0.5
    }

    public static void setUnlocked(ImageView v) {
        v.setColorFilter(null);
        v.setImageAlpha(255);
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity.getCurrentFocus() == null) {
                return;
            }
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
