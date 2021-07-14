package com.hyeeyoung.wishboard.folder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.FolderItem;

import java.util.ArrayList;
import java.util.Objects;

public class FolderMoreActivity extends AppCompatActivity {
    private static final String TAG = "폴더더보기";

    ImageButton cancel;
    LinearLayout btn_del;
    LinearLayout btn_upt;

    Intent intent;
    ArrayList<FolderItem> folderList = new ArrayList<>();
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_more);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent get_bd = getIntent();
        if(get_bd != null){
            folderList = Objects.requireNonNull(get_bd.getExtras()).getParcelableArrayList("folderList"); //@TODO : null
            position = get_bd.getExtras().getInt("position"); //@TODO : 0
//            folderList = (ArrayList<FolderItem>) get_intent.getSerializableExtra("folderList");
//            position = get_intent.getIntExtra("position", 0);
            Log.i(TAG, String.valueOf(folderList));
            Log.i(TAG, String.valueOf(position));
        }else{
            Log.i(TAG, "getIntent() null");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        // @brief : 각 아이템 뷰 초기화
        cancel = findViewById(R.id.cancel);
        btn_del = findViewById(R.id.btn_del);
        btn_upt = findViewById(R.id.btn_upt);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.cancel : // @brief : 액티비티 종료
                onStop();
                break;
            case R.id.btn_del : // @brief : 서버와 연결하여 폴더를 삭제
                Log.i(TAG, "폴더삭제 클릭");
                // @TODO : 서버와 연동하여 폴더 삭제 진행
//                folderList.remove(item.getPosition());
                folderList.remove(position);
                break;
            case R.id.btn_upt : // @brief : 서버와 연결하여 폴더명을 수정하고 변경 정보를 display에 보여줌
                Log.i(TAG, "폴더명수정 클릭");
                // @TODO : 서버와 연동하여 폴더명 수정 진행
                break;
        }
    }
}