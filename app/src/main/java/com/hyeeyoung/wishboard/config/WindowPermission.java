package com.hyeeyoung.wishboard.config;

import android.Manifest;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.hyeeyoung.wishboard.CustumSnackbar;
import com.hyeeyoung.wishboard.R;

import java.util.List;

// @goal : 위험권한 얻어오기
public class WindowPermission {
    // @see : http://blog.naver.com/PostView.nhn?blogId=pointer98&logNo=222017159322&parentCategoryNo=&categoryNo=&viewDate=&isShowPopularPosts=false&from=postView
    protected Context mContext;
    private String TAG = "WindowPermissionActivity";
    //protected View view;
    public WindowPermission(Context context) {
        mContext = context;
    }

    // @todo : 추후 토스트나 스낵바 커스텀 시 사용 예정
//    public WindowPermission(View view, Context context) {
//        this.view = view;
//        this.mContext = context;
//    }

    public void setPermission(@NonNull String... permissions) {
        // @brief : 권한 설정 확인
        boolean isAlertWindowPermissionGranted = TedPermission.isGranted(mContext,
                Manifest.permission.SYSTEM_ALERT_WINDOW);
        Log.d(TAG, "SYSTEM_ALERT_WINDOW: " + isAlertWindowPermissionGranted);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // @brief : 권한 부여
                //Toast.makeText(mContext, "권한 부여", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                // @brief : 권한 거부
                //new CustumSnackbar(view, "권한 거부", Snackbar.LENGTH_SHORT).show();
                Toast.makeText(mContext, "권한 거부\n" + deniedPermissions.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(mContext)
                .setPermissionListener(permissionlistener)
                .setRationaleTitle("필요한 권한")
                .setRationaleMessage("연락처 읽기, 위치 찾기와 시스템 경고 창 권한이 필요합니다.")  // @todo : 메세지 변경 예정
                .setDeniedTitle("권한이 거부되었습니다")
                .setDeniedMessage("권한을 거부하면 서비스를 사용할 수 없습니다\n" +
                        " [설정] > [허가]에서 권한을 설정하십시오")
                .setGotoSettingButtonText("설정")
                .setPermissions(permissions)
                .check();
    }
}
