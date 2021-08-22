package com.hyeeyoung.wishboard.add;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.RealPathUtil;
import com.hyeeyoung.wishboard.config.WindowPermission;
import com.hyeeyoung.wishboard.folder.FolderListActivity;
import com.hyeeyoung.wishboard.model.NotiItem;
import com.hyeeyoung.wishboard.model.WishItem;
import com.hyeeyoung.wishboard.noti.NotiSettingActivity;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.AwsS3Service;
import java.io.File;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;
import com.hyeeyoung.wishboard.util.DateFormatUtil;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewItemFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "아이템 수동 등록";

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
    private ConstraintLayout item_image_layout;
    private LinearLayout btn_folder, btn_noti;
    private ImageView item_image;
    private TextView save, noti_type, noti_date, folder_name;
    private EditText item_name, item_price, item_url, item_memo;
    public AwsS3Service aws_s3;
    private String time_stamp, image_path;
    private WishItem wish_item; //@brief : 서버연동 시 사용, 추가
    private NotiItem noti_item;
    private ImageButton add_photo_btn;
    // @param : 카메라, 갤러리 접근
    private File file;
    private Uri img_uri, photo_uri, album_uri;
    private String current_photo_path;
    private static final int FROM_CAMERA = 0;
    private static final int FROM_ALBUM = 1;
    private LinearLayout layout;
    private String user_id = "";
    private String type, date, f_name;
