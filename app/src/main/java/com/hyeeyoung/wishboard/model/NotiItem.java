package com.hyeeyoung.wishboard.model;

public class NotiItem {
    private int item_image; // @param : 서버연동 후 String으로 변경 예정
    private String item_name;
    // @parmas : 서버와 연동하기 위해 사용될 변수
    private String user_id, item_id,item_notification_type, item_notification_date;

    public NotiItem() {
    }

    public String getUser_id() {
        return user_id;
    }

    public int getItem_image() {
        return item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_notification_type() {
        return item_notification_type;
    }

    public String getItem_notification_date() {
        return item_notification_date;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setItem_image(int item_image) {
        this.item_image = item_image;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_notification_type(String item_notification_type) {
        this.item_notification_type = item_notification_type;
    }

    public void setItem_notification_date(String item_notification_date) {
        this.item_notification_date = item_notification_date;
    }

    @Override
    public String toString() {
        return "NotiItem{" +
                ", user_id='" + user_id + '\'' +
                ", item_id='" + item_id + '\'' +
                ", noti_date='" + item_notification_date + '\'' +
                ", noti_type='" + item_notification_type + '\'' +
                '}';
    }
}
