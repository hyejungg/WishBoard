package com.hyeeyoung.wishboard.adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.detail.FolderDetailActivity;
import com.hyeeyoung.wishboard.folder.MoreFolderDialog;
import com.hyeeyoung.wishboard.model.FolderItem;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.CustomViewHolder> {
    private static final String TAG = "폴더어댑터";
    private ArrayList<FolderItem> folderList;

    // @param : 폴더이미지 사진
    private int[] folder_images = {R.mipmap.ic_main_round, R.drawable.bag, R.drawable.sofa, R.drawable.shoes, R.drawable.twinkle,
            R.drawable.ring, R.drawable.orange, R.drawable.clothes, R.drawable.camera, R.drawable.bubble};

    private Intent intent;
    protected Context context;
    private String user_id = "", folder_id = "";

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
        }
    }
    public FolderAdapter(ArrayList<FolderItem> data, String user_id) {
        this.folderList = data;
        this.user_id = user_id;
        notifyDataSetChanged(); // @brief : 데이터 변경사항 반영
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

        // @brief : 더보기 버튼 클릭 시 더보기 diolog 생성
        holder.more_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                folder_id = item.getFolder_id();
                Log.i(TAG, "더보기 클릭");

                // @brief : 다이얼로그에 전달할 값 bundle 담기
                Bundle args = new Bundle();
                args.putString("user_id", user_id);
                args.putString("folder_id", folder_id);
                // @brief : 다이얼로그 생성하여 전달 후 보여줌
                MoreFolderDialog mfd = MoreFolderDialog.getInstance();
                mfd.setArguments(args);
                mfd.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(),
                        MoreFolderDialog.TAG_EVENT_DIALOG);
            }
        });

        // @brief : 아이템 버튼 클릭 시 상세조회로 이동
        // @see : item view 클릭 시 넘어가도록 할 경우 더보기 버튼 클릭이 어려워서 folder_image 클릭 시로 변경
        holder.folder_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "폴더 상세조회 이동");
                intent = new Intent(v.getContext(), FolderDetailActivity.class);
                Log.i(TAG, "folderItem send : " + item); //@deprecated 확인용
                Log.i(TAG, "folder_id send : " + item.getFolder_id()); //@deprecated 확인용
                intent.putExtra("FolderItem", item);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != folderList ? folderList.size() : 0);
    }
}
