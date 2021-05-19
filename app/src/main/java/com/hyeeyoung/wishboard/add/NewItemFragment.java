package com.hyeeyoung.wishboard.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.folder.FolderListActivity;
import com.hyeeyoung.wishboard.service.AwsS3Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public AwsS3Service aws_s3;
    private String time_stamp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_item, container, false);
        item_image = (ConstraintLayout) view.findViewById(R.id.item_image_layout);
        btn_folder = (LinearLayout) view.findViewById(R.id.btn_folder);
        btn_noti = (LinearLayout) view.findViewById(R.id.btn_noti);
        item_image.setOnClickListener(this);
        btn_folder.setOnClickListener(this);
        btn_noti.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
//  Toast.makeText(getActivity(), "클릭", Toast.LENGTH_SHORT).show(); // @deprecated : click event test용
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
        }
    }
}