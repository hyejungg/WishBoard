package com.hyeeyoung.wishboard.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyeeyoung.wishboard.R;

public class SignupActivity extends AppCompatActivity {

    private String email; // @params : DB에 들어갈 사용자 email
    private String pw; // @params : DB에 들어갈 사용자 pw
    private String pw_re;  //@deprecated : DB에 안넣고 이 안에서 유효성 검사 시 필요

    private EditText edit_email;
    private EditText edit_pw;
    private EditText edit_pw_re;
    private Button btn_signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        email = edit_email.getText().toString();
        pw = edit_pw.getText().toString();
        pw_re = edit_pw_re.getText().toString();

        // @TODO : 수정 필요
        // @brief : 비밀번호 유효성 검사
        if(pw.equals(pw_re)){
            Drawable bg_btn_green = getResources().getDrawable(R.drawable.button_green);
            btn_signup.setBackground(bg_btn_green);
        }else{
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            edit_pw.setText("");
            edit_pw_re.setText("");
        }

    }

    private void init(){
        edit_email = findViewById(R.id.text_email);
        edit_pw = findViewById(R.id.text_pw);
        edit_pw_re = findViewById(R.id.text_pw_re);
        btn_signup = findViewById(R.id.btn_signup);
    }

    public void onClick(View v){

    }
}