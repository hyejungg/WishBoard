package com.hyeeyoung.wishboard.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.CartAdapter;
import com.hyeeyoung.wishboard.model.CartItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CartAdapter adapter;
    private ArrayList<CartItem> cartList;
    private LinearLayoutManager linearLayoutManager;
    private ImageButton cart;

    // @params : total 변수
    private TextView total;
    private String user_id = "";
    private CartItem res_cart_item;
    private ArrayList<CartItem> array_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
//        ImageButton back = findViewById(R.id.back);
        // @brief : back 버튼 클릭 시 이전 화면으로 돌아가기
//        back.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                onBackPressed();
//                // @brief : 오른쪽 -> 왼쪽으로 화면 전환
//                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
//            }
//        });

        if (SaveSharedPreferences.getUserId(this).length() != 0)
            user_id = SaveSharedPreferences.getUserId(this);

        // @brief : 서버와 연결하여 데이터 가져옴
        getData(user_id);
    }

    private void init() {
        total = findViewById(R.id.total);

        recyclerView = findViewById(R.id.recyclerview_cart_list);
        cartList = new ArrayList<>();
        adapter = new CartAdapter(cartList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // @brief : 아이템 간 구분선 넣기
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        cart = findViewById(R.id.cart);

//        addItem(R.drawable.sample, "name1", "100,000", 1);
//        addItem(R.drawable.sample, "name2", "200,000", 2);
//        addItem(R.drawable.sample, "name3", "300,000", 3);
//        addItem(R.drawable.sample, "name4", "400,000", 4);
//        addItem(R.drawable.sample, "name5", "500,000", 5);
//        addItem(R.drawable.sample, "name6", "600,000", 6);
//        addItem(R.drawable.sample, "name7", "700,000", 7);
//        addItem(R.drawable.sample, "name8", "800,000", 8);

        adapter.notifyDataSetChanged();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            // @brief : back 버튼 클릭 시 이전 화면으로 돌아가기
            case R.id.back:
                onBackPressed();
                // @brief : 오른쪽 -> 왼쪽으로 화면 전환
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        }
    }

    // @deprecated
    private void addItem(CartItem cartItem) {
//        CartItem add_cart_item = new CartItem();
//        add_cart_item.getItem_image();
//        add_cart_item.getItem_name();
//        add_cart_item.getItem_price();
//        add_cart_item.getQty();
        cartList.add(cartItem);
    }

    private void getData(String user_id) {
        Log.i("CartItem select값 확인", user_id );

        IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
        Call<CartItem> call = remote_service.selectCartInfo(user_id);
        call.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                if(response.isSuccessful()){
                    // @brief : 정상적으로 통신 성공한 경우
                    try{
                        res_cart_item = response.body();
//                        array_cart = response.body(); // @params : ArrayList<CartItem>
//                        StringBuilder builder = new StringBuilder();

//                        Log.i("Cart 조회", "성공" + "\n " + builder.toString());
                        Log.i("Cart 조회", "성공" + "\n " + res_cart_item);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

//                    cartList.add(cartItem);
                }else{
                    // @brief : 통신에 실패한 경우
                    Log.e("Cart 조회", "오류");
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                // @brief : 통신 실패 시 callback
                Log.e("Cart 조회", "서버 연결 실패");
                t.printStackTrace();
            }

        });

//        addItem(res_cart_item);
    }
}