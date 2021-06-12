package com.hyeeyoung.wishboard.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.WishItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // @brief : 선택한 아이템의 상세정보 조회를 위해 홈 > 아이템 어뎁터 > 해당 아이템의 아이디를 받아옴
        Intent intent = getIntent();
        String item_id = intent.getStringExtra("item_id");
        //Log.i("아이템 상새정보 가져오기", "아이템 아이디 가져오기: " + item_id);
        selectItemDetails(item_id);

        ImageButton back = findViewById(R.id.back);

        // @brief : back 버튼 클릭 시 이전 화면으로 돌아가기
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onBackPressed();
                // @brief : 오른쪽 -> 왼쪽으로 화면 전환
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
            }
        });
    }

    // @brief domain : 쇼핑몰 url을 도메인네임으로 변경한 것
    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();

        // @brief : 도메인 네임으로 바꾸지 못한 경우
        if(domain == null) return "";

        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    private void init(WishItem wish_item){
        //TextView itme_folder = findViewById(R.id.folder_name); // @TODO : 폴더 연동 후 작업하기
        ImageView item_image = findViewById(R.id.item_image);
        TextView item_price = findViewById(R.id.item_price);
        TextView item_name = findViewById(R.id.item_name);
        TextView item_memo = findViewById(R.id.tag);
        TextView item_url = findViewById(R.id.domain_name);
        Button go_to_shop = findViewById(R.id.go_to_shop);

        // @brief : 아이템 이미지 디스플레이
        try {
            Picasso.get().load(wish_item.getItem_image()).into(item_image); // @brief : 가져온 이미지경로값으로 이미지뷰 디스플레이
        } catch (IllegalArgumentException i) {
            Log.d("아이템 상새정보 가져오기", "아이템 사진 없음");
        }

        item_price.setText(wish_item.getItem_price());
        item_name.setText(wish_item.getItem_name());

        // @brief : 메모가 있는 경우에만 태그를 디스플레이
        if(wish_item.getItem_memo()!=null){
            item_memo.setVisibility(View.VISIBLE);
            item_memo.setText(wish_item.getItem_memo());
        }

        // @brief : 쇼핑몰 url을 도메인네임으로 변경하여 디스플레이
        if (wish_item.getItem_url() != null) {
            try {
                String domain = getDomainName(wish_item.getItem_url()); // @brief domain : 쇼핑몰 url을 도메인네임으로 변경한 것

                // @brief : 도메인 주소로 바꾼 경우
                if(!domain.equals(""))
                    item_url.setText(domain); // @brief : 해당 도메인으로 디스플레이
                else // @brief : 도메인 주소로 바꾸지 못한 경우
                    item_url.setText(wish_item.getItem_url()); // @brief : 기존 url로 디스플레이
            } catch (URISyntaxException e) {
                Log.e("아이템 상세정보 가져오기", "url 없음");
            }
        }

        // @brief : 쇼핑몰로 이동하기 버튼 클릭 시 해당쇼핑몰로 이동
        go_to_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(wish_item.getItem_url()));
                    startActivity(intent);
                } catch (Exception e){
                    Toast.makeText(ItemDetailActivity.this, "해당 쇼핑몰로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * @brief : 서버에서 아이템 아이디에 해당하는 아이템 정보를 조회한다.
     * @param item_id 아이템 아이디
     */
    private void selectItemDetails(String item_id) {
        IRemoteService remoteService = ServiceGenerator.createService(IRemoteService.class);
        Call<WishItem> call = remoteService.selectItemDetails(item_id);
        call.enqueue(new Callback<WishItem>() {
            @Override
            public void onResponse(Call<WishItem> call, Response<WishItem> response) {
                WishItem wish_item = response.body(); // @brief : body()는, json 으로 컨버팅되어 객체에 담겨 지정되로 리턴됨.

                // @brief : 가져온 아이템이 없는 경우
                if (wish_item == null) {
                    Log.i("아이템 상새정보 가져오기", "가져온 아이템 없음");
                }

                // @brief : 서버연결 성공한 경우
                if(response.isSuccessful()){
                     Log.i("아이템 상새정보 가져오기", "Retrofit 통신 성공");
                     //Log.i("아이템 상새정보 가져오기", wish_item+""); // @deprecated : 테스트용
                     init(wish_item); // @brief : onCreateView 메서드에서 해당 위치로 옮김
                } else { // @brief : 통신에 실패한 경우
                    Log.e("아이템 상새정보 가져오기", "Retrofit 통신 실패");
                }
            }

            @Override
            public void onFailure(Call<WishItem> call, Throwable t) {
                // @brief : 통식 실패 ()시 callback (예외 발생, 인터넷 끊김 등의 시스템적 이유로 실패)
                Log.e("아이템 상새정보 가져오기", "서버 연결 실패");
            }
        });
    }

//    private void onClick(View v){ // @TODO : 추후 url 클릭, 아이템 수정, 삭제 구현 시 사용
//        switch (v.getId()){
//        }
//    }
}