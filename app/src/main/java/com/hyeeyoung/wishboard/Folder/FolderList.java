package com.hyeeyoung.wishboard.Folder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.FolderAdapter;
import com.hyeeyoung.wishboard.adapter.FolderListAdapter;
import com.hyeeyoung.wishboard.item.FolderListItem;
import com.hyeeyoung.wishboard.item.FoldersItem;

import java.util.ArrayList;

public class FolderList extends AppCompatActivity {
    RecyclerView recyclerView;
    FolderListAdapter adapter;
    private ArrayList<FolderListItem> foldersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);

        ImageView folder_image = findViewById(R.id.folder_image);
        TextView folder_name = findViewById(R.id.folder_name);
        ImageView checkbox = findViewById(R.id.checkbox);

        recyclerView = findViewById(R.id.recyclerview_folders_selected);

        foldersList = new ArrayList<>();
        adapter = new FolderListAdapter(foldersList);
        recyclerView.setAdapter(adapter);

        // @deprecated : add()
        addItem(R.mipmap.ic_launcher, "name1", 1);
        addItem(R.mipmap.ic_launcher, "name2", 0);
        addItem(R.mipmap.ic_launcher, "name3", 0);
        addItem(R.mipmap.ic_launcher, "name4", 0);
        addItem(R.mipmap.ic_launcher, "name5", 1);

        adapter.notifyDataSetChanged();

    }
    // @deprecated
    private void addItem(int folder_image, String folder_name, int checkbox) {
        FolderListItem item = new FolderListItem();
        item.setFolderImage(folder_image);
        item.setFolderName(folder_name);
        item.setCheckbox(checkbox);
        foldersList.add(item);
    }
}