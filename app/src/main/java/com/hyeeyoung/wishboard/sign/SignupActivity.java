package com.hyeeyoung.wishboard.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hyeeyoung.wishboard.R;

public class SignupActivity extends AppCompatActivity {

    private String email; // @params : DB에 들어갈 사용자 email
    private String pw; // @params : DB에 들어갈 사용자 pw
    //private String pw_re; @deprecated : DB에 안넣고 이 안에서 유효성 검사 시 필요?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView text_email = findViewById(R.id.text_email);
        TextView text_pw = findViewById(R.id.text_pw);
        TextView text_pw_re = findViewById(R.id.text_pw_re);

        Button btn_sign_up = findViewById(R.id.btn_signup);
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}