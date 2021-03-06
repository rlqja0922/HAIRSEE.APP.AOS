package com.example.hairsee.utils;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.hairsee.R;


public class MyAlert {
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

    public static void MyDialog_single(Context context, String title, String content, View.OnClickListener event) {

        try {
            LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(MyXml, null);

            TextView Mytitle = (TextView) view.findViewById(R.id.tv_title);
            TextView Mycontent = (TextView) view.findViewById(R.id.tv_content);
            Mytitle.setText(title);
            Mycontent.setText(content);
            Button btnAgree = (Button) view.findViewById(R.id.btn_agree);
            Button btnReject = (Button) view.findViewById(R.id.btn_reject);
            btnAgree.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
            Button btnOk = view.findViewById(R.id.btn_ok);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            if (event == null){//null ???????????? dismiss ??? ??? ?????? ????????? ?????? ??????. // null??? ?????? ????????? ?????? ???????????????
                event = (v)-> dialog.dismiss();
            }

            btnOk.setOnClickListener((View.OnClickListener) event);
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        } catch (Exception e) {
            Log.d("showAlert_single", "MyAlert_single: error " + e.getMessage());
        }
    }

    public static void MyDialog(Context context, String title, String content, View.OnClickListener event, View.OnClickListener event2) {
        try {
            LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(MyXml, null);

            TextView Mytitle = (TextView) view.findViewById(R.id.tv_title);
            TextView Mycontent = (TextView) view.findViewById(R.id.tv_content);
            Button btnOk = (Button) view.findViewById(R.id.btn_ok);
            Button btnAgree = (Button) view.findViewById(R.id.btn_agree);
            Button btnReject = (Button) view.findViewById(R.id.btn_reject);

            Mytitle.setText(title);
            Mycontent.setText(content);
            btnOk.setVisibility(View.GONE);
            btnAgree.setOnClickListener((View.OnClickListener) event);


            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setView(view);
            AlertDialog dialog = builder.create();

            if (event2 == null){
                event2 = (v)->dialog.dismiss();
            }
            btnReject.setOnClickListener((View.OnClickListener) event2);
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        catch (Exception e) {
            Log.d("showAlert", "MyAlert: error " + e.getMessage());
        }
    }
}