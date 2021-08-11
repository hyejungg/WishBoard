package com.hyeeyoung.wishboard.detail;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.add.NewItemActivity;
import com.hyeeyoung.wishboard.model.WishItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.util.DateFormatUtil;
import com.squareup.picasso.Picasso;
import java.net.URI;
import java.net.URISyntaxException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity {
    private static final String TAG = "아이템 상세정보 조회";
    static final int UPDATE_REQUEST = 1;
    private boolean is_updated = false;
    private String item_id;
    WishItem wish_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // @brief : 선택한 아이템의 상세정보 조회를 위해 홈 > 아이템 어뎁터 > 해당 아이템의 아이디를 받아옴
        Intent intent = getIntent();
        item_id = intent.getStringExtra("item_id");
        Log.i(TAG, "item_id : " + item_id);
    }

    @Override
    protected void onStart() { //@brief : item_id에 해당하는 아이템의 정보를 가져옴
        super.onStart();
        selectItemDetails(item_id);
        Log.i(TAG, "onStart: " + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: " + "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: " + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: " + "");
    }

    // @brief domain : 쇼핑몰 url을 도메인네임으로 변경한 것
    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();

        // @brief : 도메인 네임으로 바꾸지 못한 경우
        if(domain == null)
            return "";

        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    private void init(){ // WishItem wish_item
        //TextView item_folder = findViewById(R.id.folder_name); // @TODO : 폴더 연동 후 작업하기
        ImageView item_image = findViewById(R.id.item_image);
        TextView item_price = findViewById(R.id.item_price);
        TextView item_name = findViewById(R.id.item_name);
        TextView item_memo = findViewById(R.id.tag);
        TextView item_url = findViewById(R.id.domain_name);
        TextView create_at = findViewById(R.id.create_at);
        TextView noti_info = findViewById(R.id.noti_info);

        item_price.setText(wish_item.getItem_price());
        item_name.setText(wish_item.getItem_name());
        create_at.setText(DateFormatUtil.shortDateYMD(wish_item.getCreate_at()));

        try { // @brief : 아이템 이미지 디스플레이
            Log.i(TAG, "init: " + wish_item.getItem_image());
            Picasso.get().load(wish_item.getItem_image()).error(R.mipmap.ic_main).into(item_image); // @brief : 이미지 가져올 떄 에러 발생 시 기본 이미지 적용
        } catch (IllegalArgumentException i) {
            Log.d(TAG, "아이템 사진 없음");
        }

        // @brief : 메모가 있는 경우에만 태그를 디스플레이
        if(wish_item.getItem_memo()!=null){
            item_memo.setVisibility(View.VISIBLE);
            item_memo.setText(wish_item.getItem_memo());
        }

        // @brief : 쇼핑몰 url을 도메인네임으로 변경하여 디스플레이
        if (wish_item.getItem_url() != null) {
            item_url.setVisibility(View.VISIBLE);
            try {
                String domain = getDomainName(wish_item.getItem_url()); // @brief domain : 쇼핑몰 url을 도메인네임으로 변경한 것

                // @brief : 도메인 주소로 바꾼 경우
                if(!domain.equals(""))
                    item_url.setText(domain); // @brief : 해당 도메인으로 디스플레이
                else // @brief : 도메인 주소로 바꾸지 못한 경우
                    item_url.setText(wish_item.getItem_url()); // @brief : 기존 url로 디스플레이
            } catch (URISyntaxException e) {
                Log.e(TAG, "url 없음");
            }
        }

        // @brief : 알림이 지정된 경우에만 태그를 디스플레이
        if(wish_item.getItem_notification_type()!=null){
            noti_info.setVisibility(View.VISIBLE);
            noti_info.setText(DateFormatUtil.shortDateMDHM(wish_item.getItem_notification_date()) + " " + wish_item.getItem_notification_type());
        }
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
                wish_item = response.body(); // @brief : body()는, json 으로 컨버팅되어 객체에 담겨 지정되로 리턴됨.
                // @brief : 가져온 아이템이 없는 경우
                if (wish_item == null) {
                    Log.i(TAG, "가져온 아이템 없음");
                    Log.i(TAG, response.message());
                }

                // @brief : 서버연결 성공한 경우
                if(response.isSuccessful()){
                    Log.i(TAG, "Retrofit 통신 성공" + wish_item);
                    init();
                } else { // @brief : 통신에 실패한 경우
                    Log.e(TAG, "Retrofit 통신 실패");
                    Log.i(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<WishItem> call, Throwable t) {
                // @brief : 통식 실패 ()시 callback (예외 발생, 인터넷 끊김 등의 시스템적 이유로 실패)
                Log.e(TAG, "서버 연결 실패");
            }
        });
    }

    /**
     * 아이템 삭제를 서버에 요청한다.
     * @param item_id 삭제하려는 아이템의 아이디
     */
    public void deleteItem(String item_id) {
        IRemoteService remoteService = ServiceGenerator.createService(IRemoteService.class);
        Call<Void> call = remoteService.deleteItem(item_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // @brief : 정상적으로 통신 성공한 경우
                    Log.i(TAG, "아이템 삭제 성공");
                    Toast.makeText(ItemDetailActivity.this, "위시리스트에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // @brief : 통신에 실패한 경우
                    Log.e(TAG, "아이템 삭제 오류");
                    Log.i(TAG, response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i(TAG, "아이템 삭제 실패 : "+ t.getMessage());
            }
        });
    }

    public void onClick(View v){
        switch (v.getId()) {
            // @brief : back 버튼 클릭 시 이전 화면으로 돌아가기
            case R.id.back:
                onBackPressed();
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit); // @brief : 오른쪽 -> 왼쪽으로 화면 전환
                break;

            // @brief : 좌측 하단 삭제 버튼 클릭 시 DB에서 해당 아이템 삭제
            case R.id.delete:
                Log.i(TAG, "onClick: " + item_id);
                deleteItem(item_id);
                break;

            // @brief : 좌측 하단 수정 버튼 클릭 시 DB에서 해당 아이템 수정
            case R.id.edit:
                Log.i(TAG, "onClick: " + item_id);
                Intent intent = new Intent(ItemDetailActivity.this, NewItemActivity.class);
                intent.putExtra("item_id", item_id);
                startActivityForResult(intent, 1);
                break;

            // @brief : 쇼핑몰로 이동하기 버튼 클릭 시 해당쇼핑몰로 이동
            case R.id.go_to_shop:
                try {
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(wish_item.getItem_url()));
                    startActivity(intent2);
                } catch (Exception e){
                    Toast.makeText(ItemDetailActivity.this, "해당 쇼핑몰로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // @brief : 아이템 수정 후 복귀할 때 UI 업데이트 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,  "onBackPressed: " + is_updated);
        if (requestCode == UPDATE_REQUEST && resultCode == RESULT_OK)
            is_updated = true;
        Log.i(TAG,  "onBackPressed: " + is_updated);
    }

    // @todo : 아이템이 수정된 경우에 HomeFragment UI를 수정하도록 구현
    //@Override
//    public void onBackPressed() {
//        HomeFragment fragment = new HomeFragment();
//        Bundle bundle2 = new Bundle(1);
//        bundle2.putBoolean("is_updated", is_updated);
//        fragment.setArguments(bundle2);
//        Log.i("홈 test",  "onBackPressed: " + is_updated);

//        if(is_updated){
//            Intent intent = new Intent();
//            intent.putExtra("is_update", true);
//            setResult(Activity.RESULT_OK, intent);
//        } else{
//            setResult(RESULT_OK);
//        }

        // @see : https://ddolcat.tistory.com/461
//        super.onBackPressed();
//    }
}