package com.hyeeyoung.wishboard.folder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.CartAdapter;
import com.hyeeyoung.wishboard.adapter.FolderAdapter;
import com.hyeeyoung.wishboard.adapter.FolderListAdapter;
import com.hyeeyoung.wishboard.model.FolderListItem;
import com.hyeeyoung.wishboard.model.FolderItem;

import java.util.ArrayList;

public class FolderListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FolderListAdapter adapter;
    private ArrayList<FolderListItem> foldersList;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);

        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerview_folders_selected);
        foldersList = new ArrayList<>();
        adapter = new FolderListAdapter(foldersList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // @brief : 아이템 간 구분선 넣기
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        // @deprecated : add()
        addItem(R.drawable.sample, "옷", true);
        addItem(R.drawable.sample, "상의", true);
        addItem(R.drawable.sample, "아우터", false);
        addItem(R.drawable.sample, "하의", false);
        addItem(R.drawable.sample, "가방", false);
        addItem(R.drawable.sample, "신발", false);
        addItem(R.drawable.sample, "장신구", false);
        addItem(R.drawable.sample, "모자", false);
        adapter.notifyDataSetChanged();
    }

    public void onClick(View v){
        switch (v.getId()){
            // @brief : back 버튼 클릭 시 이전 화면으로 돌아가기
            case R.id.back :
                onBackPressed();
                // @brief : 오른쪽 -> 왼쪽으로 화면 전환
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        }
    }
    // @deprecated
    private void addItem(int folder_image, String folder_name, boolean checkbox) {
        FolderListItem item = new FolderListItem();
        item.setFolderImage(folder_image);
        item.setFolderName(folder_name);
        item.setCheckboxState(checkbox);
        foldersList.add(item);
    }

}