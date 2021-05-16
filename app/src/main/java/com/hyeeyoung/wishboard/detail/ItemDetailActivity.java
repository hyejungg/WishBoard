package com.hyeeyoung.wishboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ItemDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ImageButton back = findViewById(R.id.back);

        // @brief : back 버튼 클릭 시 이전 화면으로 돌아가기
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onBackPressed();
                // @brief : 오른쪽 -> 왼쪽으로 화면 전환
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
            }
        });
    }
}