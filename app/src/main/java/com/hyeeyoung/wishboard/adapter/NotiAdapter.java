package com.hyeeyoung.wishboard.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hyeeyoung.wishboard.R;
import com.hyeeyoung.wishboard.detail.ItemDetailActivity;
import com.hyeeyoung.wishboard.remote.IRemoteService;
import com.hyeeyoung.wishboard.remote.ServiceGenerator;
import com.hyeeyoung.wishboard.util.DateFormatUtil;
import com.hyeeyoung.wishboard.model.NotiItem;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.CustomViewHolder> {
    private static final String TAG = "알림어뎁터";
    private ArrayList<NotiItem> notiList;
    private int noti_layout_color; // @brief : 읽지 않은 알림의 배경색
    private Intent intent;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView item_image;
        protected TextView noti_title;
        protected TextView noti_date;
        protected RelativeLayout noti_layout;


        public CustomViewHolder(View view) {
            super(view);
            this.item_image = view.findViewById(R.id.item_image);
            this.noti_title = view.findViewById(R.id.noti_title);
            this.noti_date = view.findViewById(R.id.noti_date);
            this.noti_layout = view.findViewById(R.id.noti_layout);
        }
    }
    public NotiAdapter(ArrayList<NotiItem> data) {
        this.notiList = data;
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.noti_item, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        noti_layout_color = view.getResources().getColor(R.color.pastelGreen); // @brief : 읽지 않은 알림의 배경색을 지정

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        NotiItem item = notiList.get(position);

        /**
         * @brief : 아이템 이미지를 화면에 보여준다.
         */
        try {
            Picasso.get().load(item.getItem_image()).into(viewholder.item_image); // @brief : 가져온 이미지경로값으로 이미지뷰 디스플레이
        } catch (IllegalArgumentException i) {
            Log.d("checkings", "아이템 사진 없음");
        }

        // @brief : 읽지 않은 알림인 경우 unread 알림 수를 갱신하고 배경색을 설정
        if(item.getIs_read().equals("0")) {
            viewholder.noti_layout.setBackgroundColor(noti_layout_color);
        }
        viewholder.noti_title.setText("[" + item.getItem_notification_type() + "] " + item.getItem_name());
        viewholder.noti_date.setText(DateFormatUtil.beforeTime(item.getItem_notification_date()));

        /**
         * @param : 아이템 클릭 시 아이템 상세조회로 이동
         */
        viewholder.noti_layout.setOnClickListener(v -> {
            String id = item.getItem_id();
            // @brief : 읽지 않은 알림을 클릭한 경우 읽음 처리
            if(item.getIs_read().equals("0")) {
                updateNotiRead(id);
            }
            viewholder.noti_layout.setBackgroundColor(Color.argb(0,0,0,0));

            // @brief : 상세조회로 이동
            intent = new Intent(v.getContext(), ItemDetailActivity.class);
            intent.putExtra("item_id", id);
            v.getContext().startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return (null != notiList ? notiList.size() : 0);
    }

    /**
     * @brief : 서버로 알림 읽음 여부 업데이트를 요청한다.
     * @param item_id 읽지 않은 알림 아이템의 아이디
     */
    private void updateNotiRead(String item_id) {
        IRemoteService remoteService = ServiceGenerator.createService(IRemoteService.class);
        Call<ResponseBody> call = remoteService.updateNotiRead(item_id);
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