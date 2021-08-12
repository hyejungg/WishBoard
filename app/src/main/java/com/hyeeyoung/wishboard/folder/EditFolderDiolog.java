package com.hyeeyoung.wishboard.folder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.FolderItem;
import com.hyeeyoung.wishboard.model.SharedFolderVM;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;

import java.io.IOException;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFolderDiolog extends DialogFragment implements View.OnClickListener {
    public static final String TAG = "추가 및 수정창";
    public static final String TAG_EVENT_DIALOG = "edit&add_diolog";

    private ImageView folder_image;
    private EditText folder_name;

    private FolderItem folderItem = new FolderItem();
    private String user_id, folder_id, text_folder_name = "";
    private int where; // @param : 수정과 추가 시 동일한 UI를 사용하므로 해당 변수를 이용하여 타이틀 이름 변경
    private int num = 0;
    private Context context;

    private Intent intent = new Intent(); // @param : Fragment 간 전달용

    private SharedFolderVM viewModel;

    // @param : 폴더이미지 사진
    private int[] folder_images = {R.mipmap.ic_main_round, R.drawable.bag, R.drawable.sofa, R.drawable.shoes, R.drawable.twinkle,
            R.drawable.ring, R.drawable.orange, R.drawable.clothes, R.drawable.camera, R.drawable.bubble};
    // @param : 해당 다이어로그 타이틀
    private String[] titles = {"", "새폴더 추가", "폴더명 수정"};

    public static EditFolderDiolog getInstance() {
        EditFolderDiolog ddf = new EditFolderDiolog();
        return ddf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // @brief : bundle 받아서 where, user_id, folder_id 초기화
        Bundle getArgs = getArguments();
        if (getArgs != null) {
            where = getArgs.getInt("where");
            user_id = getArgs.getString("user_id");
            folder_id = getArgs.getString("folder_id");
        } else {
            where = 0;
            user_id = "";
            folder_id = "";
        }

        // @brief : 뷰 생성 및 뷰 내 아이템 초기화
        View v = inflater.inflate(R.layout.folder_edit, container);
        TextView title = (TextView) v.findViewById(R.id.title);
        folder_image = (ImageView) v.findViewById(R.id.folder_image);
        folder_name = (EditText) v.findViewById(R.id.folder_name);
        ImageButton cancel = (ImageButton) v.findViewById(R.id.cancel);
        Button add = (Button) v.findViewById(R.id.add);
        context = getContext();


        Log.i(TAG + "onCreateView()", where + user_id + folder_id); // @deprecated

        if (where == 1) title.setText(titles[where]);
        else if (where == 2) title.setText(titles[where]);
        else title.setText(titles[where]);

        folder_image.setOnClickListener(this);
        folder_name.setOnClickListener(this);
        cancel.setOnClickListener(this);
        add.setOnClickListener(this);
        // @brief : setCancelable(false); 를 설정해두지 않아서 검은 영역 터치 시 dismiss() 발생
        //setCancelable(false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            // @brief : 너비 지정
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point deviceSize = new Point();
            display.getSize(deviceSize);

            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = (int)(deviceSize.x * 0.9); // @brief : width는 전체 디바이스의 95%
            getDialog().getWindow().setAttributes(params);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @brief : 생성지점에 ViewModel 객체 생성
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedFolderVM.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.folder_image:
                /** @brief : 해당 이미지뷰 클릭 시 랜덤으로 대표이미지 아이콘 변경
                 * @see : 대표이미지 파일들은 배열에 담아두고 클릭 시 난수 발생시켜서 변경
                 * @brief : 저장될 이미지를 무작위로 리턴. 배열 크기 = 9 **/
                Random random = new Random();
                num = random.nextInt(9); // @brief : 1~9사이의 난수 발생
                folder_image.setImageResource(folder_images[num]);
                Log.i(TAG, "이미지뷰 클릭"); //@deprecated 확인용
                break;

            case R.id.cancel:
                dismiss(); // @brief : 다이얼로그 닫기
                break;

            case R.id.add:
                text_folder_name = folder_name.getText().toString();
                if (text_folder_name.equals("")) { //@brief : 빈 값인 경우
                    Toast.makeText(view.getContext(), "폴더명을 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    if (where == 1) { // @brief : 폴더 추가
                        addFolder();
                        dismiss();
                    } else if (where == 2) { // @brief : 폴더명 수정
                        editFolder();
                        dismiss();
                    } else { // @brief : where을 받아오지 못할 경우, 문제가 발생했다는 토스트를 띄우고 다이얼로그 종료
                        Toast.makeText(view.getContext(), "문제가 발생했습니다!", Toast.LENGTH_SHORT).show();
                        viewModel.setIsUpdated(false); //update여부 false
                        dismiss();
                    }
                }
                break;
        }
    }

    /**
     * @param where(해당 다이얼로그를 호출한 위치)
     * @brief : 서버와 연결 전 서버에 전송할 데이터를 folderItem에 저장
     */
    private void setData(int where) {
        folderItem.setUser_id(user_id);
        folderItem.setFolder_image(num); // idx번호로 저장 및 수정
        folderItem.setFolder_name(text_folder_name); //새 입력 값으로 저장 및 수정
        // @brief : 다이얼로그를 호출한 위치에 따라(수정/추가) data를 다르게 지정
        if (where == 1) {
            folderItem.setItem_count(0); // @brief : default;
        } else {
            folderItem.setFolder_id(folder_id);
            folderItem.getItem_count(); // @brief : 별도의 수정 없이 그대로
        }
    }

    /**
     * @brief : 새로 추가한 폴더 정보 저장
     */
    private void addFolder() {
        //@brief : 폴더 추가를 위한 FolderItem 초기화
        setData(1);

        // @brief : 서버와 연동하여 폴더명 추가
        IRemoteService remoteService = ServiceGenerator.createService(IRemoteService.class);
        Call<ResponseBody> call = remoteService.insertFolderInfo(folderItem);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String seq = "";
                    try {
                        seq = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "Retrofit 통신 성공");
                    Log.i(TAG + "추가", seq); //@deprecated : 성공여부 확인 test
                    Toast.makeText(context, "폴더가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    viewModel.setIsUpdated(true); //update여부 true
                } else {
                    Log.i(TAG, "Retrofit 통신 실패");
                    Log.i(TAG, response.message());
                    viewModel.setIsUpdated(false); //update여부 false
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // @brief : 통신 실패 시 callback
                Log.e(TAG, "서버 연결 실패");
            }
        });
    }

    /**
     * @brief : 새로 추가한 폴더 정보 수정
     */
    private void editFolder() {
        //@brief : 폴더 수정을 위한 FolderItem 초기화
        setData(2);

        // @brief : 서버와 연동하여 폴더명 수정
        IRemoteService remoteService = ServiceGenerator.createService(IRemoteService.class);
        Call<ResponseBody> call = remoteService.updateFolderInfo(folderItem);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String seq = "";
                    try {
                        seq = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "Retrofit 통신 성공");
                    Log.i(TAG, seq); //@deprecated : 성공여부 확인 test
                    Toast.makeText(context, "폴더가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    viewModel.setIsUpdated(true); //update여부 true
                } else {
                    Log.e(TAG, "Retrofit 통신 실패");
                    Log.i(TAG, response.message());
                    viewModel.setIsUpdated(false); //update여부 false
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // @brief : 통신 실패 시 callback
                Log.e(TAG, "서버 통신 실패");
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
