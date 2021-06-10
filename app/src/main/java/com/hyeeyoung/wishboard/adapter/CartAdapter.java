package com.hyeeyoung.wishboard.adapter;

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

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView item_image;
        protected TextView item_name;
        protected TextView item_price;
        protected TextView quantity;
        protected ImageButton x;

        protected ImageButton plus;
        protected ImageButton minus;

        public CustomViewHolder(View view) {
            super(view);
            this.item_image = view.findViewById(R.id.item_image);
            this.item_name = view.findViewById(R.id.item_name);
            this.item_price = view.findViewById(R.id.item_price);
            this.quantity = view.findViewById(R.id.quantity);

            this.plus = view.findViewById(R.id.plus);
            this.minus = view.findViewById(R.id.minus);
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
        holder.item_image.setImageResource(item.getItem_image());
        holder.item_name.setText(item.getItem_name());
        holder.item_price.setText(item.getItem_price());
        holder.quantity.setText(item.getQty()+"");

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int display_qty = 0;
                if(item.getQty() < 0){
                    holder.quantity.setText(0);
                }else{
                }
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // +버튼이 눌릴 때마다 가격이 item_price 만큼 더해짐
            }
        });
    }
    @Override
    public int getItemCount() {
        return (null != cartList ? cartList.size() : 0);
    }
}
