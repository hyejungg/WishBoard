package com.hyeeyoung.wishboard.model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyeeyoung.wishboard.R;

public class NotiItem {
    private int item_image; // @param : 서버연동 후 String으로 변경 예정
    private String item_name;
    private String noti_type;
    private String noti_date;

    public NotiItem() {
    }

    public NotiItem(int item_image, String item_name, String noti_type, String noti_date) {
        this.item_image = item_image;
        this.item_name = item_name;
        this.noti_type = noti_type;
        this.noti_date = noti_date;
    }

    public int getItem_image() {
        return item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getNoti_type() {
        return noti_type;
    }

    public String getNoti_date() {
        return noti_date;
    }

    public void setItem_image(int item_image) {
        this.item_image = item_image;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setNoti_type(String noti_type) {
        this.noti_type = noti_type;
    }

    public void setNoti_date(String noti_date) {
        this.noti_date = noti_date;
    }
}
