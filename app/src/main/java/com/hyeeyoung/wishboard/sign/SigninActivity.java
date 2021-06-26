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
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;
import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;

import java.util.Arrays;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
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

    // @params : 서버와 연결 후 response로 받는 정보를 담는 변수
    private UserItem res_user_item = new UserItem();

    private EditText edit_email;
    private EditText edit_pw;

    // @params : google 로그인 관련 변수
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1001; // @ brief : 성공코드 //임의로 설정함
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();
    }

    /**
     * @brief : 로그인 버튼을 클릭했을 때, 동작을 지정
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:
                email = edit_email.getText().toString();
                pw = edit_pw.getText().toString();
                //  @brief : wish board 앱 로그인 진행
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
                // @brief : wish board 앱 회원가입 처리
                intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * @brief : 뷰 초기화
     */
    private void init() {
        // @brief : 뷰 초기화
        edit_email = findViewById(R.id.email);
        edit_pw = findViewById(R.id.pw);
    }

    /**
     * @params : 로그인 성공 시 메인액티비티로 넘기기 위한 함수
     **/
    private void goMainActivity(boolean isLogin) {
        // @brief : 로그인 성공 시 MainActivity로 이동
        if (isLogin) {
            // @brief : 로그인 이후 해당 기기에서 재로그인 없이 이용할 수 있도록 sharedpreferences에 저장
            SaveSharedPreferences.setUserEmail(this, res_user_item.email);
            SaveSharedPreferences.setUserId(this, res_user_item.user_id);

            Intent goMain = new Intent(this, MainActivity.class);
            startActivity(goMain);
        }
    }

    /****************************************
     * @brief : wishboard 로그인 관련 함수
     * @param email 이메일주소
     * @param pw 비밀번호
     * @see : 구글, 카카오 로그인의 경우 -> user_id를 생성 후 가져오기 위해 이 함수 사용
     **/
    private void signInWish(String email, String pw) {
        // @biref : 로그인 요청 시 @body에 담아서 전송 -> user_item에 담아 전송
        user_item.setEmail(email);
        user_item.setPassword(pw);

        IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
        Call<UserItem> call = remote_service.signInUser(user_item);
        call.enqueue(new Callback<UserItem>() {
            @Override
            public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                if (response.isSuccessful()) {
                    // @brief : 정상적으로 통신 성공한 경우
                    try {
                        res_user_item = response.body();
                        // @brief : 로그인 이후 필요한 user_id, email 저장
                        res_user_item.user_id = res_user_item.getUser_id();
                        res_user_item.email = res_user_item.getEmail();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.i("Wish 로그인", "성공" + "\n" + res_user_item.user_id + " / " + res_user_item.email); // @deprecated : 확인용
                    Toast.makeText(SigninActivity.this, "로그인이 정상적으로 수행됐습니다.", Toast.LENGTH_SHORT).show();

                    // @brief : 로그인 성공 시 메인 화면으로 이동
                    goMainActivity(true);
                } else {
                    // @brief : 통신에 실패한 경우
                    Log.e("Wish 로그인", "오류");
                    Toast.makeText(SigninActivity.this, "다시 입력하세요.", Toast.LENGTH_SHORT).show();
                    edit_email.setText("");
                    edit_pw.setText("");
                }
            }

            @Override
            public void onFailure(Call<UserItem> call, Throwable t) {
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
            Log.i("[카카오] 로그인", "성공");
            Toast.makeText(this, "카카오 로그인이 정상적으로 수행됐습니다.", Toast.LENGTH_SHORT).show();
            updateKakaoLogin();
        }
        if (throwable != null) {
            Log.i("[카카오] 로그인", "실패");
            Toast.makeText(this, "카카오 로그인을 실패했습니다.", Toast.LENGTH_SHORT).show();
            Log.e("signInKakao()", throwable.getLocalizedMessage());
        }
        return null;
    };

    // @brief : 로그인 여부를 확인 및 update UI
    private void updateKakaoLogin() {
        UserApiClient.getInstance().me((user, throwable) -> {
            if (user != null) {
                // @brief : 로그인 성공
                Log.i("[카카오] 로그인 정보", user.toString());
                // @brief : 로그인한 유저의 email주소와 token 값 가져오기. pw는 제공 X
                email = user.getKakaoAccount().getEmail();
                Log.i("[카카오] 로그인 정보", email + "");
                // @brief : 로그인한 유저의 email주소를 못가져올 경우 추가 정보 요청하기
                if(email == null){
                    List<String> scopes = Arrays.asList("account_email");
                    LoginClient.getInstance().loginWithNewScopes(this, scopes, (token, error) -> {
                        if(error != null){
                            Log.e("[카카오] 로그인 추가정보 요청", "실패",  error);
                        }else{
                            Log.i("[카카오] 로그인 추가정보 요청", "성공");
                            Log.i("[카카오] 로그인 추가정보 요청", String.valueOf(token.getScopes()));
                            email = token.getScopes().toString();
                        }
                        return null;
                    } );
                }
                /** @brief : email주소를 user db에 넣어 user_id 생성
                  * @see : SignupActivity.save()로 user_id 생성, signInWish()로 바로 로그인 후 홈화면으로 이동
                          현재 Users 테이블의 email 값이 unique로 되어있어 이미 존재하는 경우 db에 추가 안되고 바로 signInWish로 넘어감
                  */
                SignupActivity.save(email, "");
                signInWish(email, "0");
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
                Log.e("[카카오] 로그아웃", "실패", throwable);
                Toast.makeText(this, "카카오 로그아웃을 실패했습니다.", Toast.LENGTH_SHORT).show();
            } else {
                // @brief : 로그아웃 성공
                Log.i("[카카오] 로그아웃", "성공");
                Toast.makeText(this, "카카오 로그아웃이 정상적으로 수행됐습니다.", Toast.LENGTH_SHORT).show();
            }
            return null;
        });
        // @brief : 카카오 연결 끊기
        UserApiClient.getInstance().unlink((throwable) -> {
            if (throwable != null) {
                // @brief : 연결 끊기 실패
                Log.e("[카카오] 로그아웃", "연결 끊기 실패", throwable);
            } else {
                // @brief : 연결 끊기 성공
                Log.i("kakaoLogout", "연결 끊기 성공. SDK에서 토큰 삭제");
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
                    token = account.getIdToken();
                    Log.d("[구글] 로그인", "task의 token 정보\n" + token);
                    email = account.getEmail();
                    Log.d("[구글] 로그인", "task의 이메일 정보\n" + email);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // @brief : 구글 로그인 실패 했다면
                    Log.e("[구글] 로그인", "task 정보 실패", e);
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
                        user = mAuth.getCurrentUser();
                        Log.i("[구글] 로그인", "signInWithCredential:성공");
                        Toast.makeText(getApplicationContext(), "구글 로그인이 정상적으로 성공하였습니다.", Toast.LENGTH_LONG).show();

                        /** @brief : email주소를 user db에 넣어 user_id 생성
                         * @see : SignupActivity.save()로 user_id 생성, signInWish()로 바로 로그인 후 홈화면으로 이동
                                 현재 Users 테이블의 email 값이 unique로 되어있어 이미 존재하는 경우 db에 추가 안되고 바로 signInWish로 넘어감
                         */
                        SignupActivity.save(email, "");
                        signInWish(email, "0");
                    } else {
                        // @brief : 로그인 실패 시
                        Log.e("[구글] 로그인", "signInWithCredential:실패" + task.getException());
                        Toast.makeText(getApplicationContext(), "구글 로그인이 실패하였습니다.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updateGoogleLogin() {
        goMainActivity(mAuth != null);
    }

    // @brief : 로그아웃
//    private void singOutGoogle() {
//        mAuth.getInstance().signOut();
//    }

}