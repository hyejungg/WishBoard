package com.hyeeyoung.wishboard.folder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.FolderItem;
import com.hyeeyoung.wishboard.config.SharedFolderVM;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteFolderDialog extends DialogFragment implements View.OnClickListener {
    public static final String TAG = "삭제 경고창";
    public static final String TAG_EVENT_DIALOG = "delete_diolog";

    private Context context;
    private String folder_id;

    private SharedFolderVM viewModel;

    // @brief : 생성지점에 ViewModel 객체 생성
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(getActivity()).get(SharedFolderVM.class);
    }

    public DeleteFolderDialog(){}

    public static DeleteFolderDialog getInstance(){
        DeleteFolderDialog ddf = new DeleteFolderDialog();
        return ddf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // @brief : bundle 받아서 folder_id 초기화
        Bundle getArgs = getArguments();
        if(getArgs != null){
            folder_id = getArgs.getString("folder_id");
        }else{
            folder_id = "";
        }

        // @brief : 뷰 생성 및 뷰 내 아이템 초기화
        View v = inflater.inflate(R.layout.folder_delete_warning, container);
        Button cancel = (Button) v.findViewById(R.id.cancel);
        Button confirm = (Button) v.findViewById(R.id.confirm);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
        // @brief : setCancelable(false); 를 설정해두지 않아서 검은 영역 터치 시 dismiss() 발생
        //setCancelable(false);
        context = getContext();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            //@brief : 너비 지정
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel :
                Log.i(TAG, "취소 버튼 클릭");
                dismiss(); // @brief : 다이얼로그 닫기
                break;

            case R.id.confirm :
                Log.i(TAG, "확인 버튼 클릭");

                // @brief : 다이얼로그 종료 여부 FolderFragment에 전달
                deleteFolder(folder_id);
                dismiss(); // @brief : 다이얼로그 닫기
                break;
        }
    }
    /**
     * @brief : 폴더 삭제 요청
     * @param folder_id
     */
    private void deleteFolder(String folder_id){
        FolderItem item = new FolderItem(folder_id, 0, null, 0); //@brief : folder_id만 필요

        // @brief : 서버와 연동하여 폴더명 수정
        IRemoteService remoteService = ServiceGenerator.createService(IRemoteService.class);
        Call<ResponseBody> call = remoteService.deleteFolderInfo(item);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String seq = "";
                    try {
                        seq = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "Retrofit 통신 성공");
                    Log.i(TAG + "삭제", seq); //@deprecated : 성공여부 확인 test
                    Toast.makeText(context, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    viewModel.setIsUpdated(true); //update여부 true
                }else{
                    Log.i(TAG, "Retrofit 통신 실패");
                    Log.i(TAG, response.message());
                    viewModel.setIsUpdated(false); //update여부 false
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // @brief : 통신 실패 시 callback
                Log.e(TAG, "서버 통신 실패");
            }
        });
    }
}
