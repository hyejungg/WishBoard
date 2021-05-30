package com.hyeeyoung.wishboard.sign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hyeeyoung.wishboard.MainActivity;
import com.hyeeyoung.wishboard.R;
import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class SigninActivity extends AppCompatActivity{
    private Intent intent;

    // @params : 서버와 연결하여 DB에 등록할 정보를 담는 변수
    private String email;
    private String pw;
    private String token;

    private EditText edit_email;
    private EditText edit_pw;

    // @params : google 로그인 관련 변수
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1001; // @ brief : 성공코드 ...임의로 설정함
    private FirebaseUser user;
    // @params : kakao 로그인 관련 변수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();

        /**
         * @see : onCreate()될 때마다 updateKakaoLogin()을 해두어서 해당 기기에서 로그인한 기록이 있는 경우,
         *        자동으로 로그인 정보를 가져옴
         * */
        updateKakaoLogin();

        // @TODO : goMainActivity() 수정 필요
        //goMainActivity(user != null);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:
                email = edit_email.getText().toString();
                pw = edit_pw.getText().toString();

                // @TODO: 해야 할 일
                //  @brief : 서버와 연결하여 로그인 처리


                break;
            case R.id.kakao:
                //  @brief : 카카오 로그인 진행
                signInKakao();
                break;
            case R.id.google:
                //  @brief : 구글 로그인 진행
                initGoogle();
                signInGoogle();
                break;
            case R.id.btn_email_signup:
                // @brief : 회원가입 처리
                intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void init() {
        edit_email = findViewById(R.id.email);
        edit_pw = findViewById(R.id.pw);
    }
    /**
     * @params : 메인액티비티로 넘기기 위한 함수
     **/
    private void goMainActivity(boolean isLogin){
        Log.i("goMainActivity", String.valueOf(isLogin));
        // @brief : 로그인 성공 시 MainActivity로 이동
        if(isLogin) {
            Intent goMain = new Intent(this, MainActivity.class);
            startActivity(goMain);
        }
    }

    /**
    * @params : kakao 로그인 관련 함수
    **/
    private void signInKakao(){
        // @brief : 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (LoginClient.getInstance().isKakaoTalkLoginAvailable(this))
            LoginClient.getInstance().loginWithKakaoTalk(this, callback);
        else
            LoginClient.getInstance().loginWithKakaoAccount(this, callback);
    }

    /**
     * @brief : 로그인 결과 수행에 관한 콜백메서드
     * @see : token이 전달되면 로그인 성공, token 전달 안되면 로그인 실패
     */
    Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>(){
        @Override
        public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
            if(oAuthToken != null){
                Toast.makeText(SigninActivity.this, "카카오 로그인 성공", Toast.LENGTH_SHORT).show();
                // @brief : 로그인 성공했으므로 메인엑티비티로 이동
                goMainActivity(oAuthToken != null);
            }
            if(throwable != null){
                Log.e("signInKakao()", throwable.getLocalizedMessage());
                Toast.makeText(SigninActivity.this, "카카오 로그인 실패", Toast.LENGTH_SHORT).show();
            }
            updateKakaoLogin();
            return null;
        }
    };
    // @brief : 로그인 여부를 확인 및 update UI
    private void updateKakaoLogin() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if(user != null){
                    // @brief : 로그인 성공
                    Log.i("UserApiClient", user.toString());
                    // @brief : 로그인한 유저의 email주소와 token 값 가져오기. pw는 제공 X
                    email = user.getKakaoAccount().getEmail();
                    token = String.valueOf(user.getId());
                    pw = null;
                }else {
                    // @brief : 로그인 실패

                }
                return null;
            }
        });
    }

    /**
     * @params : google 로그인 관련 함수
     **/
    private void initGoogle() {
        // @brief : 구글 로그인 시작 관련 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) // 기본 로그인 방식 사용
                .requestIdToken(getString(R.string.default_web_client_id)) // 필수사항, 사용자의 식별값(token)을 사용
                .requestEmail() // 사용자의 이메일을 사용(App이 구글에게 요청)
                .build();
        // @brief : 내 앱에서 구글의 계정을 가져다 쓸 예정
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // @brief : Firebase auth 초기화
        mAuth = FirebaseAuth.getInstance();
    }

    private void signInGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // @brief : 구글 로그인 결과
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(resultCode == RESULT_OK){
                try {
                    // @brief : 구글 로그인 성공 했다면 -> task의 정보를 가져옴
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d("firebaseAuthWithGoogle", account.getId());
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // @brief : 구글 로그인 실패 했다면
                    Log.w("firebaseAuthWithGoogle", "Google sign in failed", e);
                }
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        String acc_idToken = acct.getIdToken();
        // @brief : 구글로부터 로그인된 사용자의 정보(Credentail)을 얻어옴
        AuthCredential credential = GoogleAuthProvider.getCredential(acc_idToken, null);
        /* @brief : credentail를 사용하여 Firebase의 auth를 실행
                    signIn ~ 로그인 시도*/
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // @brief : 로그인 성공 시
                        Log.d("firebaseAuthWithGoogle", "signInWithCredential:success");
                        user = mAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(), "구글 로그인 성공", Toast.LENGTH_LONG).show();
                        goMainActivity(user != null);
                    } else {
                        // @brief : 로그인 실패 시
                        Log.w("firebaseAuthWithGoogle", "signInWithCredential:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "구글 로그인 실패", Toast.LENGTH_LONG).show();
                    }
                });
    }
}