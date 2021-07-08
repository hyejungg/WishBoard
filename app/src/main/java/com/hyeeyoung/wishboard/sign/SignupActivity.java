package com.hyeeyoung.wishboard.sign;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.hyeeyoung.wishboard.MainActivity;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.UserItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;
import com.kakao.sdk.user.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "Wish 회원가입 등록";

    private String email; // @params : DB에 들어갈 사용자 email
    private String pw; // @params : DB에 들어갈 사용자 pw
    private String pw_re;  //@deprecated : DB에 안넣고 이 안에서 유효성 검사 시 필요

    private boolean isCheckPw = false;
    private boolean isCheckId = false;

    private EditText edit_email;
    private EditText edit_pw;
    private EditText edit_pw_re;
    private Button btn_signup;

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

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TAG", "onStop()");
        finish(); // @berif : back버튼 클릭 시 로그인화면으로 돌아가도록 해당 액티비티 종료
    }

    /**
     * @brief : 뷰 초기화
     */
    private void init(){
        edit_email = findViewById(R.id.edit_email);
        edit_pw = findViewById(R.id.edit_pw);
        edit_pw_re = findViewById(R.id.edit_pw_re);
        btn_signup = findViewById(R.id.btn_signup);
    }

    /**
     * @brief : 로그인 버튼을 클릭했을 때, 동작을 지정
     */
    public void onClick(View v){
        switch (v.getId()){
            // @brief : back 버튼 클릭 시 이전 화면(로그인 화면)으로 돌아가기
            case R.id.back :
                onBackPressed();
                // @brief : 오른쪽 -> 왼쪽으로 화면 전환
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
                onStop();
                break;
            case R.id.btn_signup:
                // @brief : 비밀번호 유효성 검사
                isValidPassWd();

                // @brief : 회원가입 성공하여 로그인 화면으로 이동
                if(isCheckId && isCheckPw) {
                    // @breif : 서버 연결하여 DB에 저장
                    save(email, pw_re);
                    Toast.makeText(this, "회원가입에 성공했습니다!", Toast.LENGTH_SHORT).show();
                    // @see : 토스트 확인 후 로그인 화면으로 넘어갈 수 있도록 delay 발생시킴
                    Handler timer = new Handler();
                    timer.postDelayed(() -> {
                        onStop();
                    }, 1000);
                }
                break;
        }
    }
    /**
     *@prams : 아이디 유효성 검사 함수
     * */
    private void isValidId() {
        Pattern EMAIL_PATTENRN = Patterns.EMAIL_ADDRESS; // @brief : 이메일 형식에 맞도록 패턴 생성 (@~.~)
        if(EMAIL_PATTENRN.matcher(email).matches()){
            isCheckId = true;
        }else{
            Toast.makeText(this, "이메일 형식에 맞춰 작성해주세요.", Toast.LENGTH_SHORT).show();
            edit_email.setText("");
            email = " ";
        }
    }
    /**
     *@prams : 비밀번호 유효성 검사 함수
     * */
    private void isValidPassWd(){
        pw = edit_pw.getText().toString();
        pw_re = edit_pw_re.getText().toString();

        Pattern PASSWORD_PATTENRN = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8}.$"); // @brief : 8자리 이상의 영문자, 숫자, 특수 문자 조합 정규식 패턴 생성
        Matcher match_pw = PASSWORD_PATTENRN.matcher(pw);
        Matcher match_pw_re = PASSWORD_PATTENRN.matcher(pw_re);
        Log.i("pw", pw + " //**// " + pw_re); // @deprecated : test용
        if( (pw.length() != 0 && pw_re.length() != 0) && pw.length() == pw_re.length()) {
            if (match_pw.matches() == match_pw_re.matches()){ // @brief : 정규식에 통과한 경우 (입력 형식에 맞음)
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

    /**
     * @breif : 회원가입 가능 시 버튼이 초록색으로 변경
     */
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
    /**
     * @brief : Retrofit을 이용하여 서버에 데이터 값 저장하는 함수
     * @param email 이메일 주소
     * @param pw_re 재입력한 비밀번호
     * @see : private -> protected static으로 변경. SigninActivity에서 카카오/구글 로그인 시 user_id 저장을 위해
     * */
    protected static void save(String email, String pw_re){
        // @brief : 카카오, 구글로 로그인하는 경우 -> pw가 없으므로
        if(pw_re.equals("")) pw_re = "0";

        UserItem user_item = new UserItem(null, email, pw_re, true); // @brief : option_noti는 초기값 true

        // @brief : Retrofit 통신을 통해 서버에 user 정보 저장 요청
        IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
        Call<ResponseBody> call = remote_service.signUpUser(user_item);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String seq = null;
                    try{
                        seq = response.body().string();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.i(TAG, seq);
                }else{
                    Log.e(TAG, "Retrofit 통신 실패");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // @brief : 통신 실패 ()시 callback (예외 발생, 인터넷 끊김 등의 시스템적 이유로 실패)
                Log.e(TAG, "서버 연결 실패");
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}