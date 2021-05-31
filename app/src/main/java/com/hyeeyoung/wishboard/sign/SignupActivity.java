package com.hyeeyoung.wishboard.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyeeyoung.wishboard.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private String email; // @params : DB에 들어갈 사용자 email
    private String pw; // @params : DB에 들어갈 사용자 pw
    private String pw_re;  //@deprecated : DB에 안넣고 이 안에서 유효성 검사 시 필요
    private boolean isCheckPw = false;
    private boolean isCheckId = false;

    private EditText edit_email;
    private EditText edit_pw;
    private EditText edit_pw_re;
    private Button btn_signup;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        edit_email.setOnFocusChangeListener((view, b) -> {
            email = edit_email.getText().toString();
            isValidId();
        });
    }

    private void init(){
        edit_email = findViewById(R.id.edit_email);
        edit_pw = findViewById(R.id.edit_pw);
        edit_pw_re = findViewById(R.id.edit_pw_re);
        btn_signup = findViewById(R.id.btn_signup);
    }

    public void onClick(View v){
        switch (v.getId()){
            // @brief : back 버튼 클릭 시 이전 화면으로 돌아가기
            case R.id.back :
                onBackPressed();
                // @brief : 오른쪽 -> 왼쪽으로 화면 전환
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
                break;

            case R.id.btn_signup:
                // @brief : 비밀번호 유효성 검사
                isValidPassWd();

                // @TODO: 해야 할 일
                // @breif : 서버 연결하여 DB에 저장

                /**
                 * @see : 서버와 연결 성공하면 token 값을 생성, 이 값과 함께 db에 저장
                 *         이후 이 token 값은 로그인 화면에서 사용?
                 */

                // @brief : 회원가입 성공하여 로그인 화면으로 이동
                if(isCheckId && isCheckPw) {
                    Toast.makeText(this, "회원가입에 성공하였습니다!", Toast.LENGTH_SHORT).show();
                    // @see : 토스트 확인 후 로그인 화면으로 넘어갈 수 있도록 delay 발생시킴
                    Handler timer = new Handler();
                    timer.postDelayed(() -> {
                        intent = new Intent(SignupActivity.this, SigninActivity.class);
                        startActivity(intent);
                    }, 1000);
                }
                break;
        }
    }
    /**
     *@brief : 아이디 유효성 검사
     * */
    private void isValidId() {
        Pattern EMAIL_PATTENRN = Patterns.EMAIL_ADDRESS;
        if(EMAIL_PATTENRN.matcher(email).matches()){
            isCheckId = true;
        }else{
            Toast.makeText(this, "이메일 형식에 맞춰 작성해주세요.", Toast.LENGTH_SHORT).show();
            edit_email.setText("");
            email = " ";
        }
    }
    /**
     *@brief : 비밀번호 유효성 검사
     * */
    private void isValidPassWd(){
        pw = edit_pw.getText().toString();
        pw_re = edit_pw_re.getText().toString();

        // @brief :
        Pattern PASSWORD_PATTENRN = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8}.$");
        Matcher match_pw = PASSWORD_PATTENRN.matcher(pw);
        Matcher match_pw_re = PASSWORD_PATTENRN.matcher(pw_re);
        Log.i("pw", pw + " //**// " + pw_re);
        if( (pw.length() != 0 && pw_re.length() != 0) && pw.length() == pw_re.length()) { //뭔가 입력함
            if (match_pw.matches() == match_pw_re.matches()){ //형식에 맞는 경우
                setBtnBackgroud();
            } else {
                Toast.makeText(this, "비밀번호 형식에 맞춰 작성해주세요.", Toast.LENGTH_SHORT).show();
                edit_pw.setText("");
                edit_pw_re.setText("");
            }
        }else{
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

        }
    }
    private void setBtnBackgroud(){
        if(isCheckId){ // @brief : 아이디도 입력되어 있다면,
            Drawable bg_btn_green = getResources().getDrawable(R.drawable.button_green);
            btn_signup.setBackground(bg_btn_green);
            isCheckPw = true;
        }else{
            Toast.makeText(this, "이메일 형식에 맞춰 작성해주세요.", Toast.LENGTH_SHORT).show();
            edit_pw.setText("");
            edit_pw_re.setText("");
        }

    }
}