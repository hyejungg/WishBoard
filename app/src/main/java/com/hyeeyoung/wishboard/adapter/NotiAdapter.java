package com.hyeeyoung.wishboard.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.util.Util;
import com.hyeeyoung.wishboard.model.NotiItem;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.CustomViewHolder> {
    private ArrayList<NotiItem> notiList;
    private int noti_layout_color; // @brief : 읽지 않은 알림의 배경에 적용할 색상
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView item_image;
        protected TextView noti_title;
        protected TextView noti_date;
        protected RelativeLayout noti_layout;

        public CustomViewHolder(View view) {
            super(view);
            this.item_image = view.findViewById(R.id.item_image);
            this.noti_title = view.findViewById(R.id.noti_title);
            this.noti_date = view.findViewById(R.id.noti_date);
            this.noti_layout = view.findViewById(R.id.noti_layout);
        }
    }
    public NotiAdapter(ArrayList<NotiItem> data) {
        this.notiList = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.noti_item, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        noti_layout_color = view.getResources().getColor(R.color.pastelGreen);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        NotiItem item = notiList.get(position);

        /**
         * @brief : 아이템 이미지를 화면에 보여준다.
         */
        try {
            Picasso.get().load(item.getItem_image()).into(viewholder.item_image); // @brief : 가져온 이미지경로값으로 이미지뷰 디스플레이
        } catch (IllegalArgumentException i) {
            Log.d("checkings", "아이템 사진 없음");
        }

        // @brief : 읽지 않은 알림인 경우 배경색을 설정
        if(item.getIs_read() == "0")
            viewholder.noti_layout.setBackgroundColor(noti_layout_color);
        viewholder.noti_title.setText("[" + item.getItem_notification_type() + "] " + item.getItem_name());
        viewholder.noti_date.setText(Util.beforeTime(item.getItem_notification_date()));
    }
    @Override
    public int getItemCount() {
        return (null != notiList ? notiList.size() : 0);
    }
}
