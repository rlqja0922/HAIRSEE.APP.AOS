package com.example.hair__See.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.hair__See.R;

import java.util.HashMap;
import java.util.Map;

public class MyUtils {


    public final void startVibrator(Context context) {
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(110, 100));
        } else {
            vibrator.vibrate(100);
        }
    }

}
