package com.hyeeyoung.wishboard.noti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.ItemAdapter;
import com.hyeeyoung.wishboard.adapter.NotiAdapter;
import com.hyeeyoung.wishboard.model.NotiItem;

import com.hyeeyoung.wishboard.model.NotiItem;
import com.hyeeyoung.wishboard.model.WishItem;

import java.util.ArrayList;

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
    private View view;
    RecyclerView recyclerView;
    NotiAdapter adapter;
    private ArrayList<NotiItem> notiList;
    private LinearLayoutManager linearLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_noti, container, false);
        init();
        return view;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recyclerview_noti_list);
        notiList = new ArrayList<>();
        adapter = new NotiAdapter(notiList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        addNoti(R.drawable.sample, "가방", "[재입고 알림]", "1시간 전");
        addNoti(R.drawable.sample, "가방", "[재입고 알림]", "1시간 전");
        addNoti(R.drawable.sample, "가방", "[재입고 알림]", "1시간 전");
        addNoti(R.drawable.sample, "가방", "[재입고 알림]", "1시간 전");
        addNoti(R.drawable.sample, "가방", "[재입고 알림]", "1시간 전");
        addNoti(R.drawable.sample, "가방", "[재입고 알림]", "1시간 전");
        addNoti(R.drawable.sample, "가방", "[재입고 알림]", "1시간 전");
        addNoti(R.drawable.sample, "가방", "[재입고 알림]", "1시간 전");

        adapter.notifyDataSetChanged();
    }
    // @deprecated
    private void addNoti(int icon, String mainText, String notiType, String notiDate) {
        NotiItem item = new NotiItem();
        item.setItem_image(icon);
        item.setItem_name(mainText);
        item.setNoti_type(notiType);
        item.setNoti_date(notiDate);
        notiList.add(item);
    }

    /* @deprecated
    private void addItem(Drawable icon, String mainText, String subText) {
        WishItem item = new WishItem();
        item.setItem_image(icon);
        item.setItem_name(mainText);
        item.setItem_price(subText);
        wishList.add(item);
    }*/
}