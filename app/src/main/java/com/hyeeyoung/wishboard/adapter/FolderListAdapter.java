package com.hyeeyoung.wishboard.adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.model.FolderListItem;
import java.util.ArrayList;
public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.CustomViewHolder> {
    private int lastCheckedPosition = -1;
    private ArrayList<FolderListItem> folderList;

    public FolderListAdapter(ArrayList<FolderListItem> data) {
        this.folderList = data;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView folder_image;
        protected TextView folder_name;
        protected RadioButton btn_radio;
        public CustomViewHolder(View view) {
            super(view);
            this.folder_image = view.findViewById(R.id.folder_image);
            this.folder_name = view.findViewById(R.id.folder_name);
            this.btn_radio = (RadioButton)view.findViewById(R.id.btn_radio);

            btn_radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = getBindingAdapterPosition(); // @brieft : 현재 버튼의 위치
                    btn_radio.setChecked(true);
                    notifyDataSetChanged();
                    Log.i("CheckboxonClick","-------------------------------------"); //@deprecated : test용
                }
            });
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_list_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        final FolderListItem item = folderList.get(position);
        // @see: 카테고리 번호 별 아이템 사진 매칭?
        holder.folder_image.setImageResource(item.getFolderImage());
        holder.folder_name.setText(item.getFolderName());
        holder.btn_radio.setChecked( lastCheckedPosition == position? true : false );

    }
    @Override
    public int getItemCount() {
        return (null != folderList ? folderList.size() : 0);
    }
}