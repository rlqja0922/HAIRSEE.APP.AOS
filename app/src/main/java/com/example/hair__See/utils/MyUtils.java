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
    public void closeAppDialog(Context context, String title, String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(content);
        builder.setCancelable(false);
        builder.setPositiveButton(Html.fromHtml("<font color='#000000'>" + "확인" + "</font>"), new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){
                System.exit(0);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
//    public void loadPfofile(){
//        String Name = SharedStore.getProfileImgName(SettingActivity.this);
//        String Rank = SharedStore.getMyRank(SettingActivity.this);
//        tv_Name.setText(Name);
//        tv_Rank.setText(Rank);
//    }

    public String getBLE_PASS(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myNewPass", 0);
        return sharedPreferences.getString("password", "12345");
    }

    /**
     * sharedPreference에 저장한 문자열 안면정보를 float[]로 변환하여 전달함
     */
    public static Map<String, float[]> getUserData(Context context){
        Map<String, String> userList = (Map<String, String>) context.getSharedPreferences("UserList", 0).getAll();
        Map<String, float[]> returnList = new HashMap<String, float[]>();
        for (String key : userList.keySet()){
            String[] split_result = userList.get(key).split(" ");
            float[] floats = new float[split_result.length];
            for (int i = 0; i < split_result.length; i++){
                floats[i] = Float.parseFloat(split_result[i]);
            }
            returnList.put(key, floats);
        }
        return returnList;
    }




    public void deleteUserData(String name, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("UserList", 0).edit();
        for(int i = 0; i < 5; i++){
            try{
                editor.remove(i+name).commit();
            }catch (Exception e){
                Log.d("deleteUserData", "예외 발생 : " + e.getMessage());
            }
        }
    }



    public AlertDialog.Builder makeAlertDialog(Context context, String title, String content, String pBtnStr, String nBtnStr, DialogInterface.OnClickListener pOnClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialog);
        builder.setTitle(Html.fromHtml("<font color='#000000'>" + title + "</font>"));
        builder.setMessage(content);
        builder.setNegativeButton(pBtnStr, pOnClickListener);
        builder.setPositiveButton(Html.fromHtml("<font color='#000000'>" + nBtnStr + "</font>"), null);
        return builder;
    }

    public final void startVibrator(Context context) {
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(110, 100));
        } else {
            vibrator.vibrate(100);
        }
    }



    public static boolean checkEmptyString(String str){
        try{
            if (str.equals("null") || str.equals("") || str == null){
                return true;
            }
        }catch (NullPointerException e){
            return true;
        }
        return false;
    }
}
