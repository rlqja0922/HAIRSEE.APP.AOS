package com.example.hair__See.utils;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.hair__See.R;


public class MyAlert {
    public static Dialog dialogrem;
    private static int MyStyle =R.style.MyAlertDialog;
    private static int MyXml = R.layout.alertxml;

    public static void showAlert_single(Context context, String title, String content, String pBtnStr, DialogInterface.OnClickListener event) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, MyStyle);
            builder.setTitle(Html.fromHtml("<font color='#000000'>" + title + "</font>"));
            builder.setMessage(content);
            builder.setNegativeButton(pBtnStr, event);
            builder.show();

        } catch (Exception e) {
            Log.d("showAlert_single", "showAlert_single: error " + e.getMessage());
        }
    }

    public static void MyDialog_single(Context context, String title,  View.OnClickListener event) {

        try {
            LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(MyXml, null);

            TextView Mytitle = (TextView) view.findViewById(R.id.tv_title);
            Mytitle.setText(title);
            ImageView btnOk = view.findViewById(R.id.btn_ok);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(view);
            dialogrem = builder.create();
            dialogrem.setCancelable(false);
            if (event == null){//null 일경우는 dismiss 할 수 있는 코드로 자동 적용. // null이 아닐 경우는 직접 처리해야함
                event = (v)-> dialogrem.dismiss();
            }

            btnOk.setOnClickListener((View.OnClickListener) event);
            dialogrem.show();
            dialogrem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        } catch (Exception e) {
            Log.d("showAlert_single", "MyAlert_single: error " + e.getMessage());
        }
    }
}