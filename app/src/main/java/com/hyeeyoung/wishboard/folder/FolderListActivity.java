package com.hyeeyoung.wishboard.folder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.FolderListAdapter;
import com.hyeeyoung.wishboard.model.FolderItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FolderListActivity extends AppCompatActivity {
    private static final String TAG = "폴더리스트";

    private RecyclerView recyclerView;
    private FolderListAdapter adapter;
    private ArrayList<FolderItem> foldersList;
    private LinearLayoutManager linearLayoutManager;

    private String user_id = "";

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);

        // @brief : user_id를 가져옴
        if(SaveSharedPreferences.getUserId(this).length() != 0){
            user_id = SaveSharedPreferences.getUserId(this);
            Log.i(TAG, "user_id = " + user_id); // @deprecated : test용
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectFolderListInfo(user_id);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * @brief : 직접등록 및 아이템 상세화면에서 아이템 수정 시 폴더 선택에 보여질 폴더리스트 요청
     * @param user_id
     */
    private void selectFolderListInfo(String user_id){
        IRemoteService remoteService = ServiceGenerator.createService(IRemoteService.class);
        Call<ArrayList<FolderItem>> call = remoteService.selectFolderListInfo(user_id);
        call.enqueue(new Callback<ArrayList<FolderItem>>() {
            @Override
            public void onResponse(Call<ArrayList<FolderItem>> call, Response<ArrayList<FolderItem>> response) {
                foldersList = response.body();

                // @brief : 가져온 폴더 정보가 없는 경우
                if (foldersList == null) {
                    foldersList = new ArrayList<>(); // @brief : 아이탬 배열 초기화
                }

                // @brief : 서버연결 성공한 경우
                if(response.isSuccessful()){
                    if (foldersList.size() > 0) { // @brief : 가져온 폴더 정보가 하나 이상인 경우
                        Log.i(TAG, "Retrofit 통신 성공");
                        Log.i(TAG, foldersList +""); // @deprecated : 테스트용
                        init(); // @brief : onCreateView 메서드에서 해당 위치로 옮김
                    }
                } else { // @brief : 통신에 실패한 경우
                    Log.e(TAG, "Retrofit 통신 실패");
                    Log.i(TAG, response.message());
                    init(); // @brief : onCreateView 메서드에서 해당 위치로 옮김
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FolderItem>> call, Throwable t) {
                // @brief : 통식 실패 ()시 callback (예외 발생, 인터넷 끊김 등의 시스템적 이유로 실패)
                Log.e(TAG, "서버 연결 실패");
                init();
            }
        });
    }

    private void init() {
        // @brief : 폴더 아이템 뷰 및 어댑터 초기화
        recyclerView = findViewById(R.id.recyclerview_folders_selected);
        adapter = new FolderListAdapter(foldersList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // @brief : 아이템 간 구분선 넣기
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter.setOnRadioClickListener((f_id, f_name) -> {
            if (f_id != null && f_name != null) {
                Log.i(TAG, f_id + " / " + f_name);

                // @brief : 폴더명을 이전 화면으로 전달
                intent = new Intent();
                intent.putExtra("folder_id", f_id);
                intent.putExtra("folder_name", f_name);
                FolderListActivity.this.setResult(1004, intent);
            } else
                Log.i(TAG, "f_name은 null");
        });
    }

    public void onClick(View v){
        switch (v.getId()){
            // @brief : back 버튼 클릭 시 이전 화면으로 돌아가기
            case R.id.back :
                // @brief : 폴더명을 이전 화면으로 전달
                onBackPressed();
                // @brief : 오른쪽 -> 왼쪽으로 화면 전환
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        }
    }

}