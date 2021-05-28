package com.hyeeyoung.wishboard.sign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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

    private EditText edit_email;
    private EditText edit_pw;

    // @params : google 로그인 관련 변수
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1001; // @ brief : 성공코드 ...임의로 설정함

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();

    }

    @Override
    public void onStart() {
        super.onStart();
        // @brief : currentUser 정보 가져오기
//        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:
                email = edit_email.getText().toString();
                pw = edit_pw.getText().toString();
                break;
            case R.id.kakao:
                // @see : kakao 로그인 api v2는 Java8 사용 권고(람다식 이용)
                LoginClient.getInstance().loginWithKakaoTalk(this, (token, loginError) -> {
                    if (loginError != null) {
                        Log.e("loginWithKakaoTalk", "로그인 실패", loginError);
                        Toast.makeText(this, "카카오톡 로그인 실패", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("loginWithKakaoTalk", "로그인 성공" + token.getAccessToken());
                        Toast.makeText(this, "카카오톡 로그인 성공", Toast.LENGTH_SHORT).show();

                        // @deprecated : 마이페이지 구현 후 거기에 적용
                        // @brief : 로그아웃 코드
                        /*UserApiClient.getInstance().logout(error -> {
                            if (error != null) {
                                Log.e(TAG, "로그아웃 실패, SDK에서 토큰 삭제됨", error);
                            }else{
                                Log.e(TAG, "로그아웃 성공, SDK에서 토큰 삭제됨");
                            }
                        });*/

                        // @brief : 사용자 정보 요청
                        UserApiClient.getInstance().me((user, meError) -> {
                            if (meError != null) {
                                Log.e("UserApiClient", "사용자 정보 요청 실패", meError);
                            } else {
                                Log.i("UserApiClient", user.toString());
                                Log.i("UserApiClient", "사용자 정보 요청 성공" +
                                        "\n회원번호: " + user.getId() +
                                        "\n이메일: " + user.getKakaoAccount().getEmail());
                            }
                            return null;
                        });
                        // @brief : 로그인 성공했으므로 activity 변경
//                        goMainActivity();
                    }
                    return null;
                });
                break;
            case R.id.google:
//                GoogleLogin googleLogin = new GoogleLogin(this);
                initGoogle();
                signInGoogle();

                break;
            case R.id.btn_email_signup:
                intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void goMainActivity(){
        // @brief : 로그인 성공 시 MainActivity로 이동
        Intent goMain = new Intent(this, MainActivity.class);
        startActivity(goMain);
    }

    private void init() {
        edit_email = findViewById(R.id.email);
        edit_pw = findViewById(R.id.pw);

        // @brief : kakao 네이티브 앱키로 초기화
        String kakao_app_key = getResources().getString(R.string.kakao_app_key);
        KakaoSdk.init(this, kakao_app_key);
    }

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
        Log.d("firebaseAuthWithGoogle", acct.getId());
        String acc_idToken = acct.getIdToken();
        // @brief : 구글로부터 로그인된 사용자의 정보(Credentail)을 얻어옴
        AuthCredential credential = GoogleAuthProvider.getCredential(acc_idToken, null);
        // @brief : credentail를 사용하여 Firebase의 auth를 실행
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // @brief : 로그인 성공 시
                        Log.d("firebaseAuthWithGoogle", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(), "구글 로그인 성공", Toast.LENGTH_LONG).show();
                        goMainActivity();
                    } else {
                        // @brief : 로그인 실패 시
                        Log.w("firebaseAuthWithGoogle", "signInWithCredential:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "구글 로그인 실패", Toast.LENGTH_LONG).show();
                    }
                });
    }
}