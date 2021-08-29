package com.hyeeyoung.wishboard.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.noti.NotificationBroadcastReceiver;
import com.hyeeyoung.wishboard.util.NotificationUtil;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static com.hyeeyoung.wishboard.util.NotificationUtil.NOTIFICATION_MESSAGE;
import static com.hyeeyoung.wishboard.util.NotificationUtil.NOTIFICATION_TITLE;

// @see : https://triest.tistory.com/40
// @see : https://github.com/PatilShreyas/FCM-OnDeviceNotificationScheduler
public class FireBaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "파이어베이스 푸시알림 서비스";

    // @brief : 사용자 디바이스의 시스템 설정에서 '날짜 및 시간'이 자동으로 되어 있는지 확인 (그렇지 않으면 알림이 잘못된 시간에 표시될 수 있음)
    private Boolean isTimeAutomatic(Context context) {
        return Settings.Global.getInt(
                context.getContentResolver(),
                Settings.Global.AUTO_TIME,
                0
        ) == 1;
    }

     // @brief : 포그라운드 상태인 앱에서 알림 메시지 또는 데이터 메시지를 수신할 때 호출되는 메소드
    public void onMessageReceived(@NonNull RemoteMessage msg) {
        String title = msg.getData().get("title");
        String message = msg.getData().get("message");

        if (!isTimeAutomatic(getApplicationContext())) {
            Log.d(TAG, "`Automatic Date and Time` is not enabled");
            return;
        }

        // @param : 알림 예약 여부
        Boolean isScheduled =  (msg.getData().get("isScheduled") != null) ? true: false;

        if(isScheduled) { // @brief : 사용자가 지정한 상품 알림(예약됨)인 경우
            String scheduledTime = msg.getData().get("scheduledTime");
            scheduleAlarm(scheduledTime, title, message);

        } else { // @brief : 예약되지 않고 바로 전송될 알림 인 경우 (사용자 전체에게 공지 알림을 보낼 경우 사용)
            showNotification(title, message);
        }
    }

    // @breif : 푸시알림을 생성하는 메소드
    private void showNotification(String title, String message) {
        new NotificationUtil(getApplicationContext()).showNotification(title, message);
    }

    // @brief : FCM이 보낸 메시지가 전송되지 못할 때 호출되는 콜백 메소드
    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    // @brief : 새로운 토큰이 생성될 때 호출되는 메서드, 갱신을 위함
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
    }

// @brief : 일회성 알람 설정
    private void scheduleAlarm(String scheduledTimeString, String title, String message) {
        AlarmManager alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getApplicationContext(), NotificationBroadcastReceiver.class);
        alarmIntent.putExtra(NOTIFICATION_TITLE, title);
        alarmIntent.putExtra(NOTIFICATION_MESSAGE, message);

        // @brief : 알림 날짜 포맷 설정
        Date date = null;
        DateFormat scheduledTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            date = scheduledTime.parse(scheduledTimeString);
            alarmMgr.set(AlarmManager.RTC_WAKEUP, date.getTime(), PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
}