package com.hyeeyoung.wishboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.detail.ItemDetailActivity;
import com.hyeeyoung.wishboard.model.CartItem;
import com.hyeeyoung.wishboard.model.WishItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CustomViewHolder> {
    private ArrayList<WishItem> wish_list;
    private Intent intent;
    protected Context context;
    protected boolean isClicked = true;

    private CartItem cart_item;
    private CartItem res_cart_item;
    private String user_id;
    private String item_id;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView item_image;
        protected TextView item_name;
        protected TextView item_price;
        protected ImageView cart;
        protected ConstraintLayout item;

        public CustomViewHolder(View view) {
            super(view);
            this.item_image = (ImageView) view.findViewById(R.id.item_image);
            this.item_name = (TextView) view.findViewById(R.id.item_name);
            this.item_price = (TextView) view.findViewById(R.id.item_price);
            this.cart = (ImageView) view.findViewById(R.id.cart);
            this.item = (ConstraintLayout) view.findViewById(R.id.item);
        }
    }
    public ItemAdapter(ArrayList<WishItem> data) {
        this.wish_list = data;
        notifyDataSetChanged(); // @brief : 데이터 변경사항 반영
    }
    public ItemAdapter(ArrayList<WishItem> data, String user_id, String item_id){
        this.wish_list = data;
        this.user_id = user_id;
        this.item_id = item_id;
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
            Picasso.get().load(IRemoteService.IMAGE_URL +item.getItem_image()).into(viewholder.item_image); // @brief : 가져온 이미지경로값으로 이미지뷰 디스플레이
        } catch (IllegalArgumentException i) {
            Log.d("checkings", "아이템 사진 없음");
        }

        viewholder.item_name.setText(item.getItem_name());
        viewholder.item_price.setText(item.getItem_price());
        viewholder.cart.setImageResource(R.drawable.cart_black);

        // @param : 아이템 클릭 시 아이템 상세조회로 이동동
       viewholder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), ItemDetailActivity.class);
                v.getContext().startActivity(intent);
            }
        });
       // @brief : 아이템을 장바구니에 담거나 제거하는 경우, 해당 버튼의 컬러를 변경 (추후 구현)
       viewholder.cart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (isClicked == true){
                   viewholder.cart.setImageResource(R.drawable.cart_green);
                   isClicked = false;
                   addCart(user_id, item_id);
               }else {
                   viewholder.cart.setImageResource(R.drawable.cart_black);
                   isClicked = true;
                   deleteCart();
               }
           }
       });
    }
    @Override
    public int getItemCount() {
        return (null != wish_list ? wish_list.size() : 0);
    }

    private void addCart(String user_id, String item_id){
        // @brief : 서버에 들어갈 CartItem 초기화
        cart_item = new CartItem(user_id, item_id);
        Log.i("CartItem add값 확인", cart_item.user_id + " / " + cart_item.item_id);

        // @params : 서버의 응답을 받는 CartItem
        res_cart_item = new CartItem();
        IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
        Call<CartItem> call = remote_service.insertCartInfo(cart_item);
        call.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                if(response.isSuccessful()){
                    // @brief : 정상적으로 통신 성공한 경우
                    try{
                        res_cart_item = response.body();
                        cart_item.setQty(res_cart_item.getQty());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Log.i("Cart 등록", "성공" + "\n cart_item의 qty : " + cart_item.getQty());
                }else{
                    // @brief : 통신에 실패한 경우
                    Log.e("Cart 등록", "오류");
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                // @brief : 통신 실패 시 callback
                Log.e("Cart 등록", "서버 연결 실패");
                t.fillInStackTrace();
            }
        });
    }

    private void deleteCart(){
        Log.i("CartItem delete값 확인", cart_item.user_id + " / " + cart_item.item_id);
        // @params : 서버의 응답을 받는 CartItem
        res_cart_item = new CartItem();
        IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
        Call<CartItem> call = remote_service.deleteCartInfo(cart_item.user_id, cart_item.item_id);
        call.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                if(response.isSuccessful()){
                    // @brief : 정상적으로 통신 성공한 경우
                    try{
                        res_cart_item = response.body(); // @deprecated : 사용 안하면 지울 것
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Log.i("Cart 삭제", "성공");
                }else{
                    // @brief : 통신에 실패한 경우
                    Log.e("Cart 삭제", "오류");
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                // @brief : 통신 실패 시 callback
                Log.e("Cart 등록", "서버 연결 실패");
                t.fillInStackTrace();
            }
        });
    }
}
