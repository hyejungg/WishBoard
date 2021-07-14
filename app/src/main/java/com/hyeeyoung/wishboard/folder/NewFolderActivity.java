package com.hyeeyoung.wishboard.folder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.FolderItem;

import java.util.Random;

public class NewFolderActivity extends AppCompatActivity {
    private static final String TAG = "새폴더추가";
    private int[] folder_images = {R.mipmap.ic_main_round, R.drawable.bag, R.drawable.sofa, R.drawable.shoes, R.drawable.twinkle,
            R.drawable.ring, R.drawable.orange, R.drawable.clothes, R.drawable.camera, R.drawable.bubble};

    ImageView folder_image;
    EditText folder_name;
    TextView folder_name_cnt;
    Button add;

    String text_folder_name = "";
    int imgidx_folder_image = 0;
    int num = 0;

    Intent intent;
    FolderItem folderItem = new FolderItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_folder);

        init();
    }

    // @brief : back버튼 혹은 x 버튼 클릭 시
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        // @brief : 각 아이템 뷰 초기화
        folder_image = findViewById(R.id.folder_image);
        folder_name = findViewById(R.id.folder_name);
        folder_name_cnt = findViewById(R.id.folder_name_cnt);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.cancel : // @brief : 액티비티 종료
                onStop();
                break;
            case R.id.add : // @brief : 폴더명과 폴더사진을 번들에 담아 FolderFragment.java로 전송
                text_folder_name = folder_name.getText().toString();
                if(text_folder_name.equals("")){ //@brief : 빈 값인 경우
                    Toast.makeText(this, "폴더명을 입력해주세요!", Toast.LENGTH_SHORT).show();
                }else{
                    setData(text_folder_name);
                    // @brief : bundle에 folderItem 정보를 담아 setResult에 담음
                    intent = new Intent();
                    Bundle bd = new Bundle();
                    bd.putParcelable("folderItem", folderItem); //번들로 객체를 보낼 때 .putParcelable()이용
//                    bd.putSerializable("folderItem", folderItem); //번들로 객체를 보낼 때 .putSerializable()이용
                    intent.putExtras(bd);
                    setResult(RESULT_OK, intent);
                    Log.i(TAG, String.valueOf(folderItem)); //@deprecated 확인용
                    onStop(); //finish() 불러서 정보 변경
                }
                break;
            case R.id.folder_image :
                /** @brief : 해당 이미지뷰 클릭 시 랜덤으로 대표이미지 아이콘 변경
                  * @see : 대표이미지 파일들은 배열에 담아두고 클릭 시 난수 발생시켜서 변경
                  * @brief : 저장될 이미지를 무작위로 리턴. 배열 크기 = 9 **/
                Random random = new Random();
                num = random.nextInt(9); // 1~9사이의 난수 발생
                folder_image.setImageResource(folder_images[num]);
                Log.i(TAG, "이미지뷰 클릭"); //@deprecated 확인용
                break;
        }
    }

    // @brief : 해당 액티비티의 내용을 바탕으로 folderItem 값 저장
    private void setData(String text_folder_name){
        text_folder_name = folder_name.getText().toString();

        folderItem.setFolder_id(0); //임의지정 @TODO
        folderItem.setFolder_image(num); // idx번호로 저장
        folderItem.setFolder_name(text_folder_name);
        folderItem.setItem_count(0); // @brief : 폴더를 새로 지정했으니 폴더 내 항목 값은 0
    }
}