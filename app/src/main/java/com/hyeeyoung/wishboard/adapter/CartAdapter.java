package com.hyeeyoung.wishboard.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.CartItem;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CustomViewHolder> {
    private ArrayList<CartItem> cartList;

    // @brief : CustomView 내 item click
//    public interface  OnItemClickEventListener{
//        void OnItemClick(View v, int pos, int num);
//    }
//
//    private  OnItemClickEventListener onItemClickEventListener;
//
//    public void setOnItemClickListener(OnItemClickEventListener onItemClickEventListener){
//        this.onItemClickEventListener = onItemClickEventListener;
//    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView item_image;
        protected TextView item_name;
        protected TextView item_price;
        protected TextView item_count;

        protected ImageButton x;
        protected ImageButton plus;
        protected ImageButton minus;

        public CustomViewHolder(View view) {
            super(view);
            this.item_image = view.findViewById(R.id.item_image);
            this.item_name = view.findViewById(R.id.item_name);
            this.item_price = view.findViewById(R.id.item_price);
            this.item_count = view.findViewById(R.id.item_count);

            this.plus = view.findViewById(R.id.plus);
            this.minus = view.findViewById(R.id.minus);
            this.x = view.findViewById(R.id.x);
        }

        @Override
        public void onClick(View view) {
            Log.i("ㅗㅑ", "");
        }
    }

    public CartAdapter(ArrayList<CartItem> data) {
        this.cartList = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cart_item, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        CartItem item = cartList.get(position);
//        holder.item_image.setImageResource(item.getItem_image());
        holder.item_name.setText(item.getItem_name());
        holder.item_price.setText(item.getItem_price());
        holder.item_count.setText(item.getItem_count());
        // @brief : CustomView 내 버튼 이벤트 적용
//        holder.plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onItemClickEventListener.OnItemClick(view, position, 1);
//            }
//        });
//        holder.minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onItemClickEventListener.OnItemClick(view, position, 2);
//            }
//        });
//        holder.x.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onItemClickEventListener.OnItemClick(view, position, 3);
//            }
//        });
    }
    @Override
    public int getItemCount() {
        return (null != cartList ? cartList.size() : 0);
    }
}
