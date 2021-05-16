package com.hyeeyoung.wishboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.FolderListItem;
import com.hyeeyoung.wishboard.model.FolderItem;

import java.util.ArrayList;

public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.CustomViewHolder> {
    ArrayList<FolderListItem> folderList;

    public FolderListAdapter(ArrayList<FolderListItem> data) {
        this.folderList = data;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView folder_image;
        protected TextView folder_name;
        protected ImageView checkbox;

        public CustomViewHolder(View view) {
            super(view);
            ImageView folder_image = view.findViewById(R.id.folder_image);
            TextView folder_name = view.findViewById(R.id.folder_name);
            ImageView checkbox = view.findViewById(R.id.checkbox);
        }
    }

    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_list_item, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        FolderListItem item = folderList.get(position);
        // @see: 카테고리 번호 별 아이템 사진 매칭?
        holder.folder_image.setImageResource(item.getFolderImage());
        holder.folder_name.setText(item.getFolderName());
        holder.checkbox.setImageResource(item.getFolderImage());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
