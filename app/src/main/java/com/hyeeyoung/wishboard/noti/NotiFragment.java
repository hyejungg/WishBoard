package com.hyeeyoung.wishboard.noti;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.NotiAdapter;
import com.hyeeyoung.wishboard.model.NotiItem;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotiFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotiFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NotiFragment newInstance(String param1, String param2) {
        NotiFragment fragment = new NotiFragment();
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

    private static final String TAG = "알림 조회";
    private View view;
    private RecyclerView recyclerView;
    private NotiAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String user_id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_noti, container, false);
        if(SaveSharedPreferences.getUserId(this.getActivity()).length() != 0)
            user_id = SaveSharedPreferences.getUserId(this.getActivity());

        selectNotiInfo(user_id); // @ brief : 알림 정보를 요청

        return view;
    }

    private void init(ArrayList<NotiItem> noti_list) {
        recyclerView = view.findViewById(R.id.recyclerview_noti_list);
        adapter = new NotiAdapter(noti_list);
        recyclerView.setAdapter(adapter);

        // @brief: recyclerView에 itemDecorator가 없는 경우, 구분선 추가 (처음 프래그먼트가 생성될 때만 구분선 추가)
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        }

        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        // @brief : 새로고침이 발생할 경우
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            selectNotiInfo(user_id); //swipe 시 서버 재조회
            Log.i(TAG, "refresh_update 발생");
            swipeRefreshLayout.setRefreshing(false); // @brief : update 종료 알림
        });
    }

    /**
     * @brief : 서버에서 알림 정보를 조회한다.
     * @param user_id 사용자 아이디
     */
    private void selectNotiInfo(String user_id) {
        IRemoteService remoteService = ServiceGenerator.createService(IRemoteService.class);
        Call<ArrayList<NotiItem>> call = remoteService.selectNotiInfo(user_id);
        call.enqueue(new Callback<ArrayList<NotiItem>>() {
            @Override
            public void onResponse(Call<ArrayList<NotiItem>> call, Response<ArrayList<NotiItem>> response) {
                ArrayList<NotiItem> noti_list = response.body();

                // @brief : 가져온 알림이 없는 경우
                if (noti_list == null) {
                    noti_list = new ArrayList<>(); // @brief : 아이탬 배열 초기화
                }

                // @brief : 서버연결 성공한 경우
                if(response.isSuccessful()){
                    if (noti_list.size() > 0) { // @brief : 가져온 알림이 하나 이상인 경우
                        Log.i(TAG, "Retrofit 통신 성공");
                        Log.i(TAG, noti_list +""); // @deprecated : 테스트용
                        init(noti_list); // @brief : onCreateView 메서드에서 해당 위치로 옮김
                    }
                } else { // @brief : 통신에 실패한 경우
                    Log.i(TAG, "Retrofit 통신 실패" + response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotiItem>> call, Throwable t) {
                // @brief : 통신 실패 ()시 callback (예외 발생, 인터넷 끊김 등의 시스템적 이유로 실패)
                Log.i(TAG, "서버 연결 실패" + t.getMessage());
            }
        });
    }
}