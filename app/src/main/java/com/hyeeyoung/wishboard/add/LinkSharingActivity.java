package com.hyeeyoung.wishboard.add;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.NotiItem;
import com.hyeeyoung.wishboard.model.WishItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;
import com.hyeeyoung.wishboard.util.NumberPickerUtil;
import com.squareup.picasso.Picasso;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinkSharingActivity extends AppCompatActivity {
    private static final String TAG = "링크공유로 아이템 등록";
    private final static int TIME_PICKER_INTERVAL = 5; // @param : 알림 시간의 분단위를 5분간격으로 설정

    // @param : 아이템 정보가 디스플레이될 뷰
    TextView item_name;
    EditText item_price, item_memo;
    ImageView item_image;

    // @param : 아이템 저장을 위한 아이템 및 알림 객체
    WishItem wish_item;
    NotiItem noti_item;

    //TextView won;
    String site_url = "";
    String user_id = "";
    NumberPickerUtil np_util; // @param : 알림 유형 및 날짜 넘버피커 유틸

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_sharing);

        // @param : 외부에서 공유를 통해 데이터 받기
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType(); // @param : MIME type 받기

        np_util = new NumberPickerUtil();
        init();

        if(SaveSharedPreferences.getUserId(this).length() != 0)
            user_id = SaveSharedPreferences.getUserId(this);

        // @breif : 전송 중인 데이터 처리
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) { // @breif : 전송데이터 타입이 텍스트인 경우
                site_url = intent.getStringExtra(Intent.EXTRA_TEXT); // @params : site url은 String에 보관. DB 저장용
                Log.i(TAG, "handleSendText: " + site_url);
                new JsoupAsyncTask(site_url).execute();
            }
        }
    }

    void init() {
        // @param : 알림 유형 및 날짜 넘버피커
        np_util.noti_types_array = getResources().getStringArray(R.array.noti_types_array);
        np_util.type = findViewById(R.id.num_picker_type);
        np_util.date = findViewById(R.id.num_picker_date);
        np_util.hour = findViewById(R.id.num_picker_hour);
        np_util.minute = findViewById(R.id.num_picker_minute);
        np_util.initNumperPicker();

        // @param : 아이템 정보
        item_name = findViewById(R.id.item_name);
        item_image = findViewById(R.id.item_image);
        item_price = findViewById(R.id.item_price);
        item_memo = findViewById(R.id.item_memo);

        wish_item = new WishItem();
        noti_item = new NotiItem(); // @todo : 알림 등록 시에만 알림 객체를 생성하도록 수정하기
    }

    private WishItem getWishItem() {
        // @param : 사용자가 입력한 아이템 정보
        String get_item_name = item_name.getText().toString();
        String get_item_price = item_price.getText().toString();
        String get_item_memo = item_memo.getText().toString();

        // @param : 아이템 객체에 사용자 입력 정보 저장
        wish_item.user_id = user_id;
        wish_item.setFolder_id(null);

        /**
         * @brief : 아이템 정보에 대한 null값 예외처리
         * @TODO : 아이템 이미지에 대한 예외처리도 추가
         */
        // @brief : 상품명데이터 예외처리
        if(!get_item_name.equals("")){
            wish_item.setItem_name(get_item_name);
        }

        // @brief : 가격데이터 예외처리
        if(get_item_price.equals("")){
            wish_item.setItem_price(null);
        } else{
            wish_item.setItem_price(get_item_price); // @brief : 입력값으로 초기화
        }

        // @brief : 메모데이터 예외처리
        if(get_item_memo.equals("")){
            wish_item.setItem_memo(null);
        } else{
            wish_item.setItem_memo(get_item_memo);
        }

        // @todo : 알림 시간 예외처리
//        int cur_hour = now.get(Calendar.HOUR_OF_DAY) * 60;
//        int cur_minute = now.get(Calendar.MINUTE);
//        if(cur_hour + cur_minute >= hour.getValue()*60 + minute.getValue()){
//            Toast.makeText(this, "현재 시간 보다 이전 시간을 알림으로 지정할 수 없습니다.",Toast.LENGTH_SHORT).show();
//            return null;
//        }

        // @brief : 사용자가 선탣한 알림 유형과 날짜를 저장
        String noti_type = np_util.noti_types_array[np_util.type.getValue()];
        String noti_date = np_util.dates_server[np_util.date.getValue()] + " " + np_util.hour.getValue() + ":" + np_util.minute.getValue() * TIME_PICKER_INTERVAL + ":00";
        noti_item.setItem_notification_date(noti_date);
        noti_item.setItem_notification_type(noti_type);
        Log.i(TAG, "date: " + noti_date);
        Log.i(TAG, "type: " + noti_type);

        return wish_item;
    }

    /**
     * @brief : 사용자가 입력한 아이템 정보를 저장한다.
     */
    private void save() {
        wish_item = getWishItem(); // @brief : 생성된 아이템 객체 가져오기
//        if (wish_item == null){ // @todo : 알림 시간 예외처리
//            return;
//        }

        IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
        Call<ResponseBody> call = remote_service.insertItemInfo(wish_item);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // @brief : 정상적으로 통신 성공한 경우
                    String seq = null;
                    try {
                        seq = response.body().string();
                        // @brief : 사용자가 알림을 설정한 경우
                        if (!noti_item.getItem_notification_type().equals("알림 유형 선택")){ // @brief : 알림 유형을 지정한 경우
                            // @brief : noti_item(알림) 객체의 user_id, item_id 설정
                            noti_item.setUser_id(user_id);
                            noti_item.setItem_id(seq);

                            // @brief : FCM 디바이스 토큰 가져오기
                            noti_item.setToken(SaveSharedPreferences.getFCMToken(LinkSharingActivity.this));
                            addNoti(); // @brief : 알림 등록 요청
                        }
                        else
                            Log.i(TAG, "onResponse: " + "알림 선택 하지 않음");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    // @brief : 통신에 실패한 경우
                    Log.e(TAG, "Retrofit 통신 실패");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // @brief : 통신 실패 시 callback (예외 발생, 인터넷 끊김 등의 시스템 적 이유로 실패)
                Log.e(TAG, "서버 연결 실패");
            }
        });
    }

    /**
     * @brief : 사용자가 알림을 설정한 경우 알림정보를 저장한다.
     */

    private void addNoti(){
        Log.i(TAG, "addNoti : " + noti_item); // @deprecated : 테스트용
        IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
        Call<ResponseBody> call = remote_service.insertItemNoti(noti_item);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // @brief : 정상적으로 통신 성공한 경우
                    String seq = null;
                    try {
                        seq = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "알림 등록 성공 : " + seq);

                } else {
                    // @brief : 통신에 실패한 경우
                    Log.e(TAG, "알림 등록 오류" + response.message());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // @brief : 통신 실패 시 callback (예외 발생, 인터넷 끊김 등의 시스템 적 이유로 실패)
                Log.e(TAG, "서버 연결 실패" + t.getMessage());
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add: // @brief : "위시리스트에 추가하기" 버튼
                save(); // @brief : DB에 아이템 정보 저장
                Toast.makeText(this, "위시리스트에 추가되었습니다", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.cancel: // @brief : 우측 상단 취소 버튼
                finish();
                break;
        }
    }

    // @brief : price 속성을 포함하는 태그의 속성값이 숫자로만 구성된 문자열인지 검사
    public boolean isNumber(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    // @brief : 오픈그래프 메타태그 파싱을 통해 아이템 정보 가져오기
    @SuppressLint("StaticFieldLeak")
    public class JsoupAsyncTask extends AsyncTask {

        String this_url;
        String get_title, get_image, get_price; // @brief : 메타태그 내에서 가져올 content 속성값

        JsoupAsyncTask(String get_url) {
            // @brief : 인텐트로 받아온 url
            this_url = get_url;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Connection con = Jsoup.connect(this_url);
                wish_item.item_url = this_url;
                Document doc = con.get();

                // @brief : HTML의 head부분에 있는 오픈그래프 메타태그 가져오기
                Elements og_tags = doc.select("meta[property^=og:]");
                Elements price_tags = doc.select("meta[property^=product:]");

                if (og_tags.size() <= 0) {
                    return null;
                }

                // @brief : 상품명과 이미지에 해당하는 OGTag를 필터링
                for (int i = 0; i < og_tags.size(); i++) {
                    Element tag = og_tags.get(i);
                    String text = tag.attr("property");

                    if ("og:title".equals(text)) { // @brief : 타이틀 메타태그 내 content 속성값을 상품명으로 사용
                        get_title = tag.attr("content");
                    } else if ("og:image".equals(text)) { // @brief : 이미지 메타태그 내 내 content 속성값을 상품이미지로 사용
                        get_image = tag.attr("content");
                    }
                }

                // @brief : 가격에 해당하는 OGTag를 필터링
                if (price_tags.size() > 0) {
                    for (int i = 0; i < price_tags.size(); i++) {
                        Element price_tag = price_tags.get(i);
                        String text = price_tag.attr("property");

                        // @brief : 정규 표현식을 사용해서 price를 포함하는 속성값인지 검사
                        if (text.matches(".*[pP]rice.*")) {
                            String tmp_price = "";

                            tmp_price = price_tag.attr("content"); // @brief : 가격 메타태그 내 content 속성값을 상품 가격으로 사용

                            // @brief : 숫자로만 구성된 문자열인지 검사
                            if(isNumber(tmp_price)){
                                get_price = tmp_price;
                                break;
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) { // @brief : doInBackground 작업 후 작업
            super.onPostExecute(o);

            // @brief : 텍스트 뷰에 가져온 아이템 정보로 디스플레이
            item_name.setText(get_title);
            item_price.setText(get_price);

            // @brief : 아이템 객체의 상품명 초기화
            //wish_item.setItem_name(get_title);

            try { // @brief : 피카소로 이미지 로딩 속도 개선
                Picasso.get().load(get_image).into(item_image); // @brief : 가져온 이미지경로값으로 이미지뷰 디스플레이
                wish_item.setItem_image(get_image); // @brief : 아이템 객체의 이미지 초기화
            } catch (IllegalArgumentException i) {
                Log.d(TAG, "사진이 없음");
            }
        }
    }
}