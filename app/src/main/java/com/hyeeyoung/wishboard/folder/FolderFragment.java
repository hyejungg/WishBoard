package com.hyeeyoung.wishboard.folder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyeeyoung.wishboard.MainActivity;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.FolderAdapter;
import com.hyeeyoung.wishboard.cart.CartActivity;
import com.hyeeyoung.wishboard.model.FolderItem;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FolderFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FolderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FolderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FolderFragment newInstance(String param1, String param2) {
        FolderFragment fragment = new FolderFragment();
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

    private static final String TAG = "폴더";
    private static final int REQUEST_OK = 0;
    private View view;
    private RecyclerView recyclerView;
    private FolderAdapter adapter;
    private ArrayList<FolderItem> foldersList;
    private GridLayoutManager gridLayoutManager;

    private Intent intent;
    private ImageButton cart, new_folder;
    private FolderItem folderItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_folder, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        // @brief : 폴더 아이템 뷰 및 어댑터 초기화
        recyclerView = view.findViewById(R.id.recyclerview_folders_list);
        foldersList = new ArrayList<>();
        adapter = new FolderAdapter(foldersList);
        recyclerView.setAdapter(adapter);
        gridLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // @brief : 우측 상단의 장바구니와 새폴더 추가 버튼 별 리스너 등록
        cart = view.findViewById(R.id.cart);
        new_folder = view.findViewById(R.id.new_folder);

        cart.setOnClickListener(this);
        new_folder.setOnClickListener(this);

        // @deprecated : add()
        // @TODO : 서버통신 이후 init 위치 변경 필요 지금 이상태에서 addItem() 없애면 이전 값들은 저장이 안됨
        addItem(0, "name1", 32);
        addItem(1, "name2", 1);
        addItem(2, "name3", 5);
        addItem(3, "name4", 2);
        addItem(4, "name5", 124);
        addItem(5, "name6", 5151);
        addItem(6, "name7", 231);
        addItem(7, "name8", 241);
        addItem(8, "name9", 1);
        addItem(0, "name10", 23);
        addItem(1, "name10", 23);

        adapter.notifyDataSetChanged();
    }

    // @deprecated
    private void addItem(int icon, String name, int count) {
        FolderItem item = new FolderItem();
        item.setFolder_image(icon);
        item.setFolder_name(name);
        item.setItem_count(count);
        foldersList.add(item);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            // @brief : 장바구니 버튼 클릭 시 장바구니 화면으로 이동
            case R.id.cart:
                intent = new Intent(v.getContext(), CartActivity.class);
                v.getContext().startActivity(intent);
                break;
            case R.id.new_folder:
                intent = new Intent(v.getContext(), NewFolderActivity.class);
//                intent.putParcelableArrayListExtra("folderList", foldersList);
                getActivity().startActivityForResult(intent, REQUEST_OK);
                break;
        }
    }

    // @brief : 폴더 추가 시 해당 폴더 이름과 사진을 받아옴
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_OK){
            if (resultCode == RESULT_OK) { // @brief : 성공적으로 가져옴
                Toast.makeText(view.getContext(), "폴더 생성 성공", Toast.LENGTH_SHORT).show();
                folderItem = (FolderItem) data.getParcelableExtra("folderItem");
//                folderItem = (FolderItem) data.getSerializableExtra("folderItem");
                Log.i(TAG, "intent 받기 성공\n" + folderItem);

                // @brief : 폴더 추가 및 서버 연동, 현재 상태로 어댑터 유지
                foldersList.add(folderItem);
                adapter.notifyDataSetChanged();
            } else {   // RESULT_CANCEL
                Toast.makeText(view.getContext(), "폴더 생성 실패", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "intent 받기 실패");
            }
        }
    }
}
