package com.hyeeyoung.wishboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.detail.ItemDetailActivity;

import com.hyeeyoung.wishboard.model.WishItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CustomViewHolder> {
    private ArrayList<WishItem> wish_list;
    private Intent intent;
    protected Context context;
    protected boolean isClicked = true;


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView item_image;
        protected TextView item_name;
        protected TextView item_price;
        protected Button cart;
        protected ConstraintLayout item;

        public CustomViewHolder(View view) {
            super(view);
            this.item_image = (ImageView) view.findViewById(R.id.item_image);
            this.item_name = (TextView) view.findViewById(R.id.item_name);
            this.item_price = (TextView) view.findViewById(R.id.item_price);
            this.cart = (Button) view.findViewById(R.id.cart);
            this.item = (ConstraintLayout) view.findViewById(R.id.item);
        }
    }
    public ItemAdapter(ArrayList<WishItem> data) {
        this.wish_list = data;
        notifyDataSetChanged(); // @brief : 데이터 변경사항 반영
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.wish_item, viewGroup, false);
        context = view.getContext();
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {
        WishItem item = wish_list.get(position);

        try { // @brief : 아이템 이미지를 화면에 보여준다.
            Picasso.get().load(item.getItem_image()).into(viewholder.item_image); // @brief : 가져온 이미지경로값으로 이미지뷰 디스플레이
        } catch (IllegalArgumentException i) {
            Log.d("checkings", "아이템 사진 없음");
        }

        viewholder.item_name.setText(item.getItem_name());
        viewholder.item_price.setText(item.getItem_price());
        viewholder.cart.setBackgroundResource(R.drawable.round_sticker_white);
        //viewholder.cart.setImageResource(R.drawable.cart_black);
        // @param : 아이템 클릭 시 아이템 상세조회로 이동동
       viewholder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), ItemDetailActivity.class);
                intent.putExtra("item_id",item.getItem_id()+"");
                v.getContext().startActivity(intent);
            }
        });
       // @brief : 아이템을 장바구니에 담거나 제거하는 경우, 해당 버튼의 컬러를 변경 (추후 구현)
       viewholder.cart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (isClicked == true){
                   //viewholder.cart.setImageResource(R.drawable.cart_green);
                   viewholder.cart.setBackgroundResource(R.drawable.round_sticker);
                   isClicked = false;
                   //addCart(user_id, item_id);
               }else {
                   //viewholder.cart.setImageResource(R.drawable.cart_black);
                   viewholder.cart.setBackgroundResource(R.drawable.round_sticker_white);
                   isClicked = true;
                   //deleteCart();
               }
           }
       });
    }
    @Override
    public int getItemCount() {
        return (null != wish_list ? wish_list.size() : 0);
    }
}
