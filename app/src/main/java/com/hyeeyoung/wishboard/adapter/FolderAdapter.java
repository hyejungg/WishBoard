package com.hyeeyoung.wishboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.detail.FolderDetailActivity;
import com.hyeeyoung.wishboard.detail.ItemDetailActivity;
import com.hyeeyoung.wishboard.folder.FolderMoreActivity;
import com.hyeeyoung.wishboard.model.FolderItem;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.CustomViewHolder> {
    private static final String TAG = "폴더어댑터";
    private ArrayList<FolderItem> folderList;
    private int[] folder_images = {R.mipmap.ic_main_round, R.drawable.bag, R.drawable.sofa, R.drawable.shoes, R.drawable.twinkle,
            R.drawable.ring, R.drawable.orange, R.drawable.clothes, R.drawable.camera, R.drawable.bubble};

    private Intent intent;
    protected Context context;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView folder_image;
        protected TextView folder_name;
        protected TextView item_count;
        protected ImageButton more_folder;
        protected ConstraintLayout item;


        public CustomViewHolder(View view) {
            super(view);
            this.folder_image = (ImageView) view.findViewById(R.id.folder_item);
            this.folder_name = (TextView) view.findViewById(R.id.folder_name);
            this.item_count = (TextView) view.findViewById(R.id.item_count);
            this.more_folder = (ImageButton) view.findViewById(R.id.more_folder);
            this.item = (ConstraintLayout) view.findViewById(R.id.item);
        }
    }
    public FolderAdapter(ArrayList<FolderItem> data) {
        this.folderList = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.folder_item, viewGroup, false);
        context = view.getContext();
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        FolderItem item = folderList.get(position);

        holder.folder_image.setImageResource(folder_images[item.getFolder_image()]);
        holder.folder_name.setText(item.getFolder_name());
        holder.item_count.setText(item.getItem_count()+"");

        holder.more_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "더보기 클릭");
                intent = new Intent(view.getContext(), FolderMoreActivity.class);
                Bundle bd = new Bundle();
                Log.i(TAG, String.valueOf(folderList));
                Log.i(TAG, String.valueOf(position));
                bd.putParcelableArrayList("folderList", folderList);
                bd.putInt("position", position); //@TODO : 0
                intent.putExtra("bundle", bd);
                view.getContext().startActivity(intent);
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "폴더 상세조회 이동");
                intent = new Intent(v.getContext(), FolderDetailActivity.class);
//                intent.putExtra("folder_id",item.getFolder_id()+"");
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderList.size(); //@see : 수정 필요
    }
}
