package com.hyeeyoung.wishboard.noti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.hyeeyoung.wishboard.service.ScheduledWorker;
import static com.hyeeyoung.wishboard.util.NotificationUtil.NOTIFICATION_MESSAGE;
import static com.hyeeyoung.wishboard.util.NotificationUtil.NOTIFICATION_TITLE;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "알림 브로드캐스트 리시버";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        String message = intent.getStringExtra(NOTIFICATION_MESSAGE);

        // @brief : 알림 데이터 생성
        Data notificationData = new Data.Builder()
                .putString(NOTIFICATION_TITLE, title)
                .putString(NOTIFICATION_MESSAGE, message)
                .build();

        // @brief OneTimeWorkRequest : 일회성 알림이므로 한 번만 실행
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(ScheduledWorker.class)
                .setInputData(notificationData)
                .build();

        // @brief : WorkManager를 사용해서 비동기 작업 예약
        WorkManager.getInstance().beginWith(work).enqueue();
        Log.d(TAG, "WorkManager is Enqueued.");
    }
}
