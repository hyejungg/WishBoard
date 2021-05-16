package com.hyeeyoung.wishboard.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.CartAdapter;
import com.hyeeyoung.wishboard.model.CartItem;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CartAdapter adapter;
    private ArrayList<CartItem> cartList;
    private LinearLayoutManager linearLayoutManager;
    private ImageButton cart;

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
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerview_cart_list);
        cartList = new ArrayList<>();
        adapter = new CartAdapter(cartList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // @brief : 아이템 간 구분선 넣기
        linearLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(linearLayoutManager);
        cart = findViewById(R.id.cart);

        addItem(R.drawable.sample, "name1", "100,000", 1);
        addItem(R.drawable.sample, "name2", "200,000", 2);
        addItem(R.drawable.sample, "name3", "300,000", 3);
        addItem(R.drawable.sample, "name4", "400,000", 4);
        addItem(R.drawable.sample, "name5", "500,000", 5);
        addItem(R.drawable.sample, "name6", "600,000", 6);
        addItem(R.drawable.sample, "name7", "700,000", 7);
        addItem(R.drawable.sample, "name8", "800,000",8);
        adapter.notifyDataSetChanged();
    }

    public void onClick(View v){
        switch (v.getId()){
            // @brief : back 버튼 클릭 시 이전 화면으로 돌아가기
            case R.id.back :
                onBackPressed();
                // @brief : 오른쪽 -> 왼쪽으로 화면 전환
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        }
    }

    // @deprecated
    private void addItem(int img, String name, String total, int qty) {
        CartItem item = new CartItem();
        item.setItem_image(img);
        item.setItem_name(name);
        item.setTotal(total);
        item.setQty(qty);
        cartList.add(item);
    }
}