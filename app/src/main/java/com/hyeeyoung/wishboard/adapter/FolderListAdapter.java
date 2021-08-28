package com.hyeeyoung.wishboard.adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.FolderItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.CustomViewHolder> {
    private static final String TAG = "폴더리스트어댑터";

    private int lastCheckedPosition = -1;
    private ArrayList<FolderItem> folderList;

    // @parma : 폴더리스트 내 폴더id와 폴더명 정보를 아이템 등록, 수정 시 사용 하므로 f_* 이름으로 표기
    private String f_id,  f_name;

    // @param : 폴더이미지 사진
    private int default_folder_image = R.mipmap.ic_main_round;

    // @brief : FolderListAdapter.java 내 Radio btn click 시 해당 정보를 FolderListActivity.java로 전달하기 위해 listener 생성
    private FolderListAdapter.OnRadioClickListener listener;

    public interface OnRadioClickListener {
        void onRadioClick(String f_id, String f_name);
    }
    public void setOnRadioClickListener(FolderListAdapter.OnRadioClickListener listener) {
        this.listener = listener;
    }

    public FolderListAdapter(ArrayList<FolderItem> data) {
        this.folderList = data;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout layout;
        protected ImageView folder_image;
        protected TextView folder_name;
        protected RadioButton btn_radio;
        public CustomViewHolder(View view) {
            super(view);
            this.layout = view.findViewById(R.id.layout);
            this.folder_image = view.findViewById(R.id.folder_image);
            this.folder_name = view.findViewById(R.id.folder_name);
            this.btn_radio = (RadioButton)view.findViewById(R.id.btn_radio);

            // @brief : 리스트 또는 버튼 클릭 시 radio 1개만 활성화 되도록
            layout.setOnClickListener(view1 -> {
                lastCheckedPosition = getBindingAdapterPosition(); // @brief : 현재 버튼의 위치
                btn_radio.setChecked(true);
                notifyDataSetChanged();
                f_id = folderList.get(lastCheckedPosition).getFolder_id();
                f_name = folderList.get(lastCheckedPosition).getFolder_name(); // @brief : 클릭한 폴더명

                // @brief : FolderListAdatper.java의 폴더명을 FolderListActivity.java로 전달하기 위해 listener로 연결
                if(listener != null)
                    listener.onRadioClick(f_id, f_name);
                Log.i(TAG, f_id + " / " + f_name); //@deprecated
            });
            btn_radio.setOnClickListener(v -> {
                lastCheckedPosition = getBindingAdapterPosition(); // @brief : 현재 버튼의 위치
                btn_radio.setChecked(true);
                notifyDataSetChanged();
                f_id = folderList.get(lastCheckedPosition).getFolder_id();
                f_name = folderList.get(lastCheckedPosition).getFolder_name(); // @brief : 클릭한 폴더명

                // @brief : FolderListAdatper.java의 폴더명을 FolderListActivity.java로 전달하기 위해 listener로 연결
                if(listener != null)
                    listener.onRadioClick(f_id, f_name);
                Log.i(TAG, f_id + " / " + f_name); //@deprecated
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

        int item_count = item.getItem_count();
//        holder.folder_image.setImageResource(folder_images[item.getFolder_image()]);

        // @param : 폴더 내 아이템이 있는지, 없는지에 따라 item_image로 folder_image를 보여줄지, 기본 image를 보여줄지 선택
        if(item_count != 0) {
            try {
                Picasso.get().load(item.getFolder_image()).into(holder.folder_image); // @brief : 가져온 이미지경로값으로 이미지뷰 디스플레이
            } catch (IllegalArgumentException i) {
                Log.d("checkings", "아이템 사진 없음");
            }
        }
        else{
            holder.folder_image.setImageResource(default_folder_image);
        }

        holder.folder_name.setText(item.getFolder_name());
        holder.btn_radio.setChecked(lastCheckedPosition == position);
    }

    @Override
    public int getItemCount() {
        return (null != folderList ? folderList.size() : 0);
    }
}