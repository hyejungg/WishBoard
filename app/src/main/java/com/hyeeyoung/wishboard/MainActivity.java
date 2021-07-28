package com.hyeeyoung.wishboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hyeeyoung.wishboard.folder.FolderFragment;
import com.hyeeyoung.wishboard.add.NewItemFragment;
import com.hyeeyoung.wishboard.home.HomeFragment;
import com.hyeeyoung.wishboard.noti.NotiFragment;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;

public class MainActivity extends AppCompatActivity {
//    private static final String USER_ID = "USER_ID";
    private static final String TAG = "메인 화면";
    private String user_id;

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragManager;
    private FragmentTransaction fragTransaction;

    private NewItemFragment newItemFragment;
    private FolderFragment folderFragment;
    private HomeFragment homeFragment;
    private NotiFragment notiFragment;
    private MyFragment myFragment;

    @Override
    protected void onStart() {
        super.onStart();
        init();

        // @ brief : 푸시알림을 클릭해서 메인화면을 들어올 경우 flag 값(3 : 알림 프래그먼트) 받아오고, 그렇지 않은 경우 default flag 값(0 : 홈 프래그먼트)으로 설정한다.
        int flag = 0;
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            flag = extras.getInt("flag");
        }
        //int flag = intent.getIntExtra("flag", 0);
        Log.i(TAG, "onStart: " + flag);
        // @brief 초기 프래그먼트 화면 지정
        setFrag(flag);
    }
    public void init(){
        newItemFragment = new NewItemFragment();
        folderFragment = new FolderFragment();
        homeFragment = new HomeFragment();
        notiFragment = new NotiFragment();
        myFragment = new MyFragment();

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        setFrag(0);
                        break;
                    case R.id.new_item:
                        setFrag(1);
                        break;
                    case R.id.folder:
                        fragTransaction.add(folderFragment, "FolderFragment");
                        setFrag(2);
                        break;
                    case R.id.noti:
                        setFrag(3);
                        break;
                    case R.id.my:
                        setFrag(4);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SaveSharedPreferences.getUserId(this).length() != 0)
            user_id = SaveSharedPreferences.getUserId(this);

        if(SaveSharedPreferences.getFCMToken(this).length() == 0){
            // @brief : 파이어베이스 토큰 조회 코드
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();
                            SaveSharedPreferences.setFCMToken(MainActivity.this, token);
                            Log.i(TAG, "onComplete : " + SaveSharedPreferences.getFCMToken(MainActivity.this));
                            // Log and toast
                            //String msg = getString(R.string.msg_token_fmt, token);
                            //Log.d(TAG, msg);
                            //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        //       FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//                        SaveSharedPreferences.setFCMToken(MainActivity.this, token);
//                        Log.i(TAG, "onComplete : " + SaveSharedPreferences.getFCMToken(MainActivity.this));
//                        Log.i(TAG, token);
//                    }
//                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * @brief : NewFolderAticity.java에서 finish()시 MainActivity로 와서 onAticityResult FolderFragment로 보냄
         * @see : lifecycle에 따라 동작하도록 설정
         *       참고사이트 : https://stickyny.tistory.com/86
         */
        FolderFragment folderFragment = (FolderFragment) getSupportFragmentManager().findFragmentByTag("FolderFragment");
        folderFragment.onActivityResult(requestCode, resultCode, data);
    }

    // @param 초기 프래그먼트 교체 메서드
    private void setFrag(int n) {
        fragManager = getSupportFragmentManager();
        fragTransaction = fragManager.beginTransaction();
        switch (n) {
            case 0:
                fragTransaction.replace(R.id.main_frame, homeFragment).commit();
                break;
            case 1:
                fragTransaction.replace(R.id.main_frame, newItemFragment).commit();
                break;
            case 2:
                fragTransaction.replace(R.id.main_frame, folderFragment).commit();
                break;
            case 3:
                fragTransaction.replace(R.id.main_frame, notiFragment).commit();
                break;
            case 4:
                fragTransaction.replace(R.id.main_frame, myFragment).commit();
                break;
        }
    }
}