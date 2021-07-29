package com.hyeeyoung.wishboard.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.FolderAdapter;
import com.hyeeyoung.wishboard.adapter.ItemAdapter;
import com.hyeeyoung.wishboard.model.FolderItem;
import com.hyeeyoung.wishboard.model.WishItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FolderDetailActivity extends AppCompatActivity {
    private static final String TAG = "폴더상세조회";
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ArrayList<WishItem> wish_list;
    private GridLayoutManager gridLayoutManager;

    private ImageButton back, search, more;
    private TextView title;

    private String user_id, folder_id, folder_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_detail);

        // @brief : intent로 전달받은 값 확인
        Intent intent = getIntent();
        if(intent != null){
            FolderItem item = intent.getParcelableExtra("FolderItem");
            user_id = item.getUser_id();
            folder_id = item.getFolder_id();
            folder_name = item.getFolder_name();

            //@brief : user_id의 경우, 가져오지 못할 시 SharedPreferences 값 이용
            if(user_id == null){
                if (SaveSharedPreferences.getUserId(this).length() != 0) {
                    user_id = SaveSharedPreferences.getUserId(this);
                }else{ //예외처리
                    user_id = null;
                }
            }

        }else{ //예외처리
            folder_id = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "user_id = " + user_id + " folder_id = " + folder_id); // @deprecated : test용
        selectFolderItem(user_id, folder_id);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // @TODO : 폴더프레그멘트로 갔으면 좋겠는데 홈프레그먼트로 이동함 ... 수정 필요
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void selectFolderItem(String user_id, String folder_id) {
        IRemoteService remoteService = ServiceGenerator.createService(IRemoteService.class);
        Call<ArrayList<WishItem>> call = remoteService.selectFolderItemInfo(user_id, folder_id);
        call.enqueue(new Callback<ArrayList<WishItem>>() {
            @Override
            public void onResponse(Call<ArrayList<WishItem>> call, Response<ArrayList<WishItem>> response) {
                wish_list = response.body(); // @brief : body()는, json 으로 컨버팅되어 객체에 담겨 리턴됨.

                // @brief : 가져온 아이템이 없는 경우
                if (wish_list == null) {
                    wish_list = new ArrayList<>(); // @brief : 아이탬 배열 초기화
                }

                // @brief : 서버연결 성공한 경우
                if(response.isSuccessful()){
                    if (wish_list.size() > 0) { // @brief : 가져온 아이템이 하나 이상인 경우
                        Log.i(TAG, "Retrofit 통신 성공");
                        Log.i(TAG, wish_list+""); // @deprecated : 테스트용
                        init(); // @brief : onCreateView 메서드에서 해당 위치로 옮김
                    }
                } else { // @brief : 통신에 실패한 경우
                    Log.e(TAG, "Retrofit 통신 실패");
                    Log.i(TAG, response.message());
                    init(); // @brief : onCreateView 메서드에서 해당 위치로 옮김
                }
            }

            @Override
            public void onFailure(Call<ArrayList<WishItem>> call, Throwable t) {
                // @brief : 통식 실패 ()시 callback (예외 발생, 인터넷 끊김 등의 시스템적 이유로 실패)
                Log.e(TAG, "서버 연결 실패");
                Log.e(TAG, String.valueOf(t.fillInStackTrace()));
                init();
            }
        });
    }


    private void init(){
        // @brief : 뷰 및 어댑터 초기화
        recyclerView = findViewById(R.id.recyclerview_folders_detail);
        adapter = new ItemAdapter(wish_list, user_id);
        recyclerView.setAdapter(adapter);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        more = findViewById(R.id.more);
        title = findViewById(R.id.folder_name);
        title.setText(folder_name);
    }

    public void onClick(View v){
        switch (v.getId()){
            // @brief : back 버튼 클릭 시 이전 화면으로 돌아가기
            case R.id.back:
                onBackPressed();
                // @brief : 오른쪽 -> 왼쪽으로 화면 전환
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
                onStop();
                break;

            case R.id.search:
                break;

            case R.id.more:
                break;
        }
    }
}