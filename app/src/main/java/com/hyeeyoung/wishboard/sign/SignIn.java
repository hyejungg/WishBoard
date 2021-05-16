package com.hyeeyoung.wishboard.Sign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hyeeyoung.wishboard.R;

public class SignIn extends AppCompatActivity {

    private String email; // @params : 이메일 변수 (DB 통신 O)
    private String pw; // @params : 비밀번호 변수 (DB 통신 O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        TextView text_email = findViewById(R.id.text_email);
        TextView text_pw = findViewById(R.id.text_pw);

    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.btn_signin:
            case R.id.kakao:
            case R.id.naver:
            case R.id.btn_email_signup:
        }
    }
}