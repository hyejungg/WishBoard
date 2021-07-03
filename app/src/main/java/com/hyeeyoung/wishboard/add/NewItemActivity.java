package com.hyeeyoung.wishboard.add;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.RealPathUtil;
import com.hyeeyoung.wishboard.config.WindowPermission;
import com.hyeeyoung.wishboard.folder.FolderListActivity;
import com.hyeeyoung.wishboard.model.WishItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.AwsS3Service;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewItemActivity extends AppCompatActivity {

    private static final String TAG = "아이템 정보 수정";
    private ConstraintLayout item_image_layout;
    private LinearLayout btn_folder, btn_noti, layout;
    private ImageButton save;
    private ImageView item_image;
    private EditText item_name, item_price, item_url, item_memo;
    public AwsS3Service aws_s3;
    private String time_stamp, image_path, current_photo_path, item_id;
    private WishItem wish_item;

    // @ brief : 카메라, 갤러리 접근
    private File file;
    private Uri img_uri, photo_uri, album_uri;
    private static final int FROM_CAMERA = 0;
    private static final int FROM_ALBUM = 1;
    private boolean is_modified_image = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_item);
        init();

        // @brief : 아이템 상세정보 화면에서 받아온 정보로 각종 뷰에 값을 디스플레이
        try{
            Intent intent = getIntent();
            String[] item_info = intent.getStringArrayExtra("item_info");
            if(item_info.length > 0){
                item_name.setText(intent.getStringExtra("item_name"));
                item_id = item_info[0];
                Log.i(TAG, "onCreate: " + item_id);
                item_name.setText(item_info[1]);
                image_path = item_info[2];
                try {
                    Picasso.get().load(item_info[2]).into(item_image);
                } catch (IllegalArgumentException i) {
                    Log.d(TAG, "아이템 사진 없음");
                }
                item_price.setText(item_info[3]);
                item_url.setText(item_info[4]);
                item_memo.setText(item_info[5]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void init(){
        item_image_layout = findViewById(R.id.item_image_layout);
        btn_folder =  findViewById(R.id.btn_folder);
        btn_noti = findViewById(R.id.btn_noti);
        save = findViewById(R.id.save);

        item_name = findViewById(R.id.item_name);
        item_price = findViewById(R.id.item_price);
        item_url = findViewById(R.id.item_url);
        item_memo = findViewById(R.id.item_memo);
        item_image = findViewById(R.id.item_image);
        layout = findViewById(R.id.layout);

        // @brief : TedPermission 라이브러리 -> 카메라 권한 획득
        new WindowPermission(this).setPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
        );
    }

    /**
     * @param : wish_item 사용자가 새로 입력한 아이템 객체
     * @return : 입력하지 않았다면 true, 입력했다면 false
     * @brief :사용자가 상품명을 입력했는지를 확인
     */
    private boolean isNoName(String item_name) {
        if (item_name.trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 아이템 수정을 서버에 요청한다.
     */
    public void updateItem() {

        // @brief : 사용자가 입력한 아이템데이터 가져오기
        String get_item_name = item_name.getText().toString();
        String get_item_price = item_price.getText().toString();
        String get_item_url = item_url.getText().toString().replace(" ", ""); //@ @brief : 링크로 이동 시 공백에 의한 예외를 방지하기위해 공백 처리
        String get_item_memo = item_memo.getText().toString();

        /**
         * @brief : 아이템 가격, url, 메모에 대한 null값 예외처리
         */
        // @brief : 가격데이터 예외처리
        if (get_item_price.isEmpty()) {
            get_item_price = null;
        }

        // @brief : url데이터 예외처리
        if (get_item_url.isEmpty()) {
            get_item_url = null;
        }
        // @brief : 메모데이터 예외처리
        if (get_item_memo.isEmpty()) {
            get_item_memo = null;
        }

        if(is_modified_image){ // @brief : 갤러리 이미지로 아이템 이미지가 수정된 경우
            time_stamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); // @brief : 아이템 등록 시 파일명 중복 방지를 위해 파일명으로 등록 시간으로 지정, 추후 파일명도 추가할 예정
            String get_item_image = IRemoteService.IMAGE_URL + time_stamp;
            aws_s3 = new AwsS3Service(getApplicationContext()); // @param aws_s3 : @ s3에 이미지 업로드를 위한 s3 객체 생성
            /**
             * @brief : S3에 이미지 파일 업로드
             * @param image_path : 이미지 파일 경로
             * @param time_stamp : 이미지파일명 중복 방지를 위한 현재 시간 값을 추가해서 파일명 지정함
             */
            aws_s3.uploadFile(new File(image_path), time_stamp);
            wish_item = new WishItem(item_id, null, null, get_item_image, get_item_name, get_item_price, get_item_url, get_item_memo);
        } else { // @brief : 이미지를 수정하지 않은 경우
            wish_item = new WishItem(item_id, null, null, image_path, get_item_name, get_item_price, get_item_url, get_item_memo);
        }

        IRemoteService remoteService = ServiceGenerator.createService(IRemoteService.class);
        Call<WishItem> call = remoteService.updateItem(item_id, wish_item);
        call.enqueue(new Callback<WishItem>() {
            @Override
            public void onResponse(Call<WishItem> call, Response<WishItem> response) {
                if (response.isSuccessful()) {
                    // @brief : 정상적으로 통신 성공한 경우
                    Log.i(TAG, "성공");
                    Toast.makeText(NewItemActivity.this, "위시리스트가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // @brief : 통신에 실패한 경우
                    Log.e(TAG, "오류");
                }
            }
            @Override
            public void onFailure(Call<WishItem> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_image_layout: // @brief : 이미지를 클릭한 경우
                //makeDialog(); // @brief : 다이얼로그 디스플레이
                selectAlbum();
                break;

            case R.id.btn_folder: // @todo : 폴더 구현 후 작성하기
                Intent intent = new Intent(NewItemActivity.this, FolderListActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_noti: // @todo : 알림 구현 후 작성하기
                break;

            case R.id.save: // @brief : 저장 버튼을 클릭한 경우
                // @brief : 아이템 이름을 입력하지 않은 경우
                if (isNoName(item_name.getText().toString())) {
                    //new CustumSnackbar(getView(), "아이템 이름을 입력해주세요.", Snackbar.LENGTH_SHORT).show(); // @brief : 스낵바 띄우기
                    Toast.makeText(this, "아이템 이름을 입력해주세요.", Toast.LENGTH_SHORT).show(); // @brief : 아이템 정보 입력을 요구
                    return;
                }
                // @brief : 수정한 아이템 정보로 서버에 업데이트를 요청
                updateItem();
                Intent return_intent = new Intent();
                setResult(RESULT_OK, return_intent); // @brief : itemDetailActivty로 복귀하면서 UI 업데이트를 위해 업데이트(RESULT_OK)결과 전송

                // @brief : 변경사항 적용 후 0.7 초 뒤에 액티비티 종료하는 handler 설정
                new Handler().postDelayed(new Runnable() {
                   @Override
                    public void run() {
                        finish(); // @brief : 0.7 초 뒤에 액티비티 종료
                    }
                }, 700);

                break;
        }
    }

    // @todo : NewItemFragment와 중복되는 함수들로 추후 클래스로 따로 뺄 예정
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
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photo_file = null;
                try {
                    photo_file = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photo_file != null) {
                    Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photo_file);
                    img_uri = providerURI;
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(intent, FROM_CAMERA);
                }
            }
        } else {
            Log.v(TAG, "알림 : 저장공간에 접근 불가능");
            return;
        }
    }

    // @todo : 카메라와 갤러리에서 가져온 이미지 패스를 처리한 후 S3로 업로드 및 DB에 이미지 경로 저장
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
                            image_path = RealPathUtil.getRealPathFromURI_BelowAPI11(this, photo_uri);
                            Log.d(TAG, Build.VERSION.SDK_INT + "");
                        } else if (Build.VERSION.SDK_INT < 19) {
                            Log.d(TAG, Build.VERSION.SDK_INT + "");
                            image_path = RealPathUtil.getRealPathFromURI_API11to18(this, photo_uri);
                        } else {
                            Log.d(TAG, Build.VERSION.SDK_INT + "");
                            image_path = RealPathUtil.getRealPathFromURI_API19(this, photo_uri);
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
                        Log.i(TAG, "onActivityResult: " + photo_uri);
                        is_modified_image = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.v(TAG, "알림 : 앨범에서 가져오기 에러");
                    }
                }
                break;
            }

            case FROM_CAMERA: {
                // @brief : 촬영
                try {
                    Log.v(TAG, "알림 : FROM_CAMERA 처리");
                    galleryAddPic();
                    // @brief : 이미지뷰에 이미지셋팅
                    item_image.setImageURI(img_uri);

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
            Log.v(TAG, "알림 : storageDir 존재 x " + storageDir.toString());
            storageDir.mkdirs();
        }

        Log.v(TAG, "알림 : storageDir 존재함 " + storageDir.toString());
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
        sendBroadcast(mediaScanIntent);
//        new CustumSnackbar(getView(), "사진이 저장되었습니다", Snackbar.LENGTH_SHORT).show();
    }
}