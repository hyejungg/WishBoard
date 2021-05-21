package com.hyeeyoung.wishboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hyeeyoung.wishboard.R;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    List<String> data;
    LayoutInflater Inflater;

    public SpinnerAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    @Override
    public int getCount() {
        if(data!=null) return data.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // @param : 스피너 클릭 전 보여지는 레이아웃
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view==null) {
            view = Inflater.inflate(R.layout.spinner_custom_frame, viewGroup, false);
        }

        if(data!=null){
            String text = data.get(position);
            ((TextView)view.findViewById(R.id.spinner_text)).setText(text);
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup viewGroup) { //클릭 후 보여지는 레이아웃
        if(view==null){
            view = Inflater.inflate(R.layout.spinner_item, viewGroup, false);
        }

        String text = data.get(position);
        ((TextView)view.findViewById(R.id.spinner_text)).setText(text);

        return view;
    }
}
