package com.hyeeyoung.wishboard.model;

public class NotiItem {
    // @breif : 서버와 연동하기 위해 사용될 변수
    // @params is_read : 알림 읽음 여부
    private String user_id, item_id, item_name, item_image, item_notification_type, item_notification_date, is_read, token;

    public NotiItem() {
    }

    public NotiItem(String user_id, String item_id, String item_notification_type, String item_notification_date) {
        this.user_id = user_id;
        this.item_id = item_id;
        this.item_notification_type = item_notification_type;
        this.item_notification_date = item_notification_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getItem_image() {
        return item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getIs_read() {
        return is_read;
    }

    public String getItem_notification_type() {
        return item_notification_type;
    }

    public String getItem_notification_date() {
        return item_notification_date;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
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
                "item_notification_type='" + item_notification_type + '\'' +
                ", item_notification_date='" + item_notification_date + '\'' +
                ", is_read='" + is_read + '\'' +
                '}';
    }
}
