package com.hyeeyoung.wishboard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.CartItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CustomViewHolder> {
    private static final String TAG = "장바구니";

    // @see : incCount(), decCount() 에서도 사용하기 위해 static으로 선언
    private static ArrayList<CartItem> cartList;
    protected static Context context;

    // @brief : CustomView 내 item click
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onMinusClick(int position, CartAdapter.CustomViewHolder holder);
        void onPlusClick(int positio, CartAdapter.CustomViewHolder holder);
        void onXbtnClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected ImageView item_image;
        protected TextView item_name;
        protected TextView item_price;
        protected TextView item_count;

        protected ImageView x;
        protected ImageView plus;
        protected ImageView minus;

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
    }

    public CartAdapter(ArrayList<CartItem> data) {
        this.cartList = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cart_item, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        context = view.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        CartItem item = cartList.get(position);

        try { // @brief : 아이템 이미지를 화면에 보여준다.
            Picasso.get().load(item.getItem_image()).into(holder.item_image); // @brief : 가져온 이미지경로값으로 이미지뷰 디스플레이
        } catch (IllegalArgumentException i) {
            Log.e("TAG", "이미지 정보 없음", i);
        }
        holder.item_name.setText(item.getItem_name());
        holder.item_count.setText(item.getItem_count());

        if(item.getItem_price() == null){
            item.setItem_price("0");
        }

        // @brief : item_count 값에 따른 가격처리
        if(item.getItem_count() != "1"){
            int icount = Integer.parseInt(item.getItem_count());
            int ori_iprice = Integer.parseInt(item.getItem_price());
            int new_iprice = icount * ori_iprice; //변경된 아이템 가격
            holder.item_price.setText(String.valueOf(new_iprice));
        }else{
            holder.item_price.setText(item.getItem_price());
        }

        // @brief : - 버튼 기능 처리
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onMinusClick(position, holder);
            }
        });
        // @brief :  + 버튼 기능 처리
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onPlusClick(position, holder);
            }
        });
        holder.x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onXbtnClick(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return (null != cartList ? cartList.size() : 0);
    }

    /*********************
     * @brief 장바구니 +, -버튼 클릭 처리 함수
     * @param position
     */
    public static void incCount(int position, CustomViewHolder holder){
        CartItem item = cartList.get(position);

        // @brief : 해당 아이템의 원래 값을 가져옴
        int inc = Integer.parseInt(item.getItem_count());
        int iprice = Integer.parseInt(item.getItem_price());

        inc++;
        int new_iprice = inc * iprice; //변경된 아이템 가격

        // @brief : 변경 내역을 화면 상에서 보여줌
        holder.item_count.setText(inc+"");
        holder.item_price.setText(new_iprice+"");

        item.setItem_count(String.valueOf(inc)); // @brief : 객체에서 아이템의 개수만 변경
        item.setCheckCount(true); // @brief : 해당 객체의 개수 변경 여부 체크

        // @brief : 예외 처리
        if(inc >= 999){
            Toast.makeText(context, "수량을 더 크게 선택할 수 없습니다.", Toast.LENGTH_SHORT).show();
            // @brief : 원래 DB 값으로 초기화
            item.setItem_count("1");
            holder.item_count.setText(item.getItem_count());
            holder.item_price.setText(item.getItem_price());
        }
    }
    public static void decCount(int position, CustomViewHolder holder){
        // @brief : diplay 되어있는 아이템 가격 가져옴
        int holder_price = Integer.parseInt(holder.item_price.getText()+"");

        CartItem item = cartList.get(position);

        int dec = Integer.parseInt(item.getItem_count()); //원래 아이템 값 (count = 1 기준)

        int dec_num = 0; //@params : 개수 감소에 따른 가격 계산을 위해 사용되는 변수
        dec_num++;

        // @brief : 현재 diplay 된 가격 - (해당 아이템의 원래 값 * 감소 수량)
        int ori_ip = Integer.parseInt(item.getItem_price());
        int new_ip = holder_price - (ori_ip * dec_num);

        // @brief : 해당 아이템의 값을 변경
        holder.item_count.setText(--dec+"");
        holder.item_price.setText(new_ip+"");

        item.setItem_count(String.valueOf(dec)); // @brief : 객체에서 아이템의 개수만 변경
        item.setCheckCount(true); // @brief : 해당 객체의 개수 변경 여부 체크

        // @brief : 예외 처리
        if(dec < 1){
            Toast.makeText(context, "수량을 더 작게 선택할 수 없습니다.", Toast.LENGTH_SHORT).show();
            // @brief : 원래 DB 값으로 초기화
            item.setItem_count("1");
            holder.item_count.setText(item.getItem_count());
            holder.item_price.setText(item.getItem_price());
        }
    }
}
