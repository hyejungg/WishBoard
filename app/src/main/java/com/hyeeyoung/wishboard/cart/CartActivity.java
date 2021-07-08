package com.hyeeyoung.wishboard.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.CartAdapter;
import com.hyeeyoung.wishboard.adapter.ItemAdapter;
import com.hyeeyoung.wishboard.model.CartItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = "장바구니";
    RecyclerView recyclerView;
    CartAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ImageButton cart;
    private ArrayList<CartItem> array_cart;

    private static TextView total; // @params : total 변수
    private String user_id = "";

    Handler timerHandler; // @params : 장바구니 total 값 변경 핸들러
    Runnable updater; // @params : 핸들러에 post할 메세지
    private boolean isUpdate = false; // @params : +,-,x 버튼 클릭 시(값이 변경된 경우) //default : false

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        if (SaveSharedPreferences.getUserId(this).length() != 0)
            user_id = SaveSharedPreferences.getUserId(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // @brief : 서버와 연결하여 데이터 가져옴
        getData(user_id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // @brief : 총 total값 변경 handler 설정
        updater = new Runnable() {
            @Override
            public void run() {
                setTotal();
                timerHandler.postDelayed(updater,500); //@brief : 0.5초 뒤에 총 구매값 변경
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        //@brief : back버튼 클릭 시에도 업데이트 변경 시 값 수정할 수 있도록
        if(isUpdate)
            updateCart(array_cart);
    }

    //@brief : 액티비티 메모리에서 제거 시
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(updater);
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
                onStop(); // @brief : 액티비티 종료 -> 장바구니 update 처리
        }
    }

    /**
     * @param user_id 사용자 아이디
     * @brief : 서버에서 장바구니 정보를 조회
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
                        Log.i("TAG", "조회 성공" + "\n " + array_cart + "");
                } else {
                    // @brief : 통신에 실패한 경우
                    Log.e("TAG", "조회 오류");
                }
                init();
            }

            @Override
            public void onFailure(Call<ArrayList<CartItem>> call, Throwable t) {
                // @brief : 통신 실패 시 callback
                Log.e("TAG", "조회 서버 연결 실패");
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

        // @brief : cart_item 내 세부 버튼 Click event 적용
        adapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onMinusClick(int position, CartAdapter.CustomViewHolder holder) {
                CartAdapter.decCount(position, holder);
                adapter.notifyDataSetChanged();
                isUpdate = true;
            }

            @Override
            public void onPlusClick(int position, CartAdapter.CustomViewHolder holder) {
                CartAdapter.incCount(position, holder);
                adapter.notifyDataSetChanged();
                isUpdate = true;
            }

            @Override
            public void onXbtnClick(int position) {
                // @see : x버튼의 경우 ItemAdapter.deleteCart()를 동작하도록 함
                CartItem item = array_cart.get(position);
                ItemAdapter.deleteCart(user_id, item.getItem_id());
                array_cart.remove(position);
                adapter.notifyDataSetChanged();
                isUpdate = true;
            }
        });

        // @brief : 장바구니 내 아이템별 가격 및 개수가 update될 때마다 total값 변경 display
        timerHandler = new Handler();
        timerHandler.post(updater);
        adapter.notifyDataSetChanged();
    }

    /**
     * @brief : 화면 하단의 장바구니 내 총 합산 금액을 보여줌
     */
    private void setTotal() {
        int total_price = 0;
        for (int i = 0; i < array_cart.size(); i++) {
            // @brief : 가격데이터 예외처리
            if (array_cart.get(i).getItem_price() == null) { // @ brief : DB에 저장된 값이 null인 경우 0으로 초기화
                array_cart.get(i).setItem_price(0 + "");
            }
            // @brief : DB에서 가져온 아이템의 가격 정보와 개수를 이용하여 Total 금액을 구함
            int temp_price = Integer.parseInt(array_cart.get(i).getItem_price());
            int icount = Integer.parseInt(array_cart.get(i).getItem_count());
            if(icount > 1){
                temp_price = temp_price * icount;
            }
            total_price += temp_price;
        }
        total.setText(total_price + "");
    }

    /**
     * @brief : 카트의 수량 변경 시 count, price값 변경
     * @param array_cart 현재 장바구니 정보를 담아둔 ArrayList<CartItem>
     */
    private void updateCart(ArrayList<CartItem> array_cart){
        ArrayList<CartItem> req_item = new ArrayList<>();

        for(int i = 0; i < array_cart.size(); i++){
            // @brief : 해당 객체의 개수 변경여부가 true인 경우의 객체에 대해서만 update
            if(array_cart.get(i).isCheckCount()){
                CartItem item = new CartItem();
                item.setItem_count(array_cart.get(i).getItem_count());
                item.setItem_id(array_cart.get(i).getItem_id());
                item.setUser_id(user_id);

                // @brief : 요청 보낼 req_item 배열에 추가
                req_item.add(item);
            }
        }

        IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
        Call<ResponseBody> call = remote_service.updateCartInfo(req_item);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String seq = null;
                    try{
                        seq = response.body().string();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.i("TAG", "업데이트 성공" + "\n "  + seq);
                } else {
                    // @brief : 통신에 실패한 경우
                    Log.e("TAG", "업데이트 오류");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // @brief : 통신 실패 시 callback
                Log.e("TAG", "업데이트 서버 연결 실패");
                t.printStackTrace();
            }
        });
    }
}