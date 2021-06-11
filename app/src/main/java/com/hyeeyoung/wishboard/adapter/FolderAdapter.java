package com.hyeeyoung.wishboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.FolderItem;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.CustomViewHolder> {
    private ArrayList<FolderItem> folderList;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView folder_image;
        protected TextView folder_name;
        protected TextView item_count;


        public CustomViewHolder(View view) {
            super(view);
            this.folder_image = (ImageView) view.findViewById(R.id.folder_item);
            this.folder_name = (TextView) view.findViewById(R.id.folder_name);
            this.item_count = (TextView) view.findViewById(R.id.item_count);
        }
    }
    public FolderAdapter(ArrayList<FolderItem> data) {
        this.folderList = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.folder_item, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        FolderItem item = folderList.get(position);
        // @see: 카테고리 번호 별 아이템 사진 매칭?
        holder.folder_image.setImageResource(item.getFolder_image());
        holder.folder_name.setText(item.getFolder_name());
        holder.item_count.setText(item.getItem_count()+"");
    }

    @Override
    public int getItemCount() {
        return folderList.size(); //@see : 수정 필요
    }
}
