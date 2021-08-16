package com.hyeeyoung.wishboard.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedItemVM extends ViewModel {
    // @param : update 여부를 공유하는 변수
    // @brief : 데이터를 감싸기 위한 wrapper클래스

    private MutableLiveData<Boolean> isUpdated = new MutableLiveData<Boolean>();
    private MutableLiveData<String> item_name = new MutableLiveData<String>();
    private MutableLiveData<String> item_price = new MutableLiveData<String>();
    private MutableLiveData<String> item_url = new MutableLiveData<String>();
    private MutableLiveData<String> noti_type = new MutableLiveData<String>();
    private MutableLiveData<String> noti_date = new MutableLiveData<String>();
    private MutableLiveData<String> item_memo = new MutableLiveData<String>();

    public LiveData<String> getItem_name() {
        return item_name;
    }
    public LiveData<String> getItem_price() {
        return item_price;
    }
    public LiveData<String> getItem_url() {
        return item_url;
    }

    public LiveData<String> getNoti_type() {
        return noti_type;
    }

    public LiveData<String> getNoti_date() {
        return noti_date;
    }

    public LiveData<String> getItem_memo() {
        return item_memo;
    }

    public LiveData<Boolean> getIsUpdated(){ // @param : getter
        return isUpdated;
    }

    public void setItem_name(String name) {
        item_name.setValue(name);
    }

    public void setItem_price(String price) {
        item_price.setValue(price);
    }

    public void setItem_url(String url) {
        item_url.setValue(url);
    }

    public void setNoti_type(String type) {
        noti_type.setValue(type);
    }

    public void setNoti_date(String date) {
        noti_date.setValue(date);
    }

    public void setItem_memo(String memo) {
        item_memo.setValue(memo);
    }

    public void setIsUpdated(Boolean updated){ // @param : setter
        isUpdated.setValue(updated);
    }
}
