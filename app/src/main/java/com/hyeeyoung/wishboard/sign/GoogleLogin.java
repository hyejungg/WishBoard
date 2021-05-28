//package com.hyeeyoung.wishboard.sign;
//
//import android.content.Context;
//
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.firebase.auth.FirebaseAuth;
//import com.hyeeyoung.wishboard.R;
//
//import static android.provider.Settings.System.getString;
//
//public class GoogleLogin {
//    Context context;
//
//    GoogleLogin(){}
//
//    GoogleLogin(Context context){
//        context = this.context;
//    }
//
//
//    // @params : google 로그인 관련 변수
//    private GoogleSignInClient googleSignInClient;
//    private String TAG="mainTag";
//    private FirebaseAuth mAuth;
//    private int RC_SIGN_IN=1001; // @ brief : 성공코드 ...임의로 설정함
//
//    protected void initGoogle(){
//        // @brief : 구글 로그인 시작 관련 설정
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) // 기본 로그인 방식 사용
//                .requestIdToken(getString(R.string.default_web_client_id)) // 필수사항, 사용자의 식별값(token)을 사용
//                .requestEmail() // 사용자의 이메일을 사용(App이 구글에게 요청)
//                .build();
//        // @brief : 내 앱에서 구글의 계정을 가져다 쓸 예정
//        googleSignInClient = GoogleSignIn.getClient(context, gso);
//
//        // @brief : Firebase auth 초기화
//        // Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();
//    }
//}
