package com.hyeeyoung.wishboard;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyeeyoung.wishboard.adapter.ItemAdapter;
import com.hyeeyoung.wishboard.cart.CartActivity;
import com.hyeeyoung.wishboard.model.WishItem;
import com.hyeeyoung.wishboard.service.SaveSharedPreferences;
import com.hyeeyoung.wishboard.sign.SigninActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private Drawable img; // @ deprecated
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    RecyclerView recycler_view;
    ItemAdapter adapter;
    private ArrayList<WishItem> wish_list;
    private GridLayoutManager grid_layout_manager;
    private Intent intent;
    private ImageButton cart, more;
    private Button[] buttons;

    private String user_id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();

        if(SaveSharedPreferences.getUserId(this.getActivity()).length() != 0)
            user_id = SaveSharedPreferences.getUserId(this.getActivity());
        // @deprecated : test용
        Log.i("Wish/HomeFragment", "user_id = " + user_id);

        return view;
    }

    private void init() {
        recycler_view = view.findViewById(R.id.recyclerview_wish_list);
        wish_list = new ArrayList<>();
//        adapter = new ItemAdapter(wish_list);
        adapter = new ItemAdapter(wish_list, user_id, "137"); // @TODO : 추후 item_id로 변경해야 함
        recycler_view.setAdapter(adapter);
        grid_layout_manager = new GridLayoutManager(this.getActivity(), 2);
        recycler_view.setLayoutManager(grid_layout_manager);
        cart = view.findViewById(R.id.cart);
        more = view.findViewById(R.id.more);

        // @param : 폴더리스트가 있는 가로 스크롤 뷰 내 버튼들
        buttons = new Button[]{view.findViewById(R.id.all), view.findViewById(R.id.folder1), view.findViewById(R.id.folder2),
                view.findViewById(R.id.folder3), view.findViewById(R.id.folder4), view.findViewById(R.id.folder5),
                view.findViewById(R.id.folder6), view.findViewById(R.id.folder7), view.findViewById(R.id.folder8)};

        // @brief : 리스너 등록
        cart.setOnClickListener(this);
        more.setOnClickListener(this);
        for(int i = 0; i < buttons.length; i++){
            buttons[i].setOnClickListener(this);
        }

        addItem(R.drawable.sample, "PRETZEL PUFF KNIT", "129,000");
        addItem(R.drawable.sample, "PRETZEL PUFF KNIT", "129,000");
        addItem(R.drawable.sample, "PRETZEL PUFF KNIT", "129,000");
        addItem(R.drawable.sample, "PRETZEL PUFF KNIT", "129,000");
        addItem(R.drawable.sample, "PRETZEL PUFF KNIT", "129,000");
        addItem(R.drawable.sample, "PRETZEL PUFF KNIT", "129,000");
        addItem(R.drawable.sample, "PRETZEL PUFF KNIT", "129,000");
        addItem(R.drawable.sample, "PRETZEL PUFF KNIT", "129,000");
        addItem(R.drawable.sample, "PRETZEL PUFF KNIT", "129,000");
        addItem(R.drawable.sample, "PRETZEL PUFF KNIT", "129,000");
        adapter.notifyDataSetChanged();
    }

    // @deprecated
    private void addItem(int icon, String mainText, String subText) {
        WishItem item = new WishItem();
//        item.setItem_image(icon);
        item.setItem_name(mainText);
        item.setItem_price(subText);
        wish_list.add(item);
    }

    private void getItem(String image, String mainText, String subText){

    }

    /*
    * @brief : 우측상단 장바구니버튼과 더보기 버튼을 클릭했을 때, 특정 폴더를 클릭할 때의 동작을 지정
    **/
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

                // @deprecated
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
}