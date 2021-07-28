package com.hyeeyoung.wishboard.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hyeeyoung.wishboard.MainActivity;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.noti.NotiFragment;

public class FireBaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "파이어베이스 푸시알림 서비스";

     // @brief : 포그라운드 상태인 앱에서 알림 메시지 또는 데이터 메시지를 수신할 때 호출되는 메소드
    public void onMessageReceived(@NonNull RemoteMessage msg) {

        // @brief : 메세지 유형이 데이타 메세지일 경우
        if (msg.getData().size() > 0) {
            Log.i(TAG, "onMessageReceived(msg): " + msg.getData().toString()); // 데이터 방식
            sendNotification(msg.getData().get("title"), msg.getData().get("content"));
            if (true) {
                scheduleJob();
            } else {
                handleNow();
            }
        }

        // @brief : 메세지 유형이 알림 메세지일 경우
        if (msg.getNotification() != null) {
            Log.i(TAG, "onMessageReceived(notification): " + msg.getNotification().getBody());
            sendNotification(msg.getNotification().getTitle(), msg.getNotification().getBody());
        }
    }

    // @brief : FCM이 보낸 메시지가 전송되지 못할 때 호출되는 콜백 메소드
    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }


    // @brief : WorkManager를 사용해서 비동기 작업 예약
    private void scheduleJob() {
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(NotiWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
    }

    // @brief : BroadcastReceivers에 할당된 시간을 처리
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    // @brief : 상태바 푸시 알림 레이아웃 커스텀 처리
    private RemoteViews getCustomDesign(String title, String message) {
        Log.i(TAG, "getCustomDesign: ");
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.noti_title, title);
        remoteViews.setTextViewText(R.id.noti_message, message);
        remoteViews.setImageViewResource(R.id.noti_icon, R.mipmap.ic_launcher);
        return remoteViews;
    }

    // @breif : 푸시알림을 생성하는 메소드
    private void sendNotification(String title, String message) {
        Log.i(TAG, "showNotification: ");
        String channel_id = "channel";

        // @todo : 상태바에서 알림 클릭 시 알림화면(프래그먼트)이 바로 실행되도록 해야함
        Intent intent = new Intent(this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Bundle bundle = new Bundle();
        //bundle.putInt("flag", 3);
        //intent.putExtras(bundle);

        // @brief : 푸시알림 부가 설정
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT); // PendingIntent.FLAG_UPDATE_CURRENT,
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                //.setSmallIcon(R.mipmap.ic_main)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(getCustomDesign(title, message));
        }
        else {
            builder = builder.setContentTitle(title)
                    .setContentText(message);
                    //.setSmallIcon(R.mipmap.ic_main);
        }

        // @brief : NotificationChannel 생성, 오레오(8.0) 이상일 경우 채널을 반드시 생성해야 함
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }

    // @brief : 새로운 토큰이 생성될 때 호출되는 메서드, 갱신을 위함
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
    }

    // @see : https://triest.tistory.com/40
}