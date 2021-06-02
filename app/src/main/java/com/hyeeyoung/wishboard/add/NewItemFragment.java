package com.hyeeyoung.wishboard.add;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.folder.FolderListActivity;
import com.hyeeyoung.wishboard.model.WishItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.AwsS3Service;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewItemFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewItemFragment newInstance(String param1, String param2) {
        NewItemFragment fragment = new NewItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View view;
    // @param : 클릭 이벤트에서의 클릭 대상 View
    private ConstraintLayout item_image;
    private LinearLayout btn_folder, btn_noti;
    private ImageButton save;
    private EditText item_name, item_price, item_url, item_memo;
    public AwsS3Service aws_s3;
    private String time_stamp;
    WishItem wish_item; //@brief : 서버연동 시 사용, 추가
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_item, container, false);
        item_image = (ConstraintLayout) view.findViewById(R.id.item_image_layout);
        btn_folder = (LinearLayout) view.findViewById(R.id.btn_folder);
        btn_noti = (LinearLayout) view.findViewById(R.id.btn_noti);
        save = (ImageButton) view.findViewById(R.id.save);

        item_name = (EditText) view.findViewById(R.id.item_name);
        item_price = (EditText) view.findViewById(R.id.item_price);
        item_url = (EditText) view.findViewById(R.id.item_url);
        item_memo = (EditText) view.findViewById(R.id.item_url);

        item_image.setOnClickListener(this);
        btn_folder.setOnClickListener(this);
        btn_noti.setOnClickListener(this);
        save.setOnClickListener(this);

        //wish_item = getWishItem(); //@brief : 서버연동 시 사용, 추가
        return view;
    }

    /**
     * @brief : 사용자가 입력한 아이템 정보를 WishItem 객체에 저장해서 반환, 추후 사용자 입력값으로 바꿀 예정
     * @return 아이템 정보 객체
     */
    private WishItem getWishItem() {
        WishItem wish_item = new WishItem();
        wish_item.user_id = "1";
        wish_item.folder_id = "1";
        wish_item.item_img = "https://wishboardbucket.s3.ap-northeast-2.amazonaws.com/wishboard/20210519_095452";
        wish_item.item_name = "TSHIRT - BIG APPLE - IVORY";
        wish_item.item_price = "39900";
        wish_item.item_url = "https://www.29cm.co.kr/product/1040132";
        wish_item.item_memo = "none";
        /*
        * @brief : 추후 사용예정
        wish_item.item_name = item_name.getText().toString();
        wish_item.item_price = item_price.getText().toString();
        wish_item.item_url = item_url.getText().toString().replace(" ", "");
        wish_item.item_memo = item_memo.getText().toString();
        */
        return wish_item;
    }

    /**
     * @brief : 추후 사용예정, 사용자가 아이템 이름을 입력했는지를 확인
     * @param : newItem 사용자가 새로 입력한 정보 객체
     * @return : 입력하지 않았다면 true, 입력했다면 false
     */
//    private boolean isNoName(WishItem new_item) {
//        if ()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    /**
     * @brief : 사용자가 입력한 정보를 저장한다.
     */
    private void save() {
        final WishItem new_item = getWishItem();

        // @brief : 추후 사용예정, 아이템 이름을 입력하지 않은 경우
//        if (!isNoName(newItem)) {
//            Toast.makeText(getContext(), "아이템 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
        Call<ResponseBody> call = remote_service.insertItemInfo(new_item);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String seq = null;
                    try {
                        seq = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("Response 리턴값", seq);

                    wish_item.user_id = new_item.user_id;
                    wish_item.folder_id = new_item.folder_id;
                    wish_item.item_img = new_item.item_img;
                    wish_item.item_url = new_item.item_url;
                    wish_item.item_name = new_item.item_name;
                    wish_item.item_price = new_item.item_price;
                    wish_item.item_url = new_item.item_url;
                    Log.e("아이템 등록", "성공");
                } else {
                    Log.e("아이템 등록", "오류");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("아이템 등록", "서버 연결 실패");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_image_layout :
                time_stamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); // @param time_stamp : 파일명 중복 방지를 위해 파일명으로 타임스탬프를 지정
                aws_s3 = new AwsS3Service(getActivity().getApplicationContext()); // @param aws_s3 : @ s3 객체 생성

                /**
                 * @param uploadFile : 이미지 파일 업로드
                 * @brief "sdcard/Download/sample.jpg" : 업로드 테스트를 위한 임시 파일 경로 지정, 에뮬레이터로 이미지 파일 드래그(파일 복사) 후 테스트 가능
                 */
                aws_s3.uploadFile(new File("sdcard/Download/sample.jpg"), time_stamp);
            break;

            case R.id.btn_folder :
                Intent intent = new Intent(getActivity(), FolderListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_noti :
                // @ brief : 추후 사용
                break;

            case R.id.save :
                save();
                //new JSONTask().execute("http://13.125.227.20:3000/new_item/add");// @brief : AsyncTask 시작
                break;
        }
    }

    // @deprecated JSONTask 삭제 예정
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", "1");
                jsonObject.accumulate("folder_id", "1");
                jsonObject.accumulate("item_image", "none");
                jsonObject.accumulate("item_name", "TERRY SHORTS (3COLORS)");
                jsonObject.accumulate("item_price", "89,000");
                jsonObject.accumulate("item_url", "https://store.musinsa.com/app/goods/1916401");
                jsonObject.accumulate("item_memo", "youngjin");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://13.125.227.20:3000/new_item/add");
                    URL url = new URL(urls[0]);
                    //연결
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();
                    //서버로 보내기위해 스트림 만듦
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼 받아줌

                    //서버로 부터 데이터 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString(); // 서버로 부터 받은 값을 반환

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        //서버로 부터 받은 값을 출력
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("NewItemFragment.java", result);
            //view.setText(result)
        }
    }
}