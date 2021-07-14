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

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.NotiAdapter;
import com.hyeeyoung.wishboard.model.NotiItem;

import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotiFragment extends Fragment {
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotiFragment.
     */
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
    RecyclerView recyclerView;
    NotiAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private String user_id = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_noti, container, false);
        if(SaveSharedPreferences.getUserId(this.getActivity()).length() != 0)
            user_id = SaveSharedPreferences.getUserId(this.getActivity());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        selectNotiInfo(user_id); // @ brief : 알림 정보를 요청
    }

    @Override
    public void onStop() {
        super.onStop();
        updateNotiRead(user_id); // @ brief : 알림 읽음 여부 업데이트 업데이트 요청
    }

    private void init(ArrayList<NotiItem> noti_list) {
        recyclerView = view.findViewById(R.id.recyclerview_noti_list);
        adapter = new NotiAdapter(noti_list);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
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

    /**
     * @brief : 서버로 알림 읽음 여부 업데이트를 요청한다. (읽지 않은 알림을 읽음으로 업데이트)
     * @param user_id 사용자 아이디
     */
    private void updateNotiRead(String user_id) {
        IRemoteService remoteService = ServiceGenerator.createService(IRemoteService.class);
        Call<ResponseBody> call = remoteService.updateNotiRead(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // @brief : 서버연결 성공한 경우
                if (response.isSuccessful()) {
                    String seq = null;
                    try{
                        seq = response.body().string();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.i(TAG, "업데이트 성공 [seq : " + seq + "]");
                } else {
                    // @brief : 통신에 실패한 경우
                    Log.e(TAG, "업데이트 오류 [message : " + response.message() + "]");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // @brief : 통신 실패 ()시 callback (예외 발생, 인터넷 끊김 등의 시스템적 이유로 실패)
                Log.i(TAG, "서버 연결 실패 [message : " + t.getMessage() + "]");
            }
        });
    }
}