//    private SharedItemVM viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_item, container, false);
        init();

        if (SaveSharedPreferences.getUserId(this.getActivity()).length() != 0)
            user_id = SaveSharedPreferences.getUserId(this.getActivity());

        return view;
    }

    public void init(){
        item_image_layout = view.findViewById(R.id.item_image_layout);
        btn_folder = view.findViewById(R.id.btn_folder);
        btn_noti = view.findViewById(R.id.btn_noti);
        save = view.findViewById(R.id.save);
        folder_name = view.findViewById(R.id.folder_name);
        item_name = view.findViewById(R.id.item_name);
        item_price = view.findViewById(R.id.item_price);
        item_url = view.findViewById(R.id.item_url);
        noti_type = view.findViewById(R.id.noti_type);
        noti_date = view.findViewById(R.id.noti_date);
        item_memo = view.findViewById(R.id.item_memo);
        item_image = view.findViewById(R.id.item_image);
        layout = view.findViewById(R.id.layout);
        add_photo_btn = view.findViewById(R.id.add_photo_btn);

        item_image_layout.setOnClickListener(this);
        btn_folder.setOnClickListener(this);
        btn_noti.setOnClickListener(this);
        save.setOnClickListener(this);

        // @brief : TedPermission 라이브러리를 통해 카메라 권한 획득
        new WindowPermission(getContext()).setPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
        );
    }

    /**
     * @return 아이템 정보 객체
     * @brief : 사용자가 입력한 아이템 정보를 WishItem 객체에 저장해서 반환, 추후 사용자 입력값으로 바꿀 예정
     */
    private WishItem getWishItem() {
        WishItem wish_item = new WishItem();
        wish_item.user_id = user_id;
        wish_item.setFolder_id(null);

        // @brief : 사용자가 입력한 아이템데이터 가져오기
        wish_item.item_name = item_name.getText().toString();
        String get_item_price = item_price.getText().toString();
        String get_item_url = item_url.getText().toString().replace(" ", ""); //@ @brief : 링크로 이동 시 공백에 의한 예외를 방지하기위해 공백 처리
        String get_item_memo = item_memo.getText().toString();

        if(image_path != null && image_path.length() > 0){
            // @brief : 아이템 등록 시 파일명 중복 방지를 위해 파일명으로 등록 시간으로 지정
            time_stamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            wish_item.item_image = IRemoteService.IMAGE_URL + time_stamp;
        }

        // @brief : 아이템 가격, url, 메모에 대한 null값 예외처리
        // @brief : 가격데이터 예외처리
        if (get_item_price.isEmpty()) {
            wish_item.setItem_price(null);
        } else {
            wish_item.setItem_price(get_item_price);
        }

        // @brief : url데이터 예외처리
        if (get_item_url.isEmpty()) {
            wish_item.setItem_url(null);
        } else {
            wish_item.setItem_url(get_item_url);
        }

        // @brief : 메모데이터 예외처리
        if (get_item_memo.isEmpty()) {
            wish_item.item_memo = null;
        } else {
            wish_item.item_memo = get_item_memo;
        }

        return wish_item;
    }

    /**
     * @param : wish_item 사용자가 새로 입력한 아이템 객체
     * @return : 입력하지 않았다면 true, 입력했다면 false
     * @brief :사용자가 상품명과 이미지를 입력했는지를 확인
     */
    private boolean isNoData(WishItem wish_item) {
        String name = wish_item.item_name.trim();
        String img = wish_item.item_image;

        if (name.isEmpty() && TextUtils.isEmpty(img)){
            Toast.makeText(getContext(), "이미지와 상품명을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return true;
        } else if (name.isEmpty()){
            Toast.makeText(getContext(), "상품명을 작성해주세요.", Toast.LENGTH_SHORT).show();
            return true;
        } else if (TextUtils.isEmpty(img)){
            Toast.makeText(getContext(), "이미지를 업로드해주세요.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    /**
     * @brief : 사용자가 입력한 정보를 저장
     */
    private void save() {
        wish_item = getWishItem();

        // @brief : 아이템 이름을 입력하지 않은 경우
        if (isNoData(wish_item)) {
            //Toast.makeText(getContext(), "아이템 이름을 입력해주세요.", Toast.LENGTH_SHORT).show(); // @brief : 아이템 정보 입력을 요구
            return;
        }


        else{
            // @param : @ s3에 이미지 업로드를 위한 s3 객체
            aws_s3 = new AwsS3Service(getActivity().getApplicationContext());
            /**
             * @brief : S3에 이미지 파일 업로드
             * @param image_path : 이미지 파일 경로
             * @param time_stamp : 이미지파일명 중복 방지를 위한 현재 시간 값을 추가해서 파일명 지정함
             */
            aws_s3.uploadFile(new File(image_path), time_stamp);
            IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
            Call<ResponseBody> call = remote_service.insertItemInfo(wish_item);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // @brief : 서버연결 성공한 경우
                    if (response.isSuccessful()) {
                        String seq = null;
                        try {
                            seq = response.body().string();
                            if(!noti_type.equals(null)) {
                                noti_item = new NotiItem(user_id, seq, type, date);
                                noti_item.setToken(SaveSharedPreferences.getFCMToken(getContext()));
                                addNoti();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, seq);

                        // @brief : 디스플레이된 데이터 리셋
                        item_image.setImageResource(0);
                        item_name.setText("");
                        item_price.setText("");
                        item_url.setText("");
                        item_memo.setText("");
                        noti_type.setText("");
                        noti_date.setText("");
                        folder_name.setText("");

                        // @brief : 저장 완료 후 사진 등록을 위한 플러스 버튼 보이도록 변경
                        add_photo_btn.setVisibility(View.VISIBLE);

                        //viewModel.setIsUpdated(true); // @brief : update 여부 true
                        Toast.makeText(getContext(), "위시리스트에 추가했습니다.", Toast.LENGTH_SHORT).show(); // @brief : 아이템 정보 입력을 요구

                    } else { // @brief : 통신에 실패한 경우
                        Log.e(TAG, "Retrofit 통신 실패");
                        //viewModel.setIsUpdated(false); // @brief : update 여부 false
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // @brief : 통식 실패 ()시 callback (예외 발생, 인터넷 끊김 등의 시스템적 이유로 실패)
                    Log.e(TAG, "서버 연결 실패");
                    // viewModel.setIsUpdated(false); // @brief : update여부 false
                }
            });
        }
    }

    /**
     * @brief : 사용자가 상품 알림을 설정한 경우 알림을 저장
     */
    private void addNoti(){
        Log.i(TAG, "addNoti : " + noti_item);
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
                // @brief : 통식 실패 ()시 callback (예외 발생, 인터넷 끊김 등의 시스템적 이유로 실패)
                Log.e(TAG, "서버 연결 실패" + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_image_layout:
                makeDialog(); // @brief : 다이얼로그 디스플레이
                break;

            case R.id.btn_folder:
                Intent intent = new Intent(getActivity(), FolderListActivity.class);
                someActivityResultLauncher.launch(intent);
                break;

            case R.id.btn_noti:
                Intent intent_noti = new Intent(getActivity(), NotiSettingActivity.class);
                someActivityResultLauncher.launch(intent_noti);
                break;

            case R.id.save:
                save();
                break;
        }
    }

    // @brief : NotiSettingActivity에서 사용자가 입력한 알림 정보와 폴더 정보가 MainActivity를 거쳐서 이곳으로 전달 됨
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) { // @brief : 알림 정보의 경우
                    type = result.getData().getStringExtra("type");
                    date = result.getData().getStringExtra("date");
                    noti_type.setText(type);
                    noti_date.setText(DateFormatUtil.shortDateMDHM(date));
                    noti_type.setVisibility(View.VISIBLE);
                    noti_date.setVisibility(View.VISIBLE);

                } else if (result.getResultCode() == 1004) {  // @brief : 폴더 정보의 경우 //@TODO : 코드 번호 변경 필요
                    f_name = result.getData().getStringExtra("folder_name");
                    Log.i("폴", f_name);
                    folder_name.setText(f_name);
                    folder_name.setVisibility(View.VISIBLE);
                }
            });

    // @see : http://dailyddubby.blogspot.com/2018/04/107-tedpermission.html
    // @brief : 앨범 선택 클릭
    public void selectAlbum() {
        // @brief : 앨범 열기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, FROM_ALBUM);
    }

    /**
     * @TODO 사진촬영 클릭시 앱 중단되는 문제 발생, 해결 예정
     * @brief : 사진 찍기 클릭
     */

    public void takePhoto() {
        // @brief : 촬영 후 이미지 가져옴
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                File photo_file = null;
                try {
                    photo_file = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photo_file != null) {
                    Uri providerURI = FileProvider.getUriForFile(getContext(), getContext().getPackageName(), photo_file);
                    img_uri = providerURI;
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(intent, FROM_CAMERA);
                }
            }
        } else {
            Log.v("알림", "저장공간에 접근 불가능");
            return;
        }
    }

    // @TODO : 이미지뷰 클릭 시 dialog 생성하는 코드로 추후 인스타처럼 갤러리 이미지 선택을 먼저 띄운후 해당 프래그먼트로 이동하는 방식으로 변경예정.
    private void makeDialog() {
        selectAlbum(); // @brief : 우선 갤러리에서 이미지 선택을 기본으로 지정
//        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext()); // @ see : R.style.MyAlertDialogStyle 스타일 커스텀 가능
//        alt_bld.setTitle("사진 업로드").setIcon(R.drawable.cart).setCancelable(
//                false).setPositiveButton("사진촬영",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Log.v("알림", "다이얼로그 > 사진촬영 선택");
//                        // @brief : 사진 촬영 클릭
//                        takePhoto();
//                    }
//
//                }).setNeutralButton("앨범선택",
//
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogInterface, int id) {
//                        Log.v("알림", "다이얼로그 > 앨범선택 선택");
//                        // @brief : 앨범에서 선택
//                        selectAlbum();
//                    }
//
//                }).setNegativeButton("취소   ",
//
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Log.v("알림", "다이얼로그 > 취소 선택");
//                        // @brief : 취소 클릭. dialog 닫기.
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = alt_bld.create();
//        alert.show();
    }

    // @TODO : 카메라와 갤러리에서 가져온 이미지 패스를 처리한 후 S3로 업로드 및 DB에 이미지 경로 저장
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case FROM_ALBUM: {
                // @brief : 앨범에서 가져오기
                if (data.getData() != null) {
                    try {
                        File albumFile = null;
                        albumFile = createImageFile();
                        photo_uri = data.getData();
                        album_uri = Uri.fromFile(albumFile);

                        // @brief : sdk 버전에 맞게 이미지 파일 객체 생성을 위한 이미지 경로 설정
                        if (Build.VERSION.SDK_INT < 11) {
                            image_path = RealPathUtil.getRealPathFromURI_BelowAPI11(NewItemFragment.this.getContext(), photo_uri);
                            Log.d(TAG, Build.VERSION.SDK_INT + "");
                        } else if (Build.VERSION.SDK_INT < 19) {
                            Log.d(TAG, Build.VERSION.SDK_INT + "");
                            image_path = RealPathUtil.getRealPathFromURI_API11to18(NewItemFragment.this.getContext(), photo_uri);
                        } else {
                            Log.d(TAG, Build.VERSION.SDK_INT + "");
                            image_path = RealPathUtil.getRealPathFromURI_API19(NewItemFragment.this.getContext(), photo_uri);
                        }
                        Log.d(TAG, image_path + "--hello");

                        try {
                            file = new File(image_path); // @brief : 해당경로의 이미지 파일을 S3에 업로드

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i(TAG, "onActivityResult: " + "갤러리 이미지 적용 에러");
                        }

                        // @brief : 이미지뷰에 이미지 디스플레이
                        item_image.setImageURI(photo_uri);
                        add_photo_btn.setVisibility(View.GONE);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.v("알림", "앨범에서 가져오기 에러");
                    }
                }
                break;
            }

            case FROM_CAMERA: {
                // @brief : 촬영
                try {
                    Log.v("알림", "FROM_CAMERA 처리");
                    galleryAddPic();
                    // @brief : 이미지뷰에 이미지셋팅
                    item_image.setImageURI(img_uri);
                    add_photo_btn.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    // @brief : 카메라로 촬영한 이미지 생성하기
    public File createImageFile() throws IOException {
        String imgFileName = System.currentTimeMillis() + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "ireh");

        if (!storageDir.exists()) {
            Log.v("알림", "storageDir 존재 x " + storageDir.toString());
            storageDir.mkdirs();
        }

        Log.v("알림", "storageDir 존재함 " + storageDir.toString());
        imageFile = new File(storageDir, imgFileName);
        current_photo_path = imageFile.getAbsolutePath();

        return imageFile;

    }

    // @brief : 촬영한 이미지 저장하기
    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(current_photo_path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }
}