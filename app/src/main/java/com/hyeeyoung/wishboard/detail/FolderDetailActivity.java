package com.hyeeyoung.wishboard.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.FolderAdapter;
import com.hyeeyoung.wishboard.adapter.ItemAdapter;
import com.hyeeyoung.wishboard.model.FolderItem;
import com.hyeeyoung.wishboard.model.WishItem;

import java.util.ArrayList;

public class FolderDetailActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ItemAdapter adapter;
    ArrayList<WishItem> wish_list;
    GridLayoutManager gridLayoutManager;

    ImageButton back, search, more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_detail);

        init();
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
        // @brief : 뷰 및 어댑터 초기화
        recyclerView = findViewById(R.id.recyclerview_folders_detail);
        wish_list = new ArrayList<>();
        adapter = new ItemAdapter(wish_list, "0");
        recyclerView.setAdapter(adapter);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        more = findViewById(R.id.more);

        // @deprecated : add()
        // @TODO : 서버통신 이후 init 위치 변경 필요 지금 이상태에서 addItem() 없애면 이전 값들은 저장이 안됨
        addItem(R.drawable.sample, "item1", "30000");
        addItem(R.drawable.sample, "item2", "26000");
        addItem(R.drawable.sample, "item3", "29000");
        addItem(R.drawable.sample, "item4", "30000");
        addItem(R.drawable.sample, "item5", "56000");
        addItem(R.drawable.sample, "item6", "45000");
        addItem(R.drawable.sample, "item7", "40000");
        addItem(R.drawable.sample, "item8", "31000");
        addItem(R.drawable.sample, "item9", "30000");
        addItem(R.drawable.sample, "item10", "20000");
        addItem(R.drawable.sample, "item110", "242000");

        adapter.notifyDataSetChanged();
    }

    // @deprecated
    private void addItem(int item_image, String item_name, String item_price) {
        WishItem item = new WishItem();
        item.setItem_image(item_image);
        item.setItem_name(item_name);
        item.setItem_price(item_price);
        wish_list.add(item);
    }

    public void onClick(View v){
        switch (v.getId()){
            // @brief : back 버튼 클릭 시 이전 화면으로 돌아가기
            case R.id.back:
                onBackPressed();
                // @brief : 오른쪽 -> 왼쪽으로 화면 전환
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
                onStop(); // @brief : 액티비티 종료 -> 장바구니 update 처리
                break;
            case R.id.search:
                break;
            case R.id.more:
                break;
        }
    }
}