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

    private TextView total; // @params : total 변수
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

    /**
     * @brief : <- 버튼을 클릭했을 때, 동작을 지정
     */
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
        IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
        Call<ArrayList<CartItem>> call = remote_service.selectCartInfo(user_id);
        call.enqueue(new Callback<ArrayList<CartItem>>() {
            @Override
            public void onResponse(Call<ArrayList<CartItem>> call, Response<ArrayList<CartItem>> response) {
                array_cart = response.body();

                // @brief : 가져온 정보가 없는 경우, 빈 ArrayList<>로 초기화
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

    /**
     * @brief : 뷰 초기화
     */
    private void init() {
        total = findViewById(R.id.total);
        cart = findViewById(R.id.cart);

        // @brief : recyclerView 관련 초기화
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
        for(int i = 0; i < array_cart.size(); i++){
            // @brief : 가격데이터 예외처리
            if(array_cart.get(i).getItem_price() == null){ // @ brief : DB에 저장된 값이 null인 경우 0으로 초기화
                array_cart.get(i).setItem_price(0+"");
            }
            // @brief : DB에서 가져온 아이템의 가격 정보를 이용하여 Total 금액을 구함
            int temp_price = Integer.parseInt(array_cart.get(i).getItem_price());
            total_price += temp_price;
        }
        total.setText(total_price+"");
    }
}