package com.hyeeyoung.wishboard.folder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hyeeyoung.wishboard.R;

public class MoreFolderDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    public static final String TAG = "더보기 경고창";
    public static final String TAG_EVENT_DIALOG = "more_diolog";

    private Bundle args;
    private String user_id, folder_id, res_folder_name;
//    private int res_folder_image;

    public MoreFolderDialog(){}

    public static MoreFolderDialog getInstance(){
        MoreFolderDialog mfd = new MoreFolderDialog();
        return mfd;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // @brief : bundle 받아서 user_id, folder_id 초기화
        Bundle getArgs = getArguments();
        if(getArgs != null){
            user_id = getArgs.getString("user_id");
            folder_id = getArgs.getString("folder_id");
            res_folder_name = getArgs.getString("folder_name");
//            res_folder_image = getArgs.getInt("folder_image");
        }else{
            user_id = "";
            folder_id = "";
            res_folder_name = "";
//            res_folder_image = 0;
        }

        // @brief : 뷰 생성 및 뷰 내 아이템 초기화
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.folder_more, null);
        ImageButton cancel = (ImageButton) v.findViewById(R.id.cancel);
        LinearLayout btn_del = (LinearLayout) v.findViewById(R.id.btn_del) ;
        LinearLayout btn_upt = (LinearLayout) v.findViewById(R.id.btn_upt) ;
        cancel.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_upt.setOnClickListener(this);

        builder.setView(v);
        Dialog dialog = builder.create();

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        // @brief : 너비 지정
        try {
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point deviceSize = new Point();
            display.getSize(deviceSize);

            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = deviceSize.x;
            params.gravity = Gravity.BOTTOM; // @brief : 하단 지정
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
                dismiss(); // @brief : 다이얼로그 닫기
                break;

            case R.id.btn_del :
                Log.i(TAG, "폴더삭제 클릭");
                // @brief : 다이얼로그에 전달 할 값 bundle 에 담기
                args = new Bundle();
                args.putString("folder_id", folder_id);

                // @brief : 폴더 삭제에 관한 다이얼로그 생성
                DeleteFolderDialog ddf = DeleteFolderDialog.getInstance();
                ddf.setArguments(args);
                ddf.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(),
                        MoreFolderDialog.TAG_EVENT_DIALOG);
                dismiss(); //@brief : 다이얼로그 닫기
                break;

            case R.id.btn_upt :
                Log.i(TAG, "폴더수정 클릭");
                // @brief : 다이얼로그에 전달 할 값 bundle 에 담기
                args = new Bundle();
                args.putInt("where", 2);
                args.putString("user_id", user_id);
                args.putString("folder_id", folder_id);
                args.putString("folder_name", res_folder_name);
//                args.putInt("folder_image", res_folder_image);

                // @brief : 폴더명 수정에 관한 diolog 생성
                EditFolderDiolog efd = EditFolderDiolog.getInstance();
                efd.setArguments(args);
                efd.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(),
                        EditFolderDiolog.TAG_EVENT_DIALOG);
                dismiss(); //다이얼로그 닫기
                break;
        }
    }
}
