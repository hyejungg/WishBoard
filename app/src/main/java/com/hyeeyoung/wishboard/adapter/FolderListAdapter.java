package com.hyeeyoung.wishboard.adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.FolderItem;

import java.util.ArrayList;

public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.CustomViewHolder> {
    private int lastCheckedPosition = -1;
    private ArrayList<FolderItem> folderList;

    // @param : 폴더이미지 사진
    private int[] folder_images = {R.mipmap.ic_main_round, R.drawable.bag, R.drawable.sofa, R.drawable.shoes, R.drawable.twinkle,
            R.drawable.ring, R.drawable.orange, R.drawable.clothes, R.drawable.camera, R.drawable.bubble};

    public FolderListAdapter(ArrayList<FolderItem> data) {
        this.folderList = data;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView folder_image;
        protected TextView folder_name;
        protected RadioButton btn_radio;
        public CustomViewHolder(View view) {
            super(view);
            this.folder_image = view.findViewById(R.id.folder_image);
            this.folder_name = view.findViewById(R.id.folder_name);
            this.btn_radio = (RadioButton)view.findViewById(R.id.btn_radio);

            btn_radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = getBindingAdapterPosition(); // @brief : 현재 버튼의 위치
                    btn_radio.setChecked(true);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_list_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        FolderItem item = folderList.get(position);
        holder.folder_image.setImageResource(folder_images[item.getFolder_image()]);
        holder.folder_name.setText(item.getFolder_name());
        holder.btn_radio.setChecked(lastCheckedPosition == position);
    }

    @Override
    public int getItemCount() {
        return (null != folderList ? folderList.size() : 0);
    }
}