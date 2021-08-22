package com.hyeeyoung.wishboard.noti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.config.ResultCode;
import com.hyeeyoung.wishboard.util.NumberPickerUtil;

public class NotiSettingActivity extends AppCompatActivity {
    private static final String TAG = "알림설정";
    private NumberPickerUtil np_util;
    private String noti_date, noti_type;
    private Switch switch_noti;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_setting);

        noti_type = null;
        noti_date = null;
        np_util = new NumberPickerUtil();
        init();

        // @brief : 저장된 상태 데이터로 각 뷰에 내용 복구
        if (savedInstanceState != null) {
            np_util.type.setValue(savedInstanceState.getInt("type"));
            np_util.date.setValue(savedInstanceState.getInt("date"));
            np_util.hour.setValue(savedInstanceState.getInt("hour"));
            np_util.minute.setValue(savedInstanceState.getInt("minute"));
        }
    }

    // @brief : 화면 회전 시 상태 저장
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("type", np_util.type.getValue());
        outState.putInt("date", np_util.date.getValue());
        outState.putInt("hour", np_util.hour.getValue());
        outState.putInt("minute", np_util.minute.getValue());
    }

    private void init() {
        switch_noti = findViewById(R.id.noti_switch);
        LinearLayout noti_setting = findViewById(R.id.noti_setting);

        // @brief : 알림 설정 토글 스위치 클릭 이벤트 처리
        switch_noti.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                noti_setting.setVisibility(View.VISIBLE);
                expand(noti_setting);
            } else {
                noti_setting.setVisibility(View.GONE);
                noti_type = null;
                noti_date = null;
            }
        });

        np_util.noti_types_array = getResources().getStringArray(R.array.noti_types_setting_array);
        np_util.type = findViewById(R.id.num_picker_type);
        np_util.date = findViewById(R.id.num_picker_date);
        np_util.hour = findViewById(R.id.num_picker_hour);
        np_util.minute = findViewById(R.id.num_picker_minute);
        np_util.initNumperPicker();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            // @brief : back 버튼 클릭 시 이전 화면으로 돌아가기
            case R.id.back:
                // @brief : 오른쪽 -> 왼쪽으로 화면 전환
                onBackPressed();
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
                break;

            case R.id.save:

                // @brief : 토글 버튼이 활성화된 상태인 경우 사용자가 설정한 알림 정보를 NewItemFragment로 전달
                if (switch_noti.isChecked()) {
                    noti_type = np_util.noti_types_array[np_util.type.getValue()];
                    noti_date = np_util.dates_server[np_util.date.getValue()] + " " + np_util.hour.getValue() + ":" + np_util.minute.getValue() * 5 + ":00";
                }

                Intent intent = new Intent();
                intent.putExtra("type", noti_type);
                intent.putExtra("date", noti_date);
                setResult(ResultCode.NOTI_RESULT_CODE, intent);
                onBackPressed();
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
                break;
        }
    }

    // @brief : 알림 설정 레이아웃 펼치기 애니메이션 적용
    private void expand(final View layout) {
        layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final int targetHeight = layout.getMeasuredHeight();

        layout.getLayoutParams().height = 1;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                layout.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                layout.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.setDuration(200);
        layout.startAnimation(a);
    }
}