package com.hyeeyoung.wishboard;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hyeeyoung.wishboard.Fragment.FolderFragment;
import com.hyeeyoung.wishboard.Fragment.HomeFragment;
import com.hyeeyoung.wishboard.Fragment.MyFragment;
import com.hyeeyoung.wishboard.Fragment.NewItemFragment;
import com.hyeeyoung.wishboard.Fragment.NotiFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragManager;
    private FragmentTransaction fragTransaction;

    private NewItemFragment newItemFragment;
    private FolderFragment folderFragment;
    private HomeFragment homeFragment;
    private NotiFragment notiFragment;
    private MyFragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newItemFragment = new NewItemFragment();
        folderFragment = new FolderFragment();
        homeFragment = new HomeFragment();
        notiFragment = new NotiFragment();
        myFragment = new MyFragment();

        // @brief 초기 프래그먼트 화면 지정
        setFrag(0);

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