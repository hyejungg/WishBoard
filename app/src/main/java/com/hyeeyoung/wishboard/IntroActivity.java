package com.hyeeyoung.wishboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hyeeyoung.wishboard.sign.SigninActivity;

public class IntroActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() { // @param : 자동로그인

            Intent intent;
            /*
            // @brief : 저장된 사용자 정보가 없다면 로그인 화면으로 이동
            if(SaveSharedPreferences.getUserEmail(IntroActivity.this).length() == 0 || SaveSharedPreferences.getUserPw(IntroActivity.this).length() == 0) {
                intent = new Intent(IntroActivity.this, SigninActivity.class);
            } else { //저장된 사용자 정보가 있다면 메인 화면으로 이동
                intent = new Intent(IntroActivity.this, MainActivity.class);
            }
            */
            intent = new Intent(IntroActivity.this, MainActivity.class); // @deprecated : 추후 로그인 완벽 구현 시 삭제 예정
            startActivity(intent);
            finish(); // @brief : Activity 화면 제거
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 다시 화면에 들어왔을 때 예약 걸어주기
        handler.postDelayed(r, 2000); // @brief : 2초 뒤에 Runnable 객체 수행
    }

    @Override
    protected void onPause() {
        super.onPause();
        // @brief : 화면 벗어나면, handler에 예약해놓은 작업 취소
        handler.removeCallbacks(r); // @brief : 예약 취소
    }
}