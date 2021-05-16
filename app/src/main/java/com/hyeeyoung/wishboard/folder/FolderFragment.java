package com.hyeeyoung.wishboard.folder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.adapter.FolderAdapter;
import com.hyeeyoung.wishboard.model.FolderItem;
import com.hyeeyoung.wishboard.model.WishItem;

import java.util.ArrayList;

public class FolderFragment extends Fragment {
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

    private View view;
    RecyclerView recyclerView;
    FolderAdapter adapter;
    private ArrayList<FolderItem> foldersList;
    private GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_folder, container, false);
        init();

        return view;
    }

    private void init(){
        recyclerView = view.findViewById(R.id.recyclerview_folders_list);
        foldersList = new ArrayList<>();
        adapter = new FolderAdapter(foldersList);
        recyclerView.setAdapter(adapter);
        gridLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // @deprecated : add()
        addItem(R.mipmap.ic_launcher, "name1", 32);
        addItem(R.mipmap.ic_launcher, "name2", 1);
        addItem(R.mipmap.ic_launcher, "name3", 5);
        addItem(R.mipmap.ic_launcher, "name4", 2);
        addItem(R.mipmap.ic_launcher, "name5", 124);
        addItem(R.mipmap.ic_launcher, "name6", 5151);
        addItem(R.mipmap.ic_launcher, "name7", 231);
        addItem(R.mipmap.ic_launcher, "name8", 241);
        addItem(R.mipmap.ic_launcher, "name9", 1);
        addItem(R.mipmap.ic_launcher, "name10", 23);

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
}
