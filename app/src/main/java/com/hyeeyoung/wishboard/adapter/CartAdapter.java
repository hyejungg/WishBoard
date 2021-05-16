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
        protected TextView total;
        protected TextView quantity;
        protected ImageButton x;

        public CustomViewHolder(View view) {
            super(view);
            this.item_image = view.findViewById(R.id.item_image);
            this.item_name = view.findViewById(R.id.item_name);
            this.total = view.findViewById(R.id.total);
            this.quantity = view.findViewById(R.id.quantity);
        }
    }
    public CartAdapter(ArrayList<CartItem> data) {
        this.cartList = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cart_ltem, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        CartItem item = cartList.get(position);
        viewholder.item_image.setImageResource(item.getItem_image());
        viewholder.item_name.setText(item.getItem_name());
        viewholder.total.setText(item.getTotal());
        viewholder.quantity.setText(item.getQty()+"");
    }
    @Override
    public int getItemCount() {
        return (null != cartList ? cartList.size() : 0);
    }
}
