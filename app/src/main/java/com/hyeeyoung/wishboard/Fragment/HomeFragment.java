package com.hyeeyoung.wishboard.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.ItemAdapter;
import com.hyeeyoung.wishboard.item.WishItem;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
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

        /* @deprecated
        img = ResourcesCompat.getDrawable(getResources(), R.drawable.bag, null);
        addItem(img, "name1", "100000");
        addItem(img, "name2", "200000");
        addItem(img, "name3", "300000");
        addItem(img, "name4", "400000");
        addItem(img, "name5", "500000");
        addItem(img, "name6", "600000");
        addItem(img, "name7", "700000");
        addItem(img, "name8", "800000");
        addItem(img, "name9", "900000");
        addItem(img, "name10", "1000000");
        */

        addItem(R.mipmap.ic_launcher, "name1", "100000");
        addItem(R.mipmap.ic_launcher, "name2", "200000");
        addItem(R.mipmap.ic_launcher, "name3", "300000");
        addItem(R.mipmap.ic_launcher, "name4", "400000");
        addItem(R.mipmap.ic_launcher, "name5", "500000");
        addItem(R.mipmap.ic_launcher, "name6", "600000");
        addItem(R.mipmap.ic_launcher, "name7", "700000");
        addItem(R.mipmap.ic_launcher, "name8", "800000");
        addItem(R.mipmap.ic_launcher, "name9", "900000");
        addItem(R.mipmap.ic_launcher, "name10", "1000000");
        adapter.notifyDataSetChanged();
    }

    // @deprecated
    private void addItem(int icon, String mainText, String subText) {
        WishItem item = new WishItem();
        item.setItem_image(icon);
        item.setItem_name(mainText);
        item.setItem_price(subText);
        wishList.add(item); }

    /* @deprecated
    private void addItem(Drawable icon, String mainText, String subText) {
        WishItem item = new WishItem();
        item.setItem_image(icon);
        item.setItem_name(mainText);
        item.setItem_price(subText);
        wishList.add(item);
    }*/
}