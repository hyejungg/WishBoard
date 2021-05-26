package com.hyeeyoung.wishboard.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hyeeyoung.wishboard.MainActivity;
import com.hyeeyoung.wishboard.R;
import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

public class SigninActivity extends AppCompatActivity {
    private Intent intent;

    private String email; // @params : 이메일 변수 (DB 통신 O)
    private String pw; // @params : 비밀번호 변수 (DB 통신 O)

    private TextView text_email;
    private TextView text_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();

    }

    private void init(){
        text_email = findViewById(R.id.text_email);
        text_pw = findViewById(R.id.text_pw);

        // @brief : kakao 네이티브 앱키로 초기화
        String kakao_app_key = getResources().getString(R.string.kakao_app_key);
        KakaoSdk.init(this, kakao_app_key);

    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.btn_signin:
            case R.id.kakao:
                // @see : kakao 로그인 api v2는 Java8 사용 권고(람다식 이용)
                LoginClient.getInstance().loginWithKakaoTalk(this, (token, loginError) -> {
                    if (loginError != null) {
                        Log.e("loginWithKakaoTalk", "로그인 실패", loginError);
                    } else {
                        Log.d("loginWithKakaoTalk", "로그인 성공"+ token.getAccessToken());

                        // @brief : 사용자 정보 요청
                        UserApiClient.getInstance().me((user, meError) -> {
                            if (meError != null) {
                                Log.e("UserApiClient", "사용자 정보 요청 실패", meError);
                            } else {
                                Log.i("UserApiClient", user.toString());
                                Log.i("UserApiClient", "사용자 정보 요청 성공" +
                                        "\n회원번호: "+user.getId() +
                                        "\n이메일: "+user.getKakaoAccount().getEmail());
                            }
                            return null;
                        });
                    }
                    return null;
                });
                break;
            case R.id.naver:
            case R.id.btn_email_signup:
                intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                break;
        }
    }
}