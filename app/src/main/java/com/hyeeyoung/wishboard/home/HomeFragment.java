package com.hyeeyoung.wishboard.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.ItemAdapter;
import com.hyeeyoung.wishboard.cart.CartActivity;
import com.hyeeyoung.wishboard.config.SharedItemVM;
import com.hyeeyoung.wishboard.model.WishItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;
import com.hyeeyoung.wishboard.sign.SigninActivity;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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

    // @brief : 로그인한 유저의 아이템 정보를 가져오기
    @Override
    public void onStart() {
        super.onStart();
        //selectItemInfo(user_id);
    }

    // @todo : 아이템 정보를 수정하는 경우에만 수정된 내용을 반영해서 UI 업데이트하기 (2번 방법)
//    @Override
//    public void onResume() {
//        super.onResume();

//        Bundle bundle = getArguments();
//        if(bundle != null) {
//            Log.e("홈", "아이템 업데이트 여부 가져오기" + bundle.getBoolean("is_updated"));
//            if(bundle.getBoolean("is_updated"))
//                selectItemInfo(user_id);
//        }
//    }

    private static final String TAG = "홈";
    private View view;
    private RecyclerView recycler_view;
    private ItemAdapter adapter;
    private ArrayList<WishItem> wish_list;
    private GridLayoutManager grid_layout_manager;
    private Intent intent;
    private ImageButton cart;
    private Button[] buttons;
    private String user_id;
    private SharedItemVM viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    // @brief : 생성지점에 ViewModel 객체 생성
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(getActivity()).get(SharedItemVM.class);
        // @brief : ViewModel에 저장한 값을 가져와 true인 경우(변경된 경우) 재조회
        viewModel.getIsUpdated().observe(this, aBoolean -> selectItemInfo(user_id));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        if(SaveSharedPreferences.getUserId(this.getActivity()).length() != 0){
            user_id = SaveSharedPreferences.getUserId(this.getActivity());
            Log.i(TAG, "user_id = " + user_id); // @deprecated : test용
        }
        selectItemInfo(user_id);
        // @todo : 유저아이디를 가져오지 못하는 경우 예외처리하기
        return view;
    }

    /**
     * @brief : 서버에서 아이템 정보를 조회한다.
     * @param user_id 사용자 아이디
     */
    private void selectItemInfo(String user_id) {
        IRemoteService remoteService = ServiceGenerator.createService(IRemoteService.class);
        Call<ArrayList<WishItem>> call = remoteService.selectItemInfo(user_id);
        call.enqueue(new Callback<ArrayList<WishItem>>() {
            @Override
            public void onResponse(Call<ArrayList<WishItem>> call, Response<ArrayList<WishItem>> response) {
                wish_list = response.body(); // @brief : body()는, json 으로 컨버팅되어 객체에 담겨 리턴됨.

                // @brief : 가져온 아이템이 없는 경우
                if (wish_list == null) {
                    wish_list = new ArrayList<>(); // @brief : 아이탬 배열 초기화
                }

                // @brief : 서버연결 성공한 경우
                if(response.isSuccessful()){
                    if (wish_list.size() > 0) { // @brief : 가져온 아이템이 하나 이상인 경우
                        Log.i(TAG, "Retrofit 통신 성공");
                        Log.i(TAG, wish_list+""); // @deprecated : 테스트용
                        init(); // @brief : onCreateView 메서드에서 해당 위치로 옮김
                    }
                } else { // @brief : 통신에 실패한 경우
                    Log.e(TAG, "Retrofit 통신 실패");
                    Log.i(TAG, response.message());
                    init(); // @brief : onCreateView 메서드에서 해당 위치로 옮김
                }
            }

            @Override
            public void onFailure(Call<ArrayList<WishItem>> call, Throwable t) {
                // @brief : 통식 실패 ()시 callback (예외 발생, 인터넷 끊김 등의 시스템적 이유로 실패)
                Log.e(TAG, "서버 연결 실패");
                init();
            }
        });
    }

    /**
     * @brief : 뷰 초기화
     */
    private void init() {
        // @brief : 각 위시 아이템 뷰를 초기화
        recycler_view = view.findViewById(R.id.recyclerview_wish_list);
        adapter = new ItemAdapter(wish_list, user_id);
        recycler_view.setAdapter(adapter);
        grid_layout_manager = new GridLayoutManager(this.getActivity(), 2);
        recycler_view.setLayoutManager(grid_layout_manager);

        // @brief : 우측 상단의 장바구니 버튼
        cart = view.findViewById(R.id.cart);
        //TextView cart_item_cnt = view.findViewById(R.id.cart_item_cnt);

        // @todo : 카트 아이템 개수 출력
//        String cnt = RemoteLib.selectCartItemCnt(user_id);
//        Log.i(TAG, "init: " + cnt);
//        if(!cnt.equals("0")){
//            cart_item_cnt.setText(cnt);
//        }else{
//            cart_item_cnt.setVisibility(View.GONE);
//        }

        // @param : 폴더리스트가 있는 가로 스크롤 뷰 내 버튼들
        buttons = new Button[]{view.findViewById(R.id.all), view.findViewById(R.id.folder1), view.findViewById(R.id.folder2),
                view.findViewById(R.id.folder3), view.findViewById(R.id.folder4), view.findViewById(R.id.folder5),
                view.findViewById(R.id.folder6), view.findViewById(R.id.folder7), view.findViewById(R.id.folder8)};

        // @brief : 상단 폴더 버튼 별 리스너 등록
        cart.setOnClickListener(this);

        for(int i = 0; i < buttons.length; i++){
            buttons[i].setOnClickListener(this);
        }

        // @brief : 새로고침
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        // @brief : 새로고침이 발생할 경우
        swipeRefreshLayout.setOnRefreshListener(() -> {
            selectItemInfo(user_id); //swipe 시 서버 재조회
            Log.i(TAG, "refresh__update 발생");
            swipeRefreshLayout.setRefreshing(false); // @brief : update 종료 알림
        });
    }

    /**
     * @brief : 우측상단 장바구니버튼과 더보기 버튼을 클릭했을 때, 특정 폴더를 클릭할 때의 동작을 지정
     */
    @Override
    public void onClick(View v) {
        int position = 0; // @param : 버튼 배열 내 인덱스로 사용, 선택된 폴더의 인덱스를 표시
        // @brief : 초기 폴더명 버튼 텍스트 컬러를 그레이로 지정
        for(int i = 0; i < buttons.length ; i++){
            buttons[i].setTextColor(ContextCompat.getColor(HomeFragment.this.getContext(), R.color.darkGray));
        }
        switch (v.getId()){
            case R.id.cart:
                intent = new Intent(v.getContext(), CartActivity.class);
                v.getContext().startActivity(intent);
                break;

                // @TODO : 아이템 수정, 삭제 구현하기
                // @see : 우선 로그인 확인을 위해 임의의 버튼을 login 화면으로 연결되도록 설정
            case R.id.more:
                intent = new Intent(v.getContext(), SigninActivity.class);
                v.getContext().startActivity(intent);
                break;

            // @brief : 선택된 폴더명의 폰트 컬러 블랙으로 변경, 나머지 폰트 컬러는 그레이
            case R.id.all:
                position = 0;
                break;
            case R.id.folder1:
                position = 1;
                break;
            case R.id.folder2:
                position = 2;
                break;
            case R.id.folder3:
                position = 3;
                break;
            case R.id.folder4:
                position = 4;
                break;
            case R.id.folder5:
                position = 5;
                break;
            case R.id.folder6:
                position = 6;
                break;
            case R.id.folder7:
                position = 7;
                break;
            case R.id.folder8:
                position = 8;
                break;
        }
        buttons[position].setTextColor(ContextCompat.getColor(HomeFragment.this.getContext(), R.color.black));
    }

    // @todo : 아이템 정보를 수정하는 경우에만 수정된 내용을 반영해서 UI 업데이트하기 (1번 방법, 메인 엑티비티에서 받아와야함)
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if(data.getBooleanExtra("item_id", false));
//                selectItemInfo(user_id);
//        }
//    }
}