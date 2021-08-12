package com.hyeeyoung.wishboard.folder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.FolderAdapter;
import com.hyeeyoung.wishboard.cart.CartActivity;
import com.hyeeyoung.wishboard.model.FolderItem;
import com.hyeeyoung.wishboard.model.SharedFolderVM;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FolderFragment extends Fragment implements View.OnClickListener {
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
    private static final int EDITFOLDERDILOG = 1;

    private View view;
    private RecyclerView recyclerView;
    private FolderAdapter adapter;
    private ArrayList<FolderItem> foldersList;
    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Intent intent;
    private ImageButton cart, new_folder;
    private Context context;

    private String user_id = "";

    private SharedFolderVM viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_folder, container, false);
        context = view.getContext();
        if (SaveSharedPreferences.getUserId(this.getActivity()).length() != 0) {
            user_id = SaveSharedPreferences.getUserId(this.getActivity());
            Log.i(TAG, "user_id = " + user_id); // @deprecated : test용
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        selectFolderInfo(user_id);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // @brief : 생성지점에 ViewModel 객체 생성
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedFolderVM.class);
        // @brief : ViewModel에 저장한 값을 가져와 true인 경우(변경된 경우) 재조회
        viewModel.getIsUpdated().observe(this, aBoolean -> selectFolderInfo(user_id));
    }

    /**
     * @param user_id
     * @brief : 해당 유저 아이디에 맞는 폴더 정보를 보여줍니다.
     */
    private void selectFolderInfo(String user_id) {
        IRemoteService remoteService = ServiceGenerator.createService(IRemoteService.class);
        Call<ArrayList<FolderItem>> call = remoteService.selectFolderInfo(user_id);
        call.enqueue(new Callback<ArrayList<FolderItem>>() {
            @Override
            public void onResponse(Call<ArrayList<FolderItem>> call, Response<ArrayList<FolderItem>> response) {
                foldersList = response.body();

                // @brief : 가져온 폴더 정보가 없는 경우
                if (foldersList == null) {
                    foldersList = new ArrayList<>(); // @brief : 아이탬 배열 초기화
                }

                // @brief : 서버연결 성공한 경우
                if (response.isSuccessful()) {
                    if (foldersList.size() > 0) { // @brief : 가져온 폴더 정보가 하나 이상인 경우
                        Log.i(TAG, "Retrofit 통신 성공");
                        Log.i(TAG, foldersList + ""); // @deprecated : 테스트용
                        init(); // @brief : onCreateView 메서드에서 해당 위치로 옮김
                    }
                } else { // @brief : 통신에 실패한 경우
                    Log.e(TAG, "Retrofit 통신 실패");
                    Log.i(TAG, response.message());
                    init(); // @brief : onCreateView 메서드에서 해당 위치로 옮김
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FolderItem>> call, Throwable t) {
                // @brief : 통식 실패 ()시 callback (예외 발생, 인터넷 끊김 등의 시스템적 이유로 실패)
                Log.e(TAG, "서버 연결 실패");
                init();
            }
        });
    }

    private void init() {
        // @brief : 폴더 아이템 뷰 및 어댑터 초기화
        recyclerView = view.findViewById(R.id.recyclerview_folders_list);
        adapter = new FolderAdapter(foldersList, user_id);
        recyclerView.setAdapter(adapter);
        gridLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // @brief : 우측 상단의 장바구니와 새폴더 추가 버튼 별 리스너 등록
        cart = view.findViewById(R.id.cart);
        new_folder = view.findViewById(R.id.new_folder);

        cart.setOnClickListener(this);
        new_folder.setOnClickListener(this);


        // @brief : 새로고침 화면 초기화
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        // @brief : 새로고침이 발생한다면 selectFolderInfo() 발생
        swipeRefreshLayout.setOnRefreshListener(() -> {
            selectFolderInfo(user_id); //swipe 시 서버 다시 조회
            Log.i(TAG, "refresh__update 발생");
            swipeRefreshLayout.setRefreshing(false); //update 종료 알림
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // @brief : 장바구니 버튼 클릭 시 장바구니 화면으로 이동
            case R.id.cart:
                intent = new Intent(v.getContext(), CartActivity.class);
                v.getContext().startActivity(intent);
                break;
            // @brief : + 버튼 클릭 시 새폴더 추가에 관한 diolog 생성
            case R.id.new_folder:
                // @brief : diolog에 전달 할 값 bundle에 담기
                Bundle args = new Bundle();
                args.putInt("where", 1);
                args.putString("user_id", user_id);
                args.putString("folder_id", null);
                // @brief : diolog 생성
                EditFolderDiolog efd = EditFolderDiolog.getInstance();
                efd.setArguments(args);
//                efd.setTargetFragment(this, 1); //requestCode 전송
                efd.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), EditFolderDiolog.TAG_EVENT_DIALOG);
                break;
        }
    }
}
