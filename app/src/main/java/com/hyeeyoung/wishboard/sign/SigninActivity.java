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
import com.hyeeyoung.wishboard.model.UserItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {
    private Intent intent;

    // @params : 서버와 연결하여 DB에 등록할 정보를 담는 변수
    private String email;
    private String pw;
    private String token;
    private UserItem user_item = new UserItem();

    private EditText edit_email;
    private EditText edit_pw;

    // @params : google 로그인 관련 변수
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1001; // @ brief : 성공코드 ...임의로 설정함
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();

        // @TODO : goMainActivity() 수정 필요
        //goMainActivity(user != null);

    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * @see : onStart()될 때마다 updateKakaoLogin()을 해두어서 해당 기기에서 로그인한 기록이 있는 경우,
         *        자동으로 로그인 정보를 가져옴
         * */
//        updateKakaoLogin();
        /**
         * @see : onStart()될 때마다 updateGoogleLogin()을 해두어서 해당 기기에서 로그인한 기록이 있는 경우,
         *        자동으로 로그인 정보를 가져옴
         * */
        mAuth = FirebaseAuth.getInstance();
//        updateGoogleLogin();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:
                email = edit_email.getText().toString();
                pw = edit_pw.getText().toString();

                // @TODO: 해야 할 일
                //  @brief : 서버와 연결하여 로그인 처리
                signInWish(email, pw);

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
    private void goMainActivity(boolean isLogin) {
        // @brief : 로그인 성공 시 MainActivity로 이동
        if(isLogin){
            Intent goMain = new Intent(this, MainActivity.class);
            Toast.makeText(this, "로그인이 정상적으로 수행됐습니다.", Toast.LENGTH_SHORT).show();
            startActivity(goMain);
        }

    }
    /****************************************
     * @params : wishboard 로그인 관련 함수
     **/
    private void signInWish(String email, String pw){
        user_item.setEmail(email);
        user_item.setPassword(pw);

        IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
        Call<ResponseBody> call = remote_service.signInUser(user_item);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    // @brief : 정상적으로 통신 성공한 경우
                    String seq = null;
                    try{
                        seq = response.body().string();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Log.i("Wish 로그인", "성공");
                    goMainActivity(true);
                }else{
                    // @brief : 통신에 실패한 경우
                    Log.e("Wish 로그인", "오류");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // @brief : 통신 실패 시 callback
                Log.e("Wish 로그인", "서버 연결 실패");
                t.fillInStackTrace();
            }
        });
    }

    /****************************************
     * @params : kakao 로그인 관련 함수
     **/
    private void signInKakao() {
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
    Function2<OAuthToken, Throwable, Unit> callback = (oAuthToken, throwable) -> {
        if (oAuthToken != null) {
            Log.i("카카오 로그인", "성공");
        }
        if (throwable != null) {
            Log.i("카카오 로그인", "실패");
            Log.e("signInKakao()", throwable.getLocalizedMessage());
        }
        updateKakaoLogin();
        return null;
    };

    // @brief : 로그인 여부를 확인 및 update UI
    private void updateKakaoLogin() {
        UserApiClient.getInstance().me((user, throwable) -> {
            if (user != null) {
                // @brief : 로그인 성공
                Log.i("UserApiClient", user.toString());

                // @brief : 로그인한 유저의 email주소와 token 값 가져오기. pw는 제공 X
                email = user.getKakaoAccount().getEmail();
                token = String.valueOf(user.getId());
                pw = null;

                // @brief : 로그인 성공했으므로 메인엑티비티로 이동
                goMainActivity(user != null);
            } else {
                // @brief : 로그인 실패

            }
            return null;
        });
    }

    // @brief : 로그아웃
    private void singOutKakao() {
        UserApiClient.getInstance().logout((throwable) -> {
            if (throwable != null) {
                // @brief : 로그아웃 실패
                Toast.makeText(this, "카카오 로그아웃을 실패했습니다.", Toast.LENGTH_SHORT).show();
            } else {
                // @brief : 로그아웃 성공
                Toast.makeText(this, "카카오 로그아웃이 정상적으로 수행됐습니다.", Toast.LENGTH_SHORT).show();
            }
            return null;
        });
        // @brief : 카카오 연결 끊기
        UserApiClient.getInstance().logout((throwable) -> {
            if (throwable != null) {
                // @brief : 연결 끊기 실패
                Log.e("kakaoLogout", "연결 끊기 실패", throwable);
            } else {
                // @brief : 연결 끊기 성공
                Log.e("kakaoLogout", "연결 끊기 성공. SDK에서 토큰 삭제");
            }
            return null;
        });
    }

    /**********************************************
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
            if (resultCode == RESULT_OK) {
                try {
                    // @brief : 구글 로그인 성공 했다면 -> task의 정보를 가져옴
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d("firebaseAuthWithGoogle", account.getIdToken());
                    token = account.getIdToken();
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
        AuthCredential credential = GoogleAuthProvider.getCredential(acc_idToken, null);
        // @brief : credentail를 사용하여 Firebase의 auth를 실행하여 signIn ~ 로그인 시도
        /**
         * 사용자가 정상적으로 로그인한 후에 GoogleSignInAccount 개체에서 ID 토큰을 가져와서
         * Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase에 인증.
         */
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // @brief : 로그인 성공 시
                        Log.d("firebaseAuthWithGoogle", "signInWithCredential:success");
                        user = mAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(), "구글 로그인 성공", Toast.LENGTH_LONG).show();
                    } else {
                        // @brief : 로그인 실패 시
                        Log.w("firebaseAuthWithGoogle", "signInWithCredential:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "구글 로그인 실패", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updateGoogleLogin() {
        goMainActivity(mAuth != null);
    }

    // @brief : 로그아웃
    private void singOutGoogle() {
        mAuth.getInstance().signOut();
    }

}