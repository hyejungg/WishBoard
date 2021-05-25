package com.hyeeyoung.wishboard.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.detail.ItemDetailActivity;
import com.hyeeyoung.wishboard.model.WishItem;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CustomViewHolder> {
    private ArrayList<WishItem> wishList;
    private Intent intent;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView item_image;
        protected TextView item_name;
        protected TextView item_price;
        protected ConstraintLayout item;
        public CustomViewHolder(View view) {
            super(view);
            this.item_image = (ImageView) view.findViewById(R.id.item_image);
            this.item_name = (TextView) view.findViewById(R.id.item_name);
            this.item_price = (TextView) view.findViewById(R.id.item_price);
            this.item = (ConstraintLayout) view.findViewById(R.id.item);
        }
    }
    public ItemAdapter(ArrayList<WishItem> data) {
        this.wishList = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.wish_item, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {
        WishItem item = wishList.get(position);
        // viewholder.item_image.setImageDrawable(item.getItem_image()); @ deprecated : 안드로이드 기본 아이콘 대신 실제 상품 이미지로 테스트 할 경우 주석 제거 후 사용
        viewholder.item_image.setImageResource(item.getItem_image());
        viewholder.item_name.setText(item.getItem_name());
        viewholder.item_price.setText(item.getItem_price());

        // @param : 아이템 클릭 시 아이템 상세조회로 이동동
       viewholder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), ItemDetailActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return (null != wishList ? wishList.size() : 0);
    }
}
