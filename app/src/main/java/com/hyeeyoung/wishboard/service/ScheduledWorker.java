package com.hyeeyoung.wishboard.service;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.hyeeyoung.wishboard.util.NotificationUtil;
import static com.hyeeyoung.wishboard.util.NotificationUtil.NOTIFICATION_MESSAGE;
import static com.hyeeyoung.wishboard.util.NotificationUtil.NOTIFICATION_TITLE;

public class ScheduledWorker extends Worker {
    private final String TAG = "스케줄 워커";

    public ScheduledWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "Work START");

        // @brief : 알림 내용 가져오기
        String title = getInputData().getString(NOTIFICATION_TITLE);
        String message = getInputData().getString(NOTIFICATION_MESSAGE);

        // @breif : 알림 내용 디스플레이
        new NotificationUtil(getApplicationContext()).showNotification(title, message);

        // TODO Do your other Background Processing
        Log.d(TAG, "Work DONE");

        return Result.success();
    }
}
