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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CartAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ImageButton cart;
    private ArrayList<CartItem> array_cart;

    // @params : total 변수
    private TextView total;
    private String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
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


    public void onClick(View v) {
        switch (v.getId()) {
            // @brief : back 버튼 클릭 시 이전 화면으로 돌아가기
            case R.id.back:
                onBackPressed();
                // @brief : 오른쪽 -> 왼쪽으로 화면 전환
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        }
    }

    /**
     * @brief : 서버에서 장바구니 정보를 조회
     * @param user_id 사용자 아이디
     */
    private void getData(String user_id) {
        Log.i("CartItem select값 확인", user_id); // @descr

        IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
        Call<ArrayList<CartItem>> call = remote_service.selectCartInfo(user_id);
        call.enqueue(new Callback<ArrayList<CartItem>>() {
            @Override
            public void onResponse(Call<ArrayList<CartItem>> call, Response<ArrayList<CartItem>> response) {
                array_cart = response.body();

                if (array_cart == null) array_cart = new ArrayList<>();

                if (response.isSuccessful()) {
                    if (array_cart.size() > 0) // @deprecated : test용
                        Log.i("Cart 조회", "성공" + "\n " + array_cart + "");
                } else {
                    // @brief : 통신에 실패한 경우
                    Log.e("Cart 조회", "오류");
                }
                init();
                setTotal(array_cart);
            }

            @Override
            public void onFailure(Call<ArrayList<CartItem>> call, Throwable t) {
                // @brief : 통신 실패 시 callback
                Log.e("Cart 조회", "서버 연결 실패");
                t.printStackTrace();
            }

        });
    }

    private void init() {
        // @brief : 뷰 초기화
        total = findViewById(R.id.total);
        cart = findViewById(R.id.cart);

        recyclerView = findViewById(R.id.recyclerview_cart_list);
        adapter = new CartAdapter(array_cart);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // @brief : 아이템 간 구분선 넣기
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // @brief : CustomView plus, minus, x 버튼 Click Event 처리
//        adapter.setOnItemClickListener(new CartAdapter.OnItemClickEventListener() {
//            @Override
//            public void OnItemClick(View v, int pos, int num) {
//                Log.i("Cart item click", "");
//                switch (num){
//                    case 1 :
//                        //plus
//                        Log.i("Cart + click", "");
//                        break;
//                    case 2 :
//                        //minus
//                        Log.i("Cart - click", "");
//                        break;
//                    case 3 :
//                        //x
//                        Log.i("Cart x click", "");
//                        break;
//                }
//            }
//        });

        adapter.notifyDataSetChanged();
    }

    /**
     * @brief 화면 하단의
     * 장바구니 내 총 합산 금액을 보여줌
     * @param array_cart 서버의 response로 받아 온 arrayList
     */
    private void setTotal(ArrayList<CartItem> array_cart){
        int total_price = 0;
        // @deprecated : test용
//        Log.i("Cart array_cart Size", String.valueOf(array_cart.size()));
//        Log.i("Cart array_cart[i] price", array_cart.get(0).getItem_price());
//        Log.i("Cart array_cart[i] price", array_cart.get(1).getItem_price());
//        Log.i("Cart array_cart[i] price", array_cart.get(2).getItem_price());
//        Log.i("Cart array_cart[i] price", array_cart.get(3).getItem_price());
        for(int i = 0; i < array_cart.size(); i++){
            int temp_price = Integer.parseInt(array_cart.get(i).getItem_price());
            total_price += temp_price;
        }
        total.setText(total_price+"");
    }
}