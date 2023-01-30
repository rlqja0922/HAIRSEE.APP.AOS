package com.example.hair__See.utils;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.hair__See.R;
import com.example.hair__See.detection.WaitActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private Gson gson  = new GsonBuilder().serializeNulls().create();
    private Context context;

    public Activity activity;
    public AppCompatActivity activity1;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public MyFirebaseMessagingService() {
        super();
        Task<String> token = FirebaseMessaging.getInstance().getToken();
        token.addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    Log.d("FCM Token", task.getResult());// FCM 토큰 확인용
                    SharedStore.setFcmToken(context, task.getResult());
                }
            }
        });
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM Token", token);// FCM 토큰 확인용
        //token을 서버로 전송
    }

    //메시지 수신시 실행
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
            //fcm 데이터가있을시 알림내역 데이터 클래스를이용하여 알림내역 리스트에 표현
            if (remoteMessage.getData().size() > 0) {

                showNotification(remoteMessage.getData());

            }



    }

//노티 알람 실행 로직
    public void showNotification(Map<String, String> data) {
        //팝업 터치시 이동할 액티비티를 지정합니다.

//        AlarmData contact = new AlarmData();
//        // Gson 인스턴스 생성
//        gson = new GsonBuilder().create();
//        // JSON 으로 변환
//        String strContact = gson.toJson(contact, AlarmData.class);
        String Title = data.get("title");
        String Body = data.get("step");
        String Url = data.get("file");
        String channel_id = "CHN_ID";
        Intent intent = new Intent(this, WaitActivity.class);
        //알림 채널 아이디 : 본인 하고싶으신대로...
        intent.setAction(Intent.ACTION_MAIN)//노티 클릭 시 앱 중복 실행 방지
                .addCategory(Intent.CATEGORY_LAUNCHER)//노티 클릭 시 앱 중복 실행 방지
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)//노티 클릭 시 앱 중복 실행 방지
//                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
;


        Log.d("fcm", "showNotification: body : "+Body);

        if (Body.equals("스텝3")) {
            SharedStore.setWait(context,Body);
            SharedStore.setimgURL(context,Url);
//            intent = new Intent(MyFirebaseMessagingService.this,ResultActivity.class);
//            intent.putExtra("url",Url);
//            Log.d("fcm", "showNotification: "+Url);
//            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }else if (!Body.equals("스텝3")){
            SharedStore.setWait(context,Body);
        }
        else{
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            Intent cancelIntent = new Intent(this, MyFirebaseMessagingService.class);
            cancelIntent.setAction("Stop");
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channel_id)
                    .setSmallIcon(R.drawable.ic_baseline_switch_camera_24)
                    .setContentTitle(Title)
                    .setContentText(Body)
                    .setVibrate(new long[0])
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH) //중요도
                    .setAutoCancel(true)
                    .setGroup("GroupID")
                    .setContentIntent(pendingIntent);

//        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this,channel_id)
//                .setSmallIcon(R.drawable.ic_baseline_switch_camera_24)
//                .setContentTitle(Title)
//                .setContentText(Body)
//                .setVibrate(new long[0])
//                .setAutoCancel(true)
//                .setStyle(new NotificationCompat.InboxStyle())
//                .setPriority(NotificationCompat.PRIORITY_HIGH) //중요도
//                .setGroup("GroupID")
//                .setGroupSummary(true)
//                .setContentIntent(pendingIntent);

            //알림 채널이 필요한 안드로이드 버전을 위한 코드
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel channel = new NotificationChannel(channel_id, "start noti channel", NotificationManager.IMPORTANCE_HIGH );
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{0});
                notificationManager.createNotificationChannel(channel);
            }else {
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            }

            notificationManager.notify(0, builder.build());
//        notificationManager.notify(0,builder2.build());
        }

    }
    //======== [모바일 진동 강제 발생 메소드] ========
    public void PushCallVibrator(){
        Log.d("---","---");
        Log.d("//===========//","================================================");
        Log.d("","[A_NotiPushSetting > PushCallVibrator() 메소드 : 진동 발생 실시]");
        Log.d("//===========//","================================================");
        Log.d("---","---");
        try {
            /**
             * [메시지를 수신받으면 진동 실행]
             * 1. 진동 권한을 획득해야한다. AndroidManifest.xml
             * 2. Vibrator 객체를 얻어서 진동시킨다
             */
            Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(1000); //TODO [miliSecond, 지정한 시간동안 진동]
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }





    //======== [모바일 알림음 강제 발생 메소드] ========
    public void PushCallSound(){
        Log.d("---","---");
        Log.d("//===========//","================================================");
        Log.d("","[A_NotiPushSetting > PushCallSound() 메소드 : 알림음 재싱 실시]");
        Log.d("//===========//","================================================");
        Log.d("---","---");
        try {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(),defaultSoundUri);
            ringtone.play(); //TODO [사운드 재생]
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}