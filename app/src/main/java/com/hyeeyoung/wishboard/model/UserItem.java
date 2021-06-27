package com.hyeeyoung.wishboard.model;

import androidx.annotation.NonNull;

/**
 * @param : UserItem은 서버와 통신하여 DB에 데이터 값을 넣기 위해 사용되는 Class
 */

public class UserItem {
    public String user_id; // @params : DB에 들어갈 사용자 id //기본 값 null (auto_increamet)
    public String email; // @params : DB에 들어갈 사용자 email
    public String password; // @params : DB에 들어갈 사용자 pw
    private boolean option_notification = true; // @parmas : DB에 들어갈 사용자 option_noti //기본값 true

    public UserItem(){}
    public UserItem(String user_id, String email, String password, boolean option_notification) {
        this.user_id = user_id;
        this.email = email;
        this.password = password;
        this.option_notification = option_notification;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOption_notification() {
        return option_notification;
    }

    public void setOption_notification(boolean option_notification) {
        this.option_notification = option_notification;
    }

    @Override
    public String toString() {
        return "UserItem{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", option_notification=" + option_notification +
                '}';
    }
}
