package com.hyeeyoung.wishboard.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.hyeeyoung.wishboard.model.CartItem;

import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CustomViewHolder> {
    private ArrayList<WishItem> wish_list;
    private Intent intent;
    protected Context context;
    protected boolean isClicked = true;
    private CartItem cart_item, res_cart_item;
    private String user_id;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView item_image;
        protected TextView item_name, item_price;
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

    public ItemAdapter(ArrayList<WishItem> data, String user_id){
        this.wish_list = data;
        this.user_id = user_id;
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

        /**
         * @brief : 아이템 이미지를 화면에 보여준다.
         */
        try {
            Picasso.get().load(item.getItem_image()).into(viewholder.item_image); // @brief : 가져온 이미지경로값으로 이미지뷰 디스플레이
        } catch (IllegalArgumentException i) {
            Log.d("checkings", "아이템 사진 없음");
        }

        viewholder.item_name.setText(item.getItem_name());
        viewholder.item_price.setText(item.getItem_price());

        /**
         * @brief : 장바구니에 저장된 아이템은 장바구니 버튼을 활성화(그린컬러 적용)
         */
        if (item.getCart_item_id() == null) {
            viewholder.cart.setBackgroundResource(R.drawable.round_sticker_white);
        }else{
            viewholder.cart.setBackgroundResource(R.drawable.round_sticker);
        }

        /**
         * @param : 아이템 클릭 시 아이템 상세조회로 이동
         */
        viewholder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), ItemDetailActivity.class);
                intent.putExtra("item_id",item.getItem_id()+"");
                v.getContext().startActivity(intent);
            }
        });

        /**
         *  @brief : 아이템을 장바구니에 추가/제거
         */
        viewholder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked == true){ // @brief : 추가하는 경우
                    viewholder.cart.setBackgroundResource(R.drawable.round_sticker);
                    isClicked = false;
                    SaveSharedPreferences.setCheckedCart(context, true);
                    addCart(user_id, item.getItem_id());
                }else{ // @brief : 제거하는 경우
                    viewholder.cart.setBackgroundResource(R.drawable.round_sticker_white);
                    isClicked = true;
                    SaveSharedPreferences.setCheckedCart(context, false);
                    deleteCart(user_id, item.getItem_id());
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return (null != wish_list ? wish_list.size() : 0);
    }

    /**
     * @brief : 장바구니에 아이템 정보를 추가
     * @param user_id 사용자 아이디
     * @param item_id 아이템 아이디
     */
    // @TODO : 이미 장바구니에 값이 존재하는 경우, insert가 불가하도록 예외처리 필요
    private void addCart(String user_id, String item_id){
        // @brief : 서버에 들어갈 CartItem 초기화
        cart_item = new CartItem(user_id, item_id);
        Log.i("CartItem add값 확인", user_id + " / " + item_id); // @deprecated : 확인용

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
//                        cart_item.setItem_count(res_cart_item.getItem_count()); // @brief : item_count를 서버로부터 받아 user_id별 count값 재할당
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Log.i("Cart 등록", "성공");
                }else{
                    // @brief : 통신에 실패한 경우
                    Log.e("Cart 등록", "오류");
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                // @brief : 통신 실패 시 callback
                Log.e("Cart 등록", "서버 연결 실패");
                Log.e("Cart 등록", t.getMessage());
                t.fillInStackTrace();
            }
        });
    }

    /**
     * @brief : 장바구니에 아이템 정보를 삭제
     * @param user_id 사용자 아이디
     * @param item_id 아이템 아이디
     */
    public static void deleteCart(String user_id, String item_id){
        Log.i("CartItem delete값 확인", user_id + " / " + item_id); // @deprecated : 확인용

        // @brief : delete는 보안을 위해 @body로 담아 전송 -> CartItem에 정보를 담아 전송
        CartItem delete_cart = new CartItem(user_id, item_id);

        IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
        Call<ResponseBody> call = remote_service.deleteCartInfo(delete_cart); //@brief : Body로 보내야해서 객체로 변경
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    // @brief : 정상적으로 통신 성공한 경우
                    String str = response.body().toString();
                    Log.i("Cart 삭제", "성공 " + str);
                }else{
                    // @brief : 통신에 실패한 경우
                    Log.e("Cart 삭제", "오류");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // @brief : 통신 실패 시 callback
                Log.e("Cart 등록", "서버 연결 실패");
                t.fillInStackTrace();
            }
        });
    }
}
