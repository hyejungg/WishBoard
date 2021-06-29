package com.hyeeyoung.wishboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.NotiItem;

import java.util.ArrayList;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.CustomViewHolder> {
    private ArrayList<NotiItem> notiList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView item_image;
        protected TextView item_name;
        protected TextView noti_type;
        protected TextView noti_date;

        public CustomViewHolder(View view) {
            super(view);
            this.item_image = (ImageView) view.findViewById(R.id.item_image);
            this.item_name = (TextView) view.findViewById(R.id.item_name);
            this.noti_type = (TextView) view.findViewById(R.id.noti_type);
            this.noti_date = (TextView) view.findViewById(R.id.noti_date);
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

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        NotiItem item = notiList.get(position);
        viewholder.item_image.setImageResource(item.getItem_image());
        viewholder.item_name.setText(item.getItem_name());
        viewholder.noti_type.setText(item.getItem_notification_type());
        viewholder.noti_date.setText(item.getItem_notification_date());
    }
    @Override
    public int getItemCount() {
        return (null != notiList ? notiList.size() : 0);
    }
}
