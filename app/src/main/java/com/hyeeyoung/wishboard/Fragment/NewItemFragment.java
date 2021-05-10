package com.hyeeyoung.wishboard.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.hyeeyoung.wishboard.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewItemFragment extends Fragment {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_item, container, false);

        view.setOnClickListener(onClickListener); // @see : 이렇게 되면 뷰 아무거나 클릭해도 onClick이 불릴 것 같음 어쨌든 수정 필요

        return view;
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_folder :
                case R.id.item_name :
                case R.id.item_price :
                case R.id.item_url :
                case R.id.btn_noti : // @see : 버튼 클릭 시 날짜 지정 / 상품 알림 유형 제시
                case R.id.item_memo :
            }
        }
    };
}