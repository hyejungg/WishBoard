package com.hyeeyoung.wishboard.add;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hyeeyoung.wishboard.adapter.SpinnerAdapter;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.NotiItem;
import com.hyeeyoung.wishboard.model.WishItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;
import com.squareup.picasso.Picasso;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinkSharingActivity extends AppCompatActivity {

    // @param : 알림 날짜 넘버피커 변수
    private NumberPicker date, hour, minute;
    /**
     * @param : 디스플레이 될 날짜를 담는 문자열 배열
     * dates[] : 팝업창에 디스플레이할 날짜 배열로, 포맷은 MMM dd일 EEE로 설정함
     * dates_server[] : DB에 저장될 날짜 배열로 datetime 타입 포맷인 yyyy-mm-dd hh:mm:ss로 설정함
     */
    String dates[], dates_server[];
    Calendar now = Calendar.getInstance();

    // @param : 알림 유형 스피너 변수
    ArrayList<String> noti_types_array;
    Spinner noti_type_spinner;
    SpinnerAdapter spinner_adapter;

    // @param : 아이템정보가 디스플레이될 뷰를 가리키는 변수
    TextView item_name;
    EditText item_price, item_memo;
    ImageView item_image;

    // @brief : 위시리스트에 아이템 저장을 위한 아이템 객체와 알림 객체
    WishItem wish_item;
    NotiItem noti_item;

    //TextView won;
    String site_url = ""; // @param : 가져온 데이터 보관
    String user_id = "";
    //private String item_id, noti_type, noti_date; // @todo : 알림 등록 시에만 알림 객체를 설정할 경우에 사용될 알림 설정 값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_sharing);

        // @param : 외부에서 공유를 통해 데이터 받기
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType(); // @param : MIME type 받기

        // @param : 변수 초기화
        init();
        initNumperPicker();

        if(SaveSharedPreferences.getUserId(this).length() != 0)
            user_id = SaveSharedPreferences.getUserId(this);

        // @param : 알림 유형 스피너에서 선택된 아이템 처리 이벤트
        noti_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // @brief : 추후 작성 예정
                noti_item.setItem_notification_type(noti_types_array.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // @brief : 추후 작성 예정
            }
        });

        // @breif : 전송 중인 데이터 처리
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            // @breif : 전송데이터 타입이 텍스트인 경우
            if ("text/plain".equals(type)) {
                site_url = intent.getStringExtra(Intent.EXTRA_TEXT); // @params : site url은 String에 보관. DB 저장용
                Log.i("LinkTest", "handleSendText: " + site_url);
                new JsoupAsyncTask(site_url).execute();
            }
        }
    }

    void init() {
        // @param : 알림 유형 스피너 관련 변수
        noti_types_array = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.noti_types_array)));
        noti_type_spinner = findViewById(R.id.noti_type_spinner);
        spinner_adapter = new SpinnerAdapter(this, noti_types_array);
        noti_type_spinner.setAdapter(spinner_adapter);

        // @param : 알림 날짜 넘버피커 관련 변수
        date = findViewById(R.id.num_picker_date);
        hour = findViewById(R.id.num_picker_hour);
        minute = findViewById(R.id.num_picker_minute);
        dates = getDatesFromCalender();
        dates_server = getDatesFromCalenderForServer();

        item_name = findViewById(R.id.item_name);
        item_image = findViewById(R.id.item_image);
        item_price = findViewById(R.id.item_price);
        item_memo = findViewById(R.id.item_memo);
        //won = findViewById(R.id.won);

        wish_item = new WishItem();
        noti_item = new NotiItem();
    }

    // @FIXME : 알림 날짜 초기화 함수로 실행 시 앱 중단
    void initNumperPicker() {
        // @brief : 날짜 넘버피커 설정
        date.setMinValue(0);
        date.setMaxValue(dates.length - 1);
        date.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return dates[value];
            }
        });
        date.setDisplayedValues(dates); //@param : 디스플레이될 날짜 값 설정
        date.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        // @brief : 시간 설정, 0~23시 까지 넘버피커 설정
        hour.setMinValue(0); // @param : 범위의 최소값을 설정
        hour.setMaxValue(23); // @param : 범위의 최댓값을 설정
        hour.setValue(now.get(Calendar.HOUR_OF_DAY)); // @param : 초기 설정값 현재 시간으로 설정
        hour.setWrapSelectorWheel(true); // @param : +/- 버튼디자인에서 휠스크롤 디자인으로 변경
        hour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // @param : 키보드로 값을 수정하지 못하도록 포커스 막기

        // @brief :  분 설정, 0~59분 까지 넘버피커 설정
        minute.setMinValue(0);
        minute.setMaxValue(59);
        minute.setValue(now.get(Calendar.MINUTE));
        minute.setWrapSelectorWheel(true);
        minute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        /**
         * @see : 넘버피커 참고사이트
         * https://stackoverflow.com/questions/35295760/custom-date-time-picker-using-numberpicker-and-fragmentdialog-in-android
         * https://developer.android.com/guide/topics/resources/string-resource?hl=ko#java
         */
    }

    // @brief : 날짜 넘버피커 내 디스플레이 될 날짜값 배열 반환
    private String[] getDatesFromCalender() {
        Calendar c = Calendar.getInstance();
        List<String> dates = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("MMM dd일 EEE"); // @param : 날짜 포맷 지정, MMM과 EEE는 각각 한자리 값(6월, 수)로 표현하기 위한 포맷임

        // @brief : 현재부터 90일 후까지의 날짜 배열 생성
        for (int i = 0; i < 90; i++) {
            c.add(Calendar.DATE, 1);
            dates.add(dateFormat.format(c.getTime()));
        }

        return dates.toArray(new String[dates.size() - 1]);
    }

    // @brief : 서버에 저장될 날짜값 배열 반환
    private String[] getDatesFromCalenderForServer() {
        Calendar c = Calendar.getInstance();
        List<String> dates_server = new ArrayList<>();
        DateFormat dateFormat_server = new SimpleDateFormat("yyyy-MM-dd"); // @param : 서버에 저장될 날짜 포맷 지정

        // @brief : 현재부터 90일 후까지의 날짜 배열 생성
        for (int i = 0; i < 90; i++) {
            c.add(Calendar.DATE, 1);
            dates_server.add(dateFormat_server.format(c.getTime()));
        }

        return dates_server.toArray(new String[dates_server.size() - 1]);
    }

    private WishItem getWishItem() {
        wish_item.user_id = user_id;
        wish_item.folder_id = "1"; // @todo : 폴더 연동 후 수정

        String get_item_name = item_name.getText().toString();
        String get_item_price = item_price.getText().toString();
        String get_item_memo = item_memo.getText().toString();

        /**
         * @brief : 아이템 정보에 대한 null값 예외처리
         * @TODO : 아이템 이미지에 대한 예외처리도 추가
         */
        // @brief : 상품명데이터 예외처리
        if(!get_item_name.equals("")){ //@ brief : 입력값으로 초기화
            wish_item.setItem_name(get_item_name); // // @ brief : 사용자의 입력 값이 있는 경우
        }

        // @brief : 가격데이터 예외처리
        if(get_item_price.equals("")){ // @ brief : 공유페이지에서 가져온 가격데이터 또는 사용자의 입력 값이 없는 경우 null로 초기화
            wish_item.setItem_price(null);
        } else{ // @ brief : 사용자의 입력 값이 있는 경우
            wish_item.setItem_price(get_item_price); // @ brief : 입력값으로 초기화
        }

        // @brief : 메모데이터 예외처리
        if(get_item_memo.equals("")){ // @ brief : 사용자가 메모를 입력하지 않은 경우
            wish_item.setItem_memo(null);
        } else{ // @ brief : 사용자가 메모를 입력한 경우
            wish_item.setItem_memo(get_item_memo);
        }
        String noti_date = dates_server[date.getValue()] + " " + hour.getValue() + ":" + minute.getValue() + ":00";
        noti_item.setItem_notification_date(noti_date);
        Log.i("링크공유로 아이템 등록", "date: " + noti_date);

        return wish_item;
    }

    /**
     * @brief : 사용자가 입력한 아이템 정보를 저장한다.
     */
    private void save() {
        wish_item = getWishItem(); // @brief : 생성된 아이템 객체 가져오기

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
                            // @ noti_item(알림) 객체의 user_id, item_id 설정
                            noti_item.setUser_id(user_id);
                            noti_item.setItem_id(seq);
                            addNoti(); // @brief : 알림 등록 요청
                        }
                        else
                            Log.i("알림 등록", "onResponse: " + "알림 선택 하지 않음");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("아이템 간편 등록", seq);

                } else {
                    // @brief : 통신에 실패한 경우
                    Log.e("아이템 간편 등록", "Retrofit 통신 실패");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // @brief : 통식 실패 ()시 callback (예외 발생, 인터넷 끊김 등의 시스템적 이유로 실패)
                Log.e("아이템 간편 등록", "서버 연결 실패");
            }
        });
    }

    /**
     * @brief : 사용자가 알림을 설정한 경우 알림정보를 저장한다.
     */

    private void addNoti(){
        Log.i("알림 정보 확인하기", "addNoti: " + noti_item);
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
                    Log.e("알림 등록", "성공 : " + seq);

                } else {
                    // @brief : 통신에 실패한 경우
                    Log.e("알림 등록", "오류" + response.message());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // @brief : 통식 실패 ()시 callback (예외 발생, 인터넷 끊김 등의 시스템적 이유로 실패)
                Log.e("Notification 등록", "서버 연결 실패" + t.getMessage());
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
                Log.d("checkings", "사진이 없음");
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
    }
}