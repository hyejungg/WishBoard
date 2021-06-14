package com.hyeeyoung.wishboard.sign;

import android.app.Application;

import com.hyeeyoung.wishboard.R;
import com.kakao.sdk.common.KakaoSdk;

/**
 * KakaoDeveloper 사이트에서 SDK를 초기화 해주는 과정을 별도의 Application class 파일로 빼두길 권고
 * Kakao Login API version 2("com.kakao.sdk:v2-user:2.2.0") Java 8 이상 권고
 * KakaoDeveloper 사이트에서는 Kotiln을 기반으로 작성되어 있음. 콜백메소드 역시 Kotiln으로 구성
 * */

public class KakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // @brief : kakao 네이티브 앱키로 초기화
        String kakao_app_key = getResources().getString(R.string.kakao_app_key);
        KakaoSdk.init(this, kakao_app_key);
    }
}
