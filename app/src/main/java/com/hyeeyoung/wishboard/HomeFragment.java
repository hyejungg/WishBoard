package com.hyeeyoung.wishboard;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyeeyoung.wishboard.adapter.ItemAdapter;
import com.hyeeyoung.wishboard.cart.CartActivity;
import com.hyeeyoung.wishboard.model.WishItem;

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
    RecyclerView recyclerView;
    ItemAdapter adapter;
    private ArrayList<WishItem> wishList;
    private GridLayoutManager gridLayoutManager;
    private Intent intent;
    private ImageButton cart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();

        return view;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recyclerview_wish_list);
        wishList = new ArrayList<>();
        adapter = new ItemAdapter(wishList);
        recyclerView.setAdapter(adapter);
        gridLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        cart = view.findViewById(R.id.cart);
        cart.setOnClickListener(this);

        addItem(R.drawable.sample, "name1", "100000");
        addItem(R.drawable.sample, "name2", "200000");
        addItem(R.drawable.sample, "name3", "300000");
        addItem(R.drawable.sample, "name4", "400000");
        addItem(R.drawable.sample, "name5", "500000");
        addItem(R.drawable.sample, "name6", "600000");
        addItem(R.drawable.sample, "name7", "700000");
        addItem(R.drawable.sample, "name8", "800000");
        addItem(R.drawable.sample, "name9", "900000");
        addItem(R.drawable.sample, "name10", "1000000");
        adapter.notifyDataSetChanged();
    }

    // @deprecated
    private void addItem(int icon, String mainText, String subText) {
        WishItem item = new WishItem();
        item.setItem_image(icon);
        item.setItem_name(mainText);
        item.setItem_price(subText);
        wishList.add(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cart:
                intent = new Intent(v.getContext(), CartActivity.class);
                v.getContext().startActivity(intent);
        }
    }
}