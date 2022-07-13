package com.example.hairsee;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class SharedStore {

    private static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences("rebuild_preferences",Context.MODE_PRIVATE);
    }


    public static void setString(Context context, String key, String value){
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public static String getString(Context context, String key){
        SharedPreferences preferences = getPreferences(context);
        String value = preferences.getString(key, "");
        return value;
    }

    public static boolean getAutoLogin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", 0);
        return sharedPreferences.getBoolean("isAutoLogin", false);
    }
    public static void setAutoLogin(Context context, boolean isAutoLogin){
        Log.d("autoCheck", "autoCheck : " + isAutoLogin);
        context.getSharedPreferences("MyData", 0).edit().putBoolean("isAutoLogin", isAutoLogin).commit();
    }


    public static void deleteUserData(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("MyData", 0).edit();
        try{
            editor.clear().apply();
        }catch (Exception e){
            Log.d("deleteUserData", "예외 발생 : " + e.getMessage());
        }
    }

    public static void setBitmap(Context context, String key, Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,30,baos);
        byte [] b= baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        setString(context, key, temp);
    }
    public static Bitmap getBitmap(Context context, String key){
        String temp = getString(context, key);
        try {
            byte [] encodeByte = Base64.decode(temp, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch (Exception e){
            return null;
        }
    }
}